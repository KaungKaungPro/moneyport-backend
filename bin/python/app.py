from flask import Flask, request, jsonify
import pandas as pd
from waitress import serve
from flask_cors import CORS
import yfinance as yf
from datetime import datetime as dt

app = Flask(__name__)
CORS(app)
tks = ['JNJ', 'PG', 'KOF', 'VZ', 'WMT', 'MCD', 'PEP', 'ATT', 'UTG.MU', 'XOM',
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
            map[t]["{}-{}-{}".format(d.year, d.month, d.day)] = dict(
                open=data.loc[d]['Open'][t],
                close=data.loc[d]['Close'][t],
                high=data.loc[d]['High'][t],
                low=data.loc[d]['Low'][t],
                volume=int(data.loc[d]['Volume'][t]))
    
    return jsonify(map)


if __name__ == '__main__':
    print("Starting server on port 5052")
    serve(app, host='127.0.0.1', port=5052)



