package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.StockTracking;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.utils.TradeDate;

@Embeddable
public class VirtualTradingGame {
	
	@Transient
	private MktSim mkt;

	@OneToMany(mappedBy="tradeEnvOwner", fetch=FetchType.LAZY)
	private List<TradeInstruction> instructions;
	
	@OneToMany(mappedBy="tradeEnvOwner", fetch=FetchType.LAZY)
	private List<StockTrade> tradedRecords;
	
	@Transient
	private StockTradeRepository str;
	
	@Transient
	private StockRepository sr;
	
	private int gameDuration;

	private LocalDateTime gameStartDate;
	
	private LocalDate gameDate;
	
	private double v$;
	
	public VirtualTradingGame(StockTradeRepository str, StockRepository sr, MktSimParam params, List<Stock> stocks, User user) {
		this.str = str;
		this.sr = sr;
		this.v$ = 100000.0;
		this.mkt = new MktSim(params, System.currentTimeMillis() % 10000L, stocks, user);
		LocalDate preStartTradeDate = TradeDate.getLatestTradeDateBefore(LocalDate.now());
		this.gameStartDate = LocalDateTime.of(preStartTradeDate, LocalTime.now());
		this.gameDate = preStartTradeDate;
		this.gameDuration = 3;
	}
	
	public VirtualTradingGame() {
		this.v$ = 100000.0;
		this.gameDuration = 3;
	}
	
	public VirtualTradingGame loadMktSim(List<Stock> stocks, MktSimParam params, StockTradeRepository str, StockRepository sr, User user, LocalDate gameDate) {
		this.str = str;
		this.sr = sr;
		this.mkt = new MktSim(params, System.currentTimeMillis() % 10000L, stocks, user);
		this.mkt.setLastTradePrices(str, TradeDate.getLatestTradeDateBefore(gameDate));
		return this;
	}
	
	public boolean EndTurn() {
		if(getGameDay() < gameDuration) {
			gameDate = TradeDate.getNextTradeDateAfter(gameDate);
			return false;
		} else {
			gameStartDate = null;
			gameDate = null;
			v$ = 100000.0;
			return true;
		}
	}

	public MktSim getMkt() {
		return mkt;
	}

	public void setMkt(MktSim mkt) {
		this.mkt = mkt;
	}

	public List<TradeInstruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<TradeInstruction> instructions) {
		this.instructions = instructions;
	}
	
	public void addInstructions(TradeInstruction instruction) {
		this.instructions.add(instruction);
	}
	
	public List<StockTrade> getTradedRecords() {
		return tradedRecords;
	}

	public void setTradedRecords(List<StockTrade> tradedRecords) {
		this.tradedRecords = tradedRecords;
	}

	public LocalDateTime getGameStartDate() {
		return gameStartDate;
	}

	public void setGameStartDate(LocalDateTime gameStartDate) {
		this.gameStartDate = gameStartDate;
	}

	public LocalDate getGameDate() {
		return gameDate;
	}

	public void setGameDate(LocalDate gameDate) {
		this.gameDate = gameDate;
	}
	
	public LocalDate getGameStartDateAsDate() {
		return LocalDate.of(gameStartDate.getYear(), gameStartDate.getMonth(), gameStartDate.getDayOfMonth());
	}
	
	public long getGameDay() {
		return TradeDate.CountGameDays(getGameStartDateAsDate(), gameDate); 
	}

	public double getV$() {
		return v$;
	}

	public void setV$(double v$) {
		this.v$ = v$;
	}
	
	public int getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(int gameDuration) {
		this.gameDuration = gameDuration;
	}

	public void buildTrades(LocalDate tradeDate) {
		mkt.buildAndPersistSingleDayAndBundledTrades(str, sr, tradeDate);
	}
	
	public void buildTrades() {
		buildTrades(TradeDate.getLatestTradeDateBefore(this.getGameStartDateAsDate()));
	}
	
	public void optimizeTradeData() {
		str.optimizePerMinuteTrade(mkt.getUser().getId(), TradeDate.getLatestTradeDateBefore(LocalDate.now()));
	}
	
	public List<StockData> getLatestStockData(StockTradeRepository str, StockRepository sr) {
		LocalDate tradeDate = TradeDate.getLatestTradeDateBefore(gameDate);
		List<StockTrade> latestBundledSingleDayStockTrades = str.getBundledSingleDayTradeByDate(tradeDate, mkt.getUser().getId());
		List<StockData> stockData = new ArrayList<>();
		for(Stock stock : mkt.getStocks()) {
			StockTrade stockTrade = latestBundledSingleDayStockTrades
					.stream()
					.filter(st -> st.getStock().getId() == stock.getId())
					.findFirst()
					.orElse(null);
			if(stockTrade != null) {
				StockData data = new StockData();
				data.setOpen(stockTrade.getOpen().doubleValue());
				data.setClose(stockTrade.getClose().doubleValue());
				data.setHigh(stockTrade.getHigh().doubleValue());
				data.setLow(stockTrade.getLow().doubleValue());
				data.setVolume(stockTrade.getVolume());
				data.setStock(stock);
				stockData.add(data);
			}
			
		}
		return stockData;
	}
	
	public List<StockData> getLatestStockDataFromGameDate(StockTradeRepository str, StockRepository sr, LocalDate gameDate) {
		LocalDate tradeDate = TradeDate.getLatestTradeDateBefore(gameDate);
		List<StockTrade> latestBundledSingleDayStockTrades = str.getBundledSingleDayTradeByDate(tradeDate, mkt.getUser().getId());
		List<StockData> stockData = new ArrayList<>();
		for(Stock stock : mkt.getStocks()) {
			StockTrade stockTrade = latestBundledSingleDayStockTrades
					.stream()
					.filter(st -> st.getStock().getId() == stock.getId())
					.findFirst()
					.orElse(null);
			if(stockTrade != null) {
				StockData data = new StockData();
				data.setOpen(stockTrade.getOpen().doubleValue());
				data.setClose(stockTrade.getClose().doubleValue());
				data.setHigh(stockTrade.getHigh().doubleValue());
				data.setLow(stockTrade.getLow().doubleValue());
				data.setVolume(stockTrade.getVolume());
				data.setStock(stock);
				stockData.add(data);
			}
			
		}
		return stockData;
	}
	
	public void addV$(double change) {
		v$ = v$ + change;
	}
	
	public void minusV$(double change) {
		v$ = v$ - change;
	}
	
}
