from flask import Flask, jsonify, request
import pandas as pd
import pickle
import mysql.connector
from waitress import serve
from flask_cors import CORS
import yfinance as yf
from datetime import datetime as dt
import numpy as np

############################################################################
###                App Initializing Region                               ###
############################################################################

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
    'user': 'java_developer',
    'password': 'Password123!',
    'database': 'moneyport'
}

def get_db_connection():
    try:
        conn = mysql.connector.connect(**db_config)
        return conn
    except Exception as e:
        print(f"Error connecting to MySQL database: {e}")
        return None

############################################################################
###           Risk Assessment Methods and APIs                           ###
############################################################################

def encode_risk_level(risk_level):
    if risk_level == 'R1':
        return 0
    elif risk_level == 'R2':
        return 1
    else:
        return 2

def get_stocks_for_risk_level(risk_level):
    conn = get_db_connection()
    if not conn:
        print("Failed to connect to database")
        return []
    
    try:
        cursor = conn.cursor(dictionary=True)
        query = "SELECT stock_code, stock_name, sector FROM stock WHERE a_class = %s"
        cursor.execute(query, (encode_risk_level(risk_level),))
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

@app.route('/api/predict_from_db/<int:user_id>', methods=['GET'])
def predict_from_db(user_id):
    try:
        # Fetch responses for the given user_id
        conn = get_db_connection()
        print("Connected to python app to predict_from_db")
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
        # stocks_with_prices = fetch_stock_prices(stocks)

        return jsonify({
            'risk_level': risk_level,
            'recommended_stocks': stocks
        })
    
    except Exception as e:
        print(f"Error during prediction: {str(e)}")
        return jsonify({'error': 'Error during prediction'}), 500

############################################################################
###           YFinance Info and Historic Data APIs                       ###
############################################################################


tks = ['JNJ', 'PG', 'KOF', 'VZ', 'WMT', 'MCD', 'PEP', 'T', 'UTG.MU', 'XOM',
        'AAPL', 'MSFT', 'GOOGL', 'AMZN', 'META', 'V', 'JPM', 'DIS', 'NFLX', 'CSCO',
        'TSLA', 'NVDA', 'AMD', 'COIN', 'SQ', 'PLTR', 'NIO', 'BYND', 'PLUG', 'SPCE'
       ]

def getDownloadPeriod(days):
    if days <= 1:
        return '1d'
    elif days <= 5:
        return '5d'
    elif days <= 30:
        return '1mo'
    elif days <= 90:
        return '3mo'
    elif days <= 180:
        return '6mo'
    elif days <= 365:
        return '1y'
    else:
        return '1mo'


@app.route('/api/get_ticker_info/<symbol>', methods=['GET'])
def get_ticker_info(symbol):

    ticker = yf.Ticker(symbol)
    stock_info = ticker.info

    return jsonify(stock_info)

@app.route('/api/get_historic_data', methods=['GET'])
def get_historic_data():
    year = request.args.get('year', type= int)
    month = request.args.get('month', type= int)
    day = request.args.get('day', type= int)
    
    now = dt.now()
    ltd = dt(year, month, day)
    days = (now - ltd).days

    data = yf.download(period=getDownloadPeriod(days), tickers=tks)
    # parse panda dataframe to a map (dict)
    map = { symbol:{} for symbol in tks}
    for d in data.index:
        for t in tks:
            print("Parsing {}. Volume: {}".format(t, data.loc[d]['Volume'][t]))
            map[t]["{}-{}-{}".format(d.year, d.month, d.day)] = dict(
                        open=data.loc[d]['Open'][t],
                        close=data.loc[d]['Close'][t],
                        high=data.loc[d]['High'][t],
                        low=data.loc[d]['Low'][t],
                        volume=int(data.loc[d]['Volume'][t]))    
                
    
    return jsonify(map)


if __name__ == '__main__':
    print("Starting risk assessment ML model server ...")
    serve(app, host='127.0.0.1', port=5082)

