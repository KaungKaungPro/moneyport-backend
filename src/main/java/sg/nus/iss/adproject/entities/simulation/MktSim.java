package sg.nus.iss.adproject.entities.simulation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import sg.nus.iss.adproject.entities.StockTracking;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.utils.ValueRound;


public class MktSim {

	private List<Stock> stocks;
	
	private final int iterationsByMinute = 480;
	
	private MktSimMode mode = MktSimMode.TurnBased;
	
	private MktSimParam params;
	
	private User user;
	
	private Map<String, Double> lastTradePrices = new HashMap<>();
	
	public MktSim(MktSimParam params, long seed, List<Stock> stocks, User user) {
		this.params = params;
		params.setSeed(seed);
		this.stocks = stocks;
		this.user = user;
	}

	public List<Stock> getStocks() {
		return stocks;
	}
	
	public void addStock(Stock s) {
		stocks.add(s);
	}
	
	public MktSimMode getMode() {
		return mode;
	}

	public void setMode(MktSimMode mode) {
		this.mode = mode;
	}

	public MktSimParam getParams() {
		return params;
	}

	public void setParams(MktSimParam params) {
		this.params = params;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Represents the generated single day of trades across all
	// stocks
	private List<StockTrade> simSingleDaySequence(LocalDate asDate) {

		List<StockTrade> totalTrades = new ArrayList<StockTrade>();
		for(int i = 0; i < iterationsByMinute; i++) {
			totalTrades.addAll(perMinuteIteration(i + 1, asDate));
		}
		return totalTrades;
	}
	
	// Represents trades completed in single minute of time
	// Runs through all stocks and generate trades if there is
	private List<StockTrade> perMinuteIteration(int iter, LocalDate dateTraded) {
		List<StockTrade> trades = new ArrayList<StockTrade>();
		for(Stock st : stocks) {
			List<StockTrade> stockTrades = TradeStock(st, iter, dateTraded, 1);
			trades.addAll(stockTrades);
		}
		return trades;
	}
	
	private List<StockTrade> TradeStock(Stock stock, int iter, LocalDate asDate, int multiplier) {
		List<StockTrade> stockTrades = StockTrade.GenerateTrades(stock, lastTradePrices.get(stock.getStockCode()), params, iter, asDate, multiplier);
		double lastTradePrice = stockTrades.isEmpty() ? lastTradePrices.get(stock.getStockCode()) : stockTrades.get(stockTrades.size() - 1).getPrice();
		if(Math.abs(lastTradePrice - lastTradePrices.get(stock.getStockCode())) / lastTradePrices.get(stock.getStockCode()) > 0.02) {
			System.out.println("High volatility in stock " + stock.getStockCode() + " at " + iter);
		}
		lastTradePrices.put(stock.getStockCode(), lastTradePrice);
		return stockTrades;
	}
	
	private void persistStockTrades(StockTradeRepository str, List<StockTrade> trades) {
		for(StockTrade st : trades) {
			st.setTradeEnvOwner(user);
			str.save(st);
		}
	}
	
	private void persistStocks(StockRepository sr, List<Stock> stocks) {
		for(Stock s : stocks) {
			sr.save(s);
		}
	}
	
	public List<StockTrade> buildSingleDayAndBundledTrade(StockTradeRepository str, LocalDate asDate) {
		List<StockTrade> singleDayAndVBundledTrades = new ArrayList<StockTrade>();
		singleDayAndVBundledTrades.addAll(simSingleDaySequence(asDate));
		singleDayAndVBundledTrades.addAll(bundleSingleDayTrade(singleDayAndVBundledTrades, asDate));
		return singleDayAndVBundledTrades;
	}
	
	public void buildAndPersistSingleDayAndBundledTrades(StockTradeRepository str, StockRepository sr, LocalDate asDate) {
		List<StockTrade> singleDayPerMinuteTrades = simSingleDaySequence(asDate);
		List<StockTrade> bundledSingleDayTrades = bundleSingleDayTrade(singleDayPerMinuteTrades, asDate);
		persistStockTrades(str, singleDayPerMinuteTrades);
		persistStockTrades(str, bundledSingleDayTrades);

		for(StockTrade b : bundledSingleDayTrades) {
			System.out.println(b.getStock().getStockCode() + "- Open: " + b.getOpen() + " close: " + b.getClose() + " high: " + b.getHigh() + " low: " + b.getLow());
		}
	}
	
	public List<StockTrade> bundleSingleDayTrade(List<StockTrade> singleDayPerMinuteTrades, LocalDate dateTraded){
		List<StockTrade> bundledSingleDayTrades = new ArrayList<StockTrade>();
		
		for(Stock stock : stocks) {
			List<StockTrade> stockTradesSortedByTime = singleDayPerMinuteTrades
					.stream()
					.filter(st -> st.getStock().getStockCode().equals(stock.getStockCode()))
					.sorted((s1, s2) -> s1.getTimeTraded().isAfter(s2.getTimeTraded()) ? -1 : 1)
					.toList();
			int totalVolume = 0;
			double totalValue = 0.0;
			for(StockTrade st : stockTradesSortedByTime) {
				totalVolume += st.getVolume();
				totalValue += (st.getPrice() * st.getVolume());
			}
			double close = totalValue / totalVolume;
			double open = stockTradesSortedByTime.get(0).getPrice();
			// Sort by price 
			List<StockTrade> stockTradesSortedByPrice = singleDayPerMinuteTrades
					.stream()
					.filter(st -> st.getStock().getStockCode().equals(stock.getStockCode()))
					.sorted((s1, s2) -> (s1.getPrice() - s2.getPrice()) > 0 ? -1 : 1)
					.toList();
			double high = stockTradesSortedByPrice.get(0).getPrice();
			double low = stockTradesSortedByPrice.get(stockTradesSortedByPrice.size() - 1).getPrice();
			System.out.println(stock.getStockCode() + "- Open: " + open + " close: " + close + " high: " + high + " low: " + low);
			StockTrade bundledStockTrade = new StockTrade();
			bundledStockTrade.setBundle(TradeBundle.day);
			bundledStockTrade.setClose(BigDecimal.valueOf(ValueRound.RoundTo(close, 2)));
			bundledStockTrade.setOpen(BigDecimal.valueOf(ValueRound.RoundTo(open, 2)));
			bundledStockTrade.setHigh(BigDecimal.valueOf(ValueRound.RoundTo(high, 2)));
			bundledStockTrade.setLow(BigDecimal.valueOf(ValueRound.RoundTo(low, 2)));
			bundledStockTrade.setDateTraded(dateTraded);
			bundledStockTrade.setStock(stock);
			bundledStockTrade.setVolume(totalVolume);
			
			bundledSingleDayTrades.add(bundledStockTrade);
		}
		
		return bundledSingleDayTrades;
	}
	
	public void executeTradeInstructions(List<TradeInstruction> instructions) {
		// TODO: this should be called right after buildSingleDayTrade is called 
	}
	
	public void setLastTradePrices(StockTradeRepository str, LocalDate lastTradeDate) {
		List<StockTrade> lastStockTrades = str.getBundledSingleDayTradeByDate(lastTradeDate, user.getId());
		if(lastStockTrades.isEmpty()) {
			lastStockTrades = str.getBundledSingleDayTradeByDate(lastTradeDate, User.adminUser().getId());
		}
		lastTradePrices = new HashMap<>();
		lastStockTrades.forEach(st -> {
			lastTradePrices.put(st.getStockCode(), st.getClose().doubleValue());
		});
	}
	
	public Map<String, Double> getLastTradePrices(){
		return this.lastTradePrices;
	}
}