import yfinance as yf
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import pickle
import numpy as np

# Define list of all stock tickers
tickers = ['JNJ', 'PG', 'KO', 'VZ', 'WMT', 'MCD', 'PEP', 'T', 'UL', 'XOM',
           'AAPL', 'MSFT', 'GOOGL', 'AMZN', 'META', 'V', 'JPM', 'DIS', 'NFLX', 'CSCO',
           'TSLA', 'NVDA', 'AMD', 'COIN', 'SQ', 'PLTR', 'NIO', 'BYND', 'PLUG', 'SPCE']

# Fetch historical data for each stock
data = {ticker: yf.download(ticker, start='2000-01-01', end='2024-01-01') for ticker in tickers}

def preprocess_data(df, n_days=1):
    df['Prev Close'] = df['Close'].shift(n_days)
    df = df.dropna()
    return df[['Prev Close', 'Close']]

# Process data for each stock
processed_data = {ticker: preprocess_data(data[ticker]) for ticker in tickers}

# Train a model for each stock
models = {}
for ticker, df in processed_data.items():
    X = df[['Prev Close']]
    y = df['Close']
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, shuffle=False)
    
    model = LinearRegression()
    model.fit(X_train, y_train)
    
    y_pred = model.predict(X_test)
    mse = mean_squared_error(y_test, y_pred)
    r2 = r2_score(y_test, y_pred)
    models[ticker] = model
    
    print(f'{ticker} - Mean Squared Error: {mse}, R2 Score: {r2}')

# Save models to disk
for ticker, model in models.items():
    with open(f'{ticker}_model.pkl', 'wb') as file:
        pickle.dump(model, file)

def load_model(ticker):
    """Load the trained model for a given stock ticker."""
    with open(f'{ticker}_model.pkl', 'rb') as file:
        model = pickle.load(file)
    return model

def predict_future_prices(ticker, prev_close, days=30):
    """Predict future stock prices given the previous close price."""
    model = load_model(ticker)
    predictions = []
    current_close = prev_close
    
    for _ in range(days):
        X = pd.DataFrame({'Prev Close': [current_close]})
        current_close = model.predict(X)[0]
        predictions.append(current_close)
    
    return predictions

if __name__ == "__main__":
    # Example usage
    ticker = 'COIN'  # ticker
    prev_close = 90  # previous close price
    
    future_prices = predict_future_prices(ticker, prev_close, days=30)
    for i, price in enumerate(future_prices):
        print(f"Day {i+1}: Predicted price for {ticker} is {price:.2f}")