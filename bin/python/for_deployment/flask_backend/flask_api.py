from flask import Flask, request, jsonify
import pickle
import pandas as pd
import yfinance as yf
from datetime import datetime, timedelta
from flask_cors import CORS
import os
import math
import mysql.connector
from dotenv import load_dotenv
from mysql.connector import Error
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

# Database connection configuration
db_config = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'database': os.getenv('DB_NAME')
}

def get_db_connection():
    try:
        conn = mysql.connector.connect(**db_config)
        return conn
    except Error as e:
        print(f"Error connecting to MySQL database: {e}")
        return None

app = Flask(__name__)
CORS(app)

def load_model(ticker):
    """Load the trained model for a given stock ticker."""
    model_directory = r'C:\Users\Vani\Desktop\AD project- Portfolio Visualiser\StockPredictionGame\stock-prediction-game\src\flask_backend\models'
    file_path = os.path.join(model_directory, f'{ticker}_model.pkl')
    print(f"Attempting to load model from {file_path}...")
    if not os.path.isfile(file_path):
        raise ValueError(f"Model for ticker {ticker} not found at path {file_path}")
    
    with open(file_path, 'rb') as file:
        model = pickle.load(file)
    return model

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
    app.run(port=5000, debug=True)