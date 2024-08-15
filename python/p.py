from datetime import datetime as dt
from datetime import timedelta as td
import yfinance as yf



now = dt.now()
aDate = dt(year=2024, month=8, day=3)

print(aDate)
print(now)

diff = now - aDate

# tks = ['JNJ', 'PG', 'KOF', 'VZ', 'WMT', 'MCD', 'PEP', 'T', 'UL', 'XOM',
#         'AAPL', 'MSFT', 'GOOGL', 'AMZN', 'FB', 'V', 'JPM', 'DIS', 'NFLX', 'CSCO',
#         'TSLA', 'NVDA', 'AMD', 'COIN', 'SQ', 'PLTR', 'NIO', 'BYND', 'PLUG', 'SPCE'
#        ]

# tks = ['NIO' ,'SQ']

data = yf.Ticker("T")

print(data.info)

map = { "ATT":{}}




# for d in data.index:
#     map["ATT"]["{}-{}-{}".format(d.year, d.month, d.day)] = dict(
#             open=data.loc[d]['Open'],
#             close=data.loc[d]['Close'],
#             high=data.loc[d]['High'],
#             low=data.loc[d]['Low'],
#             volume=data.loc[d]['Volume']
#           )
        

# print(map)

# for d in data.index:
#     print(data.loc[d]['Close']['MSFT'])
