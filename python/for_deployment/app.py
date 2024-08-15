from flask import Flask, jsonify, request
import pandas as pd
import pickle
import mysql.connector
from waitress import serve
from flask_cors import CORS
import yfinance as yf
from datetime import datetime, timedelta
import numpy as np
import os
import math

from mysql.connector import Error
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

############################################################################
###                App Initializing Region                               ###
############################################################################

app = Flask(__name__)
CORS(app)

# Load the pre-trained model and label encoder
model = None
le = None

try:
    model_path = r"C:\Users\GenjiSun\Documents\workspace-spring-tool-suite-4-4.22.1.RELEASE\GDipSA-ADProject6.0\python\for_deployment\models\risk_assessment_model.pkl"
    le_path    = r"C:\Users\GenjiSun\Documents\workspace-spring-tool-suite-4-4.22.1.RELEASE\GDipSA-ADProject6.0\python\for_deployment\models\label_encoder.pkl"
    
    print(f"Attempting to load model from: {model_path}")
    with open(model_path, 'rb') as f:
        model = pickle.load(f)
    print(f"Model loaded successfully. Type: {type(model)}")
    
    print(f"Attempting to load label encoder from: {le_path}")
    with open(le_path, 'rb') as f:
        le = pickle.load(f)
    print("Label encoder loaded successfully")
    
    print("Model and label encoder loaded successfully")
except Exception as e:
    print(f"Error loading model or label encoder: {str(e)}")
    model = None
    le = None

# Database connection configuration
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': 'hao1jie2bao3',
    'database': 'moneyport'
}

def get_db_connection():
    try:
        conn = mysql.connector.connect(**db_config)
        return conn
    except Exception as e:
        print(f"Error connecting to MySQL database: {e}")
        return None

def load_model(ticker):
    """Load the trained model for a given stock ticker."""
    model_directory = r'C:\Users\GenjiSun\Documents\workspace-spring-tool-suite-4-4.22.1.RELEASE\GDipSA-ADProject6.0\python\for_deployment\models'
    file_path = os.path.join(model_directory, f'{ticker}_model.pkl')
    print(f"Attempting to load model from {file_path}...")
    if not os.path.isfile(file_path):
        raise ValueError(f"Model for ticker {ticker} not found at path {file_path}")
    
    with open(file_path, 'rb') as file:
        model = pickle.load(file)
    return model

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

    print(f"Received request for user_id: {user_id}")

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
    
    now = datetime.now()
    ltd = datetime(year, month, day)
    days = (now - ltd).days

    data = yf.download(period=getDownloadPeriod(days), tickers=tks)
    # parse panda dataframe to a map (dict)
    map = { symbol:{} for symbol in tks}
    for d in data.index:
        for t in tks:
            print("Parsing {}. Volume: {}".format(t, data.loc[d]['Volume'][t]))
            try:
                map[t]["{}-{}-{}".format(d.year, d.month, d.day)] = dict(
                        open=data.loc[d]['Open'][t],
                        close=data.loc[d]['Close'][t],
                        high=data.loc[d]['High'][t],
                        low=data.loc[d]['Low'][t],
                        volume=int(data.loc[d]['Volume'][t]))
            except:
                print("Exception when parsing of ticker {}".format(t))

                
                
    
    return jsonify(map)


############################################################################
###                   Price Prediction APIs                              ###
############################################################################

def predict_price(ticker, prev_close):
    model = load_model(ticker)
    if model is None:
        raise ValueError(f"Model for ticker {ticker} not found")
    X = pd.DataFrame({'Prev Close': [prev_close]})
    return model.predict(X)[0]

def get_previous_close(symbol, date):
    date = datetime.strptime(date, '%Y-%m-%d')
    start_date = date - timedelta(days=7)
    end_date = date - timedelta(days=1)
    df = yf.download(symbol, start=start_date, end=end_date)
    if df.empty:
        raise ValueError(f"No data available for {symbol} before {date}")
    return df['Close'].iloc[-1]


@app.route('/api/stocks', methods=['GET'])
def get_all_stocks():
    conn = get_db_connection()
    if not conn:
        return jsonify({'error': 'Failed to connect to database'}), 500
    
    try:
        cursor = conn.cursor(dictionary=True)
        query = "SELECT symbol, name, sector, risk_level FROM stocks"
        cursor.execute(query)
        stocks = cursor.fetchall()
        return jsonify(stocks)
    except Error as e:
        return jsonify({'error': f'Error fetching stocks: {str(e)}'}), 500
    finally:
        if conn.is_connected():
            cursor.close()
            conn.close()

@app.route('/predict', methods=['POST'])
def predict():
    print("Received prediction request")
    try:
        data = request.json
        print(f"Request data: {data}")
        symbol = data['symbol']
        date = data['date']
        user_prediction = float(data['userPrediction'])

        if not symbol or not date:
            return jsonify({'error': 'Missing symbol or date'}), 400

        # Fetch historical data
        end_date = datetime.strptime(date, '%Y-%m-%d')
        start_date = end_date - timedelta(days=7)
        hist_data = yf.download(symbol, start=start_date, end=end_date)

        if hist_data.empty:
            return jsonify({'error': 'No historical data available'}), 400

        prev_close = hist_data['Close'].iloc[-1]

        # Generate predictions for next 7 days
        model = load_model(symbol)
        model_predictions = []
        current_price = prev_close

        for _ in range(7):
            prediction = model.predict([[current_price]])[0]
            model_predictions.append(prediction)
            current_price = prediction

        # Calculate score
        model_prediction = model_predictions[0]
        error_percentage = abs(user_prediction - model_prediction) / model_prediction

        # New scoring system
        if error_percentage > 0.1:  # If error is greater than 10%, score is 0
            score = 0
        else:
            # Logarithmic scoring
            score = max(0, 100 - int(-200 * math.log10(1 - error_percentage)))

        return jsonify({
            'modelPredictions': model_predictions,
            'userPrediction': user_prediction,
            'score': score
        })

    except Exception as e:
        print(f"Error in predict function: {str(e)}")
        return jsonify({'error': str(e)}), 500

@app.route('/stock/<symbol>', methods=['GET'])
def get_stock_data(symbol):
    print(f"Received stock data request for {symbol}")
    try:
        end_date = datetime.now()
        start_date = end_date - timedelta(days=91)  # One extra day to get previous close
        df = yf.download(symbol, start=start_date, end=end_date)

        if df.empty:
            return jsonify({'error': f"No data found for symbol {symbol}"}), 404

        # Generate future dates
        future_dates = [(end_date + timedelta(days=i)).strftime('%Y-%m-%d') for i in range(1, 8)]

        # Format dates consistently
        formatted_dates = df.index.strftime('%Y-%m-%d').tolist()

        stock_data = {
            'symbol': symbol,
            'prices': df['Close'].tolist()[1:],  # Exclude the first day (previous close)
            'dates': formatted_dates[1:],  # Exclude the first day
            'futureDates': future_dates,
            'previousClose': df['Close'].iloc[0]  # Previous closing price
        }
        print(f"Sending stock data: {stock_data}")
        return jsonify(stock_data)
    except Exception as e:
        print(f"Error fetching stock data: {str(e)}")
        return jsonify({'error': str(e)}), 500

@app.route('/analyze-sentiment', methods=['POST'])
def analyze_sentiment():
    print("Entering analyze_sentiment function")
    try:
        data = request.json
        print(f"Received data: {data}")
        headlines = data.get('headlines', [])
        tickers = data.get('tickers', [])

        if not headlines or not tickers or len(headlines) != len(tickers):
            print("Invalid input data")
            return jsonify({'error': 'Invalid input data'}), 400

        analyzer = SentimentIntensityAnalyzer()
        results = []

        conn = get_db_connection()
        if not conn:
            print("Failed to connect to database")
            return jsonify({'error': 'Failed to connect to database'}), 500

        try:
            cursor = conn.cursor()
            for headline, ticker in zip(headlines, tickers):
                sentiment_scores = analyzer.polarity_scores(headline)
                sentiment_score = sentiment_scores['compound']
                sentiment_label = 'positive' if sentiment_score > 0.05 else 'negative' if sentiment_score < -0.05 else 'neutral'

                print(f"Inserting: headline='{headline}', ticker='{ticker}', score={sentiment_score}, label='{sentiment_label}'")

                cursor.execute("""
                    INSERT INTO sentiment_results (headline, ticker, score, sentiments)
                    VALUES (%s, %s, %s, %s)
                """, (headline, ticker, sentiment_score, sentiment_label))
                
                print(f"Rows affected: {cursor.rowcount}")
                
                results.append({
                    'headline': headline,
                    'score': sentiment_score,
                    'sentiments': sentiment_label,
                    'ticker': ticker
                })
            
            conn.commit()
            print(f"Committed {len(results)} results to database")

        except Exception as e:
            conn.rollback()
            print(f"Error in database operation: {str(e)}")
            return jsonify({'error': f'Database error: {str(e)}'}), 500
        finally:
            if conn.is_connected():
                cursor.close()
                conn.close()
                print("Database connection closed")

        print(f"Returning {len(results)} results")
        return jsonify(results)
    except Exception as e:
        print(f"Unexpected error in analyze_sentiment function: {str(e)}")
        return jsonify({'error': str(e)}), 500

@app.route('/api/news/sentiment/<symbol>', methods=['GET'])
def get_sentiment_for_stock(symbol):
    print(f"Fetching sentiment for symbol: {symbol}")
    try:
        conn = get_db_connection()
        if not conn:
            print("Failed to connect to database")
            return jsonify({'error': 'Failed to connect to database'}), 500

        try:
            cursor = conn.cursor(dictionary=True)
            # Query to select distinct entries, calculate average, and get the latest model sentiment
            query = """
            SELECT 
                DISTINCT headline, 
                ticker, 
                score, 
                sentiments,
                (SELECT score FROM sentiment_results WHERE ticker = %s ORDER BY id DESC LIMIT 1) as model_sentiment
            FROM sentiment_results 
            WHERE ticker = %s 
            ORDER BY score DESC
            """
            cursor.execute(query, (symbol, symbol))
            results = cursor.fetchall()

            # Calculate average sentiment
            if results:
                avg_sentiment = sum(result['score'] for result in results) / len(results)
                model_sentiment = results[0]['model_sentiment']  # Get the latest model sentiment
            else:
                avg_sentiment = 0
                model_sentiment = 0

            response_data = {
                'sentiments': results,
                'average_sentiment': avg_sentiment,
                'model_sentiment': model_sentiment
            }

            print(f"Found {len(results)} unique sentiment results for {symbol}")
            return jsonify(response_data)

        except Exception as e:
            print(f"Error fetching sentiment data: {str(e)}")
            return jsonify({'error': f'Error fetching sentiment data: {str(e)}'}), 500
       
        finally:
            if conn.is_connected():
                cursor.close()
                conn.close()
                print("Database connection closed")

    except Exception as e:
        print(f"Unexpected error in get_sentiment_for_stock function: {str(e)}")
        return jsonify({'error': str(e)}), 500






if __name__ == '__main__':
    print("Starting risk assessment ML model server ...")
    serve(app, host='127.0.0.1', port=5082)

