package sg.nus.iss.adproject.entities.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainBoard {
	
	private List<StockTrade> tradesOnPreviousTradeDate;
	
	private List<StockTrade> tradesOnTradeDateBeforePreviousTradeDate;
	
	private List<StockData> top5Volume;
	
	private List<StockData> top5Gainers;
	
	private List<StockData> bottom5Volume;
	
	private List<StockData> top5loss;
	
	private List<StockData> stockData;
	
	private Map<String, Double> lastTradePrices;
	
	public MainBoard() {
		
	}

	public List<StockData> getTop5Volume() {
		return top5Volume;
	}

	public void setTop5Volume(List<StockData> top5Volume) {
		this.top5Volume = top5Volume;
	}

	public List<StockData> getTop5Gainers() {
		return top5Gainers;
	}

	public void setTop5Gainers(List<StockData> top5Gainers) {
		this.top5Gainers = top5Gainers;
	}

	public List<StockData> getBottom5Volume() {
		return bottom5Volume;
	}

	public void setBottom5Volume(List<StockData> bottom5Volume) {
		this.bottom5Volume = bottom5Volume;
	}

	public List<StockData> getTop5loss() {
		return top5loss;
	}

	public void setTop5loss(List<StockData> top5loss) {
		this.top5loss = top5loss;
	}
	
	public void setLastTradePrices(MktSim mktSim) {
		this.lastTradePrices = mktSim.getLastTradePrices();
	}
	
	public void computeBoardData() {
		int total = stockData.size();
		System.out.println("Computing board data ...");
		if(tradesOnPreviousTradeDate.size() != total || tradesOnTradeDateBeforePreviousTradeDate.size() != total) {
			System.out.println("Inconsistent trade data sizes ... Previous date:" + tradesOnPreviousTradeDate.size() + ", date before previous date: " + tradesOnTradeDateBeforePreviousTradeDate.size());
		}
		List<String> stockCodeSortedByVolume = tradesOnPreviousTradeDate
				.stream()
				.sorted(Comparator.comparing(StockTrade::getVolume, Comparator.reverseOrder()))
				.map(st -> st.getStock().getStockCode())
				.toList();

		this.top5Volume = stockData
				.stream()
				.filter(sd -> stockCodeSortedByVolume.subList(0, 5).contains(sd.getStock().getStockCode()))
				.sorted(Comparator.comparing(StockData::getVolume))
				.toList();
		
		this.bottom5Volume = stockData
				.stream()
				.filter(sd -> stockCodeSortedByVolume.subList(total - 5, total).contains(sd.getStock().getStockCode()))
				.sorted(Comparator.comparing(StockData::getVolume))
				.toList();

		Map<String, Double> stockGains = new HashMap<>();
		Map<String, Double> prevClose =new HashMap<>();
		for(StockTrade previousStockTrade : tradesOnPreviousTradeDate) {
			String stockCode = previousStockTrade.getStock().getStockCode();
			StockTrade tradeOnTradeDateBeforePreviousTradeDate = tradesOnTradeDateBeforePreviousTradeDate
					.stream()
					.filter(st -> st.getStock().getStockCode().equals(stockCode))
					.findFirst()
					.get();
			double gain = (previousStockTrade.getClose().doubleValue() - tradeOnTradeDateBeforePreviousTradeDate.getClose().doubleValue()) / tradeOnTradeDateBeforePreviousTradeDate.getClose().doubleValue();
			stockGains.put(stockCode, gain);
			prevClose.put(stockCode, tradeOnTradeDateBeforePreviousTradeDate.getClose().doubleValue());
		}
		
		stockData.forEach(sd -> {
			sd.setGain(stockGains.get(sd.getStockCode()));
			sd.setPrevClose(prevClose.get(sd.getStockCode()));
		});
		List<String> stockCodeSortedByGains = tradesOnPreviousTradeDate
				.stream()
				.sorted((st1, st2) -> stockGains.get(st1.getStock().getStockCode()) > stockGains.get(st2.getStock().getStockCode()) ? -1 : 1)
				.map(st -> st.getStock().getStockCode())
				.toList();

		this.top5Gainers = stockData
				.stream()
				.filter(sd -> stockCodeSortedByGains.subList(0, 5).contains(sd.getStock().getStockCode()))
				.sorted((sd1, sd2) -> stockGains.get(sd1.getStockCode()) > stockGains.get(sd2.getStockCode()) ? -1 : 1)
				.toList();
		this.top5loss = stockData
				.stream()
				.filter(sd -> stockCodeSortedByGains.subList(total - 5, total).contains(sd.getStock().getStockCode()))
				.sorted((sd1, sd2) -> stockGains.get(sd1.getStockCode()) > stockGains.get(sd2.getStockCode()) ? 1 : -1)
				.toList();
		
	}

	public List<StockData> getStockData() {
		return stockData;
	}

	public void setStockData(List<StockData> stockData) {
		this.stockData = stockData;
	}

	public List<StockTrade> getPreviousDayTrade() {
		return tradesOnPreviousTradeDate;
	}

	public void setTradesOnPreviousTradeDate(List<StockTrade> tradesOnPreviousTradeDate) {
		this.tradesOnPreviousTradeDate = tradesOnPreviousTradeDate;
	}

	public List<StockTrade> getDayBeforeYesterdayTrade() {
		return tradesOnTradeDateBeforePreviousTradeDate;
	}

	public void setTradesOnTradeDateBeforePreviousTradeDate(List<StockTrade> tradesOnTradeDateBeforePreviousTradeDate) {
		this.tradesOnTradeDateBeforePreviousTradeDate = tradesOnTradeDateBeforePreviousTradeDate;
	}
	
	public Map<String, List<Map<String, String>>> getMainBoardMap() {
		Map<String, List<Map<String, String>>> mbm = new HashMap<>();
		List<Map<String, String>> top5GainersList = new ArrayList<>();
		List<Map<String, String>> top5VolumeList = new ArrayList<>();
		List<Map<String, String>> bottom5VolumeList = new ArrayList<>();
		List<Map<String, String>> top5LossList = new ArrayList<>();
		List<Map<String, String>> stockDataList = new ArrayList<>();
		
		for(StockData sd : top5Gainers) {
			Map<String, String> stockMap = new HashMap<>();
			stockMap.put("stockCode", sd.getStock().getStockCode());
			stockMap.put("stockName", sd.getStock().getStockName());
			stockMap.put("open", String.valueOf(sd.getOpen()));
			stockMap.put("close", String.valueOf(sd.getClose()));
			stockMap.put("prevClose", String.valueOf(sd.getPrevClose()));
			stockMap.put("gain", String.valueOf(sd.getGain()));
			top5GainersList.add(stockMap);
		}
		
		for(StockData sd : top5Volume) {
			Map<String, String> stockMap = new HashMap<>();
			stockMap.put("stockCode", sd.getStock().getStockCode());
			stockMap.put("stockName", sd.getStock().getStockName());
			stockMap.put("open", String.valueOf(sd.getOpen()));
			stockMap.put("close", String.valueOf(sd.getClose()));
			stockMap.put("volume", String.valueOf(sd.getVolume()));
			stockMap.put("lastTradedPrice", String.valueOf(lastTradePrices.get(sd.getStockCode())));
			top5VolumeList.add(stockMap);
		}
		
		for(StockData sd : bottom5Volume) {
			Map<String, String> stockMap = new HashMap<>();
			stockMap.put("stockCode", sd.getStock().getStockCode());
			stockMap.put("stockName", sd.getStock().getStockName());
			stockMap.put("open", String.valueOf(sd.getOpen()));
			stockMap.put("close", String.valueOf(sd.getClose()));
			stockMap.put("volume", String.valueOf(sd.getVolume()));
			stockMap.put("lastTradedPrice", String.valueOf(lastTradePrices.get(sd.getStockCode())));
			bottom5VolumeList.add(stockMap);
		}
		
		for(StockData sd : top5loss) {
			Map<String, String> stockMap = new HashMap<>();
			stockMap.put("stockCode", sd.getStock().getStockCode());
			stockMap.put("stockName", sd.getStock().getStockName());
			stockMap.put("open", String.valueOf(sd.getOpen()));
			stockMap.put("close", String.valueOf(sd.getClose()));
			stockMap.put("prevClose", String.valueOf(sd.getPrevClose()));
			stockMap.put("gain", String.valueOf(sd.getGain()));
			top5LossList.add(stockMap);
		}
		
		for(StockData sd : stockData) {
			Map<String, String> stockMap = new HashMap<>();
			stockMap.put("stockCode", sd.getStock().getStockCode());
			stockMap.put("stockName", sd.getStock().getStockName());
			stockMap.put("open", String.valueOf(sd.getOpen()));
			stockMap.put("close", String.valueOf(sd.getClose()));
			stockMap.put("high", String.valueOf(sd.getHigh()));
			stockMap.put("low", String.valueOf(sd.getLow()));
			stockMap.put("prevClose", String.valueOf(sd.getPrevClose()));
			stockMap.put("gain", String.valueOf(sd.getGain()));
			stockMap.put("lastTradePrice", String.valueOf(lastTradePrices.get(sd.getStockCode())));
			stockDataList.add(stockMap);
		}
		
		mbm.put("top5Gainers", top5GainersList);
		mbm.put("top5Volume", top5VolumeList);
		mbm.put("bottom5Volume", bottom5VolumeList);
		mbm.put("top5Loss", top5LossList);
		mbm.put("stockData", stockDataList);
		
		return mbm;
	}
	
	public static Map<String, String> getStockDataMap(Map<String, String> scoreMap, List<StockData> stockData) {

		for(StockData sd : stockData) {
			scoreMap.put(sd.getStock().getStockCode(), String.valueOf(sd.getClose()));
		}
		return scoreMap;
	}
}
