from flask import Flask, jsonify
import pandas as pd
import pickle
import mysql.connector
from waitress import serve
from flask_cors import CORS
import yfinance as yf

app = Flask(__name__)
CORS(app)

# Load the pre-trained model and label encoder
model = None
le = None

try:
    model_path = r"C:\Users\user\Desktop\GDipSA 58\06 ADProject\ml model\AD_Project_Machine Learning\models\risk_assessment_model.pkl"
    le_path    = r"C:\Users\user\Desktop\GDipSA 58\06 ADProject\ml model\AD_Project_Machine Learning\models\label_encoder.pkl"
    
    with open(model_path, 'rb') as f:
        model = pickle.load(f)
    with open(le_path, 'rb') as f:
        le = pickle.load(f)
    
    print("Model and label encoder loaded successfully")
except Exception as e:
    print(f"Error loading model or label encoder: {str(e)}")

# Database connection configuration
db_config = {
    'host': 'localhost',
    'user': 'username',
    'password': '*****!',
    'database': 'db'
}

def get_db_connection():
    try:
        conn = mysql.connector.connect(**db_config)
        return conn
    except Exception as e:
        print(f"Error connecting to MySQL database: {e}")
        return None

def get_stocks_for_risk_level(risk_level):
    conn = get_db_connection()
    if not conn:
        print("Failed to connect to database")
        return []
    
    try:
        cursor = conn.cursor(dictionary=True)
        query = "SELECT symbol, name, sector FROM stocks WHERE risk_level = %s"
        cursor.execute(query, (risk_level,))
        stocks = cursor.fetchall()
        print(f"Fetched {len(stocks)} stocks for risk level {risk_level}")
        return stocks
    except Exception as e:
        print(f"Error fetching stocks for risk level {risk_level}: {e}")
        return []
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()

def fetch_stock_prices(stocks):
    stock_data = []
    for stock in stocks:
        symbol = stock['symbol']
        try:
            ticker = yf.Ticker(symbol)
            stock_info = ticker.info

            price = stock_info.get('regularMarketPrice')
            if price is None:
                price = stock_info.get('previousClose')
                if price is None:
                    print(f"Price for {symbol} is not available.")
                    price = 'N/A'
                else:
                    price = f"{price:.2f} (previous close)"
            else:
                price = f"{price:.2f} (regular market)"
        except Exception as e:
            print(f"Error fetching price for {symbol}: {str(e)}")
            price = 'N/A'

        stock_data.append({
            'symbol': symbol,
            'name': stock['name'],
            'sector': stock['sector'],
            'price': price
        })
    return stock_data


# @app.route('/api/responses', methods=['POST'])
# def save_responses():
#     try:
#         data = request.get_json()
#         user_id = data.get('userId')
#         responses = data.get('responses')

#         if not user_id or not responses:
#             return jsonify({'error': 'User ID or responses are missing'}), 400

#         conn = get_db_connection()
#         if not conn:
#             return jsonify({'error': 'Database connection failed'}), 500

#         cursor = conn.cursor()

#         # Delete previous responses for the user
#         cursor.execute("DELETE FROM responses WHERE user_id = %s", (user_id,))

#         # Insert new responses
#         insert_query = """
#             INSERT INTO responses (user_id, question_id, response_value)
#             VALUES (%s, %s, %s)
#         """
#         for response in responses:
#             cursor.execute(insert_query, (user_id, response['questionId'], response['responseValue']))

#         conn.commit()
#         cursor.close()
#         conn.close()

#         return jsonify({'message': 'Responses saved successfully'})

#     except Exception as e:
#         print(f"Error saving responses: {str(e)}")
#         return jsonify({'error': 'Error saving responses'}), 500

@app.route('/predict_from_db/<int:user_id>', methods=['GET'])
def predict_from_db(user_id):
    try:
        # Fetch responses for the given user_id
        conn = get_db_connection()
        if not conn:
            return jsonify({'error': 'Database connection failed'}), 500
        
        cursor = conn.cursor(dictionary=True)
        cursor.execute("""
            SELECT response_value FROM responses WHERE user_id = %s ORDER BY question_id
        """, (user_id,))
        
        rows = cursor.fetchall()
        cursor.close()
        conn.close()

        if not rows or len(rows) < 7:
            return jsonify({'error': 'Incomplete or missing responses for this user'}), 400

        # Convert fetched response values to the required format
        responses = [row['response_value'] for row in rows]
        input_data = pd.DataFrame([responses], columns=[
            'investmentGoal', 'timeHorizon', 'riskTolerance', 'income',
            'investmentExperience', 'lossReaction', 'age'
        ])

        # Make prediction using the model
        prediction = model.predict(input_data)
        risk_level = le.inverse_transform(prediction)[0]

        # Fetch stocks based on predicted risk level
        stocks = get_stocks_for_risk_level(risk_level)
        stocks_with_prices = fetch_stock_prices(stocks)

        return jsonify({
            'risk_level': risk_level,
            'recommended_stocks': stocks_with_prices
        })
    
    except Exception as e:
        print(f"Error during prediction: {str(e)}")
        return jsonify({'error': 'Error during prediction'}), 500

if __name__ == '__main__':
    serve(app, host='0.0.0.0', port=8082)

