import pickle
import pandas as pd
import yfinance as yf
from sklearn.metrics import r2_score

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
    ticker = 'AMZN'  # ticker
    prev_close = 180  # previous close price
    
    # Fetch actual prices for comparison and R2 calculation
    actual_prices = yf.download(ticker, start='2024-01-01', end='2024-02-01')['Close'].tolist()
    
    future_prices = predict_future_prices(ticker, prev_close, days=len(actual_prices)-1)
    
    # Calculate R2 score
    r2 = r2_score(actual_prices[1:], future_prices)
    print(f"R2 Score for predictions: {r2}")
    
    for i, price in enumerate(future_prices):
        print(f"Day {i+1}: Predicted price for {ticker} is {price:.2f}")