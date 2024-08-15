package sg.nus.iss.adproject.entities.simulation;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import reactor.core.Disposable;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;



@Entity
public class StockTrade {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Stock stock;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private User tradeEnvOwner;
	
	private double price;
	
	private BigDecimal high;
	
	private BigDecimal low;
	
	private BigDecimal open;
	
	private BigDecimal close;
	
	private long volume;
	
	private LocalDate dateTraded;
	
	private LocalTime timeTraded;
	
	private TradeStatus status;
	
	private TradeBundle bundle;

	public StockTrade(Stock stock, double price, long volume, TradeBundle bundle) {
		super();
		this.stock = stock;
		this.price = price;
		this.volume = volume;
		this.dateTraded = LocalDate.now();
		this.timeTraded = LocalTime.now();
		this.bundle = bundle;
		this.status = TradeStatus.Completed;
	}

	public StockTrade(Stock stock, double price, long volume, TradeBundle bundle, LocalTime timeTraded, LocalDate dateTraded) {
		super();
		this.stock = stock;
		this.price = price;
		this.volume = volume;
		this.bundle = bundle;
		this.timeTraded = timeTraded;
		this.dateTraded = dateTraded;
		this.status = TradeStatus.Completed;
	}
	
	public StockTrade() {}
	
	public Stock getStock() {
		return stock;
	}
	
	public String getStockCode() {
		return stock.getStockCode();
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public LocalTime getTimeTraded() {
		return timeTraded;
	}

	public void setTimeTraded(LocalTime timeTraded) {
		this.timeTraded = timeTraded;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDateTraded() {
		return dateTraded;
	}

	public void setDateTraded(LocalDate dateTraded) {
		this.dateTraded = dateTraded;
	}
	
	public int getYear() {
		return dateTraded.getYear();
	}

	public TradeStatus getStatus() {
		return status;
	}

	public void setStatus(TradeStatus status) {
		this.status = status;
	}
	
	public User getTradeEnvOwner() {
		return tradeEnvOwner;
	}

	public void setTradeEnvOwner(User tradeEnvOwner) {
		this.tradeEnvOwner = tradeEnvOwner;
	}

	public TradeBundle getBundle() {
		return bundle;
	}

	public void setBundle(TradeBundle bundle) {
		this.bundle = bundle;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public static StockTrade GenerateTrade(Stock stock, double currentPrice, MktSimParam params, int iter, LocalDate dateTraded, int multiplier) {
		
		StockTrade trade = new StockTrade(stock, currentPrice * params.genPriceVariation(stock.getaClass()), multiplier * params.genTradeVolume(stock.getaClass()) * 1000, TradeBundle.getBundleBySize(multiplier));
		int hour = 9 + iter / 60;
		int minute = iter % 60;
		trade.setTimeTraded(LocalTime.of(hour, minute));
		trade.setDateTraded(dateTraded);
		return trade;
	}
	
	public static List<StockTrade> GenerateTrades(Stock stock, double currentPrice, MktSimParam params, int iter, LocalDate dateTraded, int multiplier){
		int count = params.genTradeCount(stock.getaClass());
		List<StockTrade> trades = new ArrayList<StockTrade>();
		for(int i : new int[count]) {
			trades.add(GenerateTrade(stock, currentPrice, params, iter, dateTraded, multiplier));
		}
		List<StockTrade> consolidatedTrade = new ArrayList<StockTrade>();
		if(trades.size() <= 1) {
			consolidatedTrade = trades;
		} else {
			double finalPrice = 0.0;
			Long totalVolume = 0L;
			for(StockTrade st : trades) {
				finalPrice = finalPrice + st.getPrice() * st.getVolume();
				totalVolume = totalVolume + st.getVolume();
			}
			StockTrade summedUpTrade = new StockTrade(stock, finalPrice / totalVolume, totalVolume, TradeBundle.minute);
			summedUpTrade.setTimeTraded(trades.get(0).getTimeTraded());
			summedUpTrade.setDateTraded(trades.get(0).getDateTraded());
			consolidatedTrade.add(summedUpTrade);
		}
		return consolidatedTrade;
	}
	
	@Override
	public String toString() {
		return "Stock " + stock.getStockCode() + " traded " + volume + " shares, at $" + price;  
	}
	
	public Map<String, String> getStockTradeMap(){
		Map<String, String> stockTradeMap = new HashMap<>();
		stockTradeMap.put("price", String.valueOf(price));
		stockTradeMap.put("close", String.valueOf(close));
		stockTradeMap.put("timeTraded", String.valueOf(timeTraded));
		stockTradeMap.put("dateTraded", String.valueOf(dateTraded));
		stockTradeMap.put("stockCode", stock.getStockCode());
		stockTradeMap.put("stockName", stock.getStockName());
//		stockTradeMap.put("lastTradePrice", String.valueOf(close));
		return stockTradeMap;
	}
	
	public static void UpdateRealWorldStockTradeData(StockRepository sr, StockTradeRepository str, WebClient wc, LocalDate lastRealWorldDataTradeDate, Runnable runnable) {
				wc
				.get()
				.uri(uriBuilder -> {
					return uriBuilder
					.path("/api/get_historic_data")
					.queryParam("year", lastRealWorldDataTradeDate.getYear())
					.queryParam("month", lastRealWorldDataTradeDate.getMonth().getValue())
					.queryParam("day", lastRealWorldDataTradeDate.getDayOfMonth())
					.build();
				})
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Map.class)
				.subscribe(
				value -> {
					Map<String, Map<String,Object>> result = (Map<String, Map<String, Object>>) value;
					System.out.println("loading real data from ... " + lastRealWorldDataTradeDate);
					for(String ticker : result.keySet()) {
						Stock tickerStock = sr.findByStockCode(ticker);
						Map<String, Object> tickerMap = result.get(ticker);
						for(String dateString : tickerMap.keySet()) {
							LocalDate dateTraded = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-M-d"));
							if(dateTraded.isAfter(lastRealWorldDataTradeDate)) {
								Map<String, Object> tradeMap = (Map<String, Object>) tickerMap.get(dateString);
								StockTrade newStockTrade = new StockTrade();
								newStockTrade.setBundle(TradeBundle.day);
								newStockTrade.setClose(BigDecimal.valueOf(Double.parseDouble(tradeMap.get("close").toString())));
								newStockTrade.setOpen(BigDecimal.valueOf(Double.parseDouble(tradeMap.get("open").toString())));
								newStockTrade.setHigh(BigDecimal.valueOf(Double.parseDouble(tradeMap.get("high").toString())));
								newStockTrade.setLow(BigDecimal.valueOf(Double.parseDouble(tradeMap.get("low").toString())));
								newStockTrade.setVolume(Long.parseLong(tradeMap.get("volume").toString()));
								newStockTrade.setStock(tickerStock);
								newStockTrade.setDateTraded(dateTraded);
								newStockTrade.setTradeEnvOwner(User.adminUser());
								str.save(newStockTrade);
							}
						}
					}
					str.flush();
					System.out.println("data flushed ... " );
				},
				error -> {
					System.out.println("Error " + error);
				},
				() -> {
					str.flush();
					System.out.println("Completed loading realworld stock trades.");
					runnable.run();
					
				}
			);
		
	}
	
}
