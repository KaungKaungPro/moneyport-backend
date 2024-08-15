package sg.nus.iss.adproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.MainBoard;
import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.entities.simulation.StockTradeViewMode;
import sg.nus.iss.adproject.entities.simulation.TradeInstruction;
import sg.nus.iss.adproject.entities.simulation.VirtualTradingGame;

public interface VirtualTradeInterface {

	List<Stock> getStocks(long userId);
	
	Stock getStockByStockCode(String stockCode);
	
	void prepMkt(User user);
	
	MktSimParam getLatestMktSimParam();
	
	void startVirtualTradeEnv(User user, WebClient wc, int gameDuration);
	
	void startVirtualTradeEnv(Long userId, WebClient wc, int gameDuration);
	
	Map<String, List<Map<String, String>>> getVirtualStats(User user);
	
	boolean nextGameDay(User user);
	
	List<StockTrade> getStockTrades(String stockCode, StockTradeViewMode mode, long userId);
	
	List<Portfolio> getPortfolios(long userId);
	
	List<Portfolio> getGamePortfolios(long userId, LocalDateTime gameStartDateTime);
	
	MainBoard getMainBoardToday(long userId);
	
	MainBoard getLast2TradeDaysTrades(MainBoard mb, User user);
	
	void buildTrades(long userId, LocalDate tradeDate);
	
	void clearStockTrade(Long userId, LocalDate gameStartDate);
}
