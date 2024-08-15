package sg.nus.iss.adproject.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.MainBoard;
import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.PortfolioStock;
import sg.nus.iss.adproject.entities.simulation.PredictedPrice;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.entities.simulation.StockData;
import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.entities.simulation.StockTradeViewMode;
import sg.nus.iss.adproject.entities.simulation.TradeBundle;
import sg.nus.iss.adproject.entities.simulation.TradeInstruction;
import sg.nus.iss.adproject.entities.simulation.TradeOp;
import sg.nus.iss.adproject.entities.simulation.VirtualTradingGame;
import sg.nus.iss.adproject.repositories.MktSimParamRepository;
import sg.nus.iss.adproject.repositories.PortfolioRepository;
import sg.nus.iss.adproject.repositories.PortfolioStockRepository;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;
import sg.nus.iss.adproject.repositories.UserRepository;
import sg.nus.iss.adproject.utils.TradeDate;

@Service
@Transactional
public class VirtualTradeService implements VirtualTradeInterface{

	@Autowired
	private StockRepository sr;
	
	@Autowired
	private MktSimParamRepository mspr;
	
	@Autowired
	private StockTradeRepository str;
	
	@Autowired
	private PortfolioRepository pr;
	
	@Autowired
	private UserRepository ur;
	

	@Override
	public List<Stock> getStocks(long userId) {
		return sr.findAll();
	}
	
	@Override
	public Stock getStockByStockCode(String stockCode) {
		return sr.findByStockCode(stockCode);
	}
	
	@Override
	public MktSimParam getLatestMktSimParam() {
		List<MktSimParam> mktSimParams = mspr.getMktSimParamsOrderByCreationDate();
		if(mktSimParams.isEmpty()) {
			return null;
		}
		return mktSimParams.get(0);
	}
	
	@Override
	public void startVirtualTradeEnv(Long userId, WebClient wc, int gameDuration) {
		User user = ur.findUserById(userId);
		if(user != null) {
			startVirtualTradeEnv(user, wc, gameDuration);
		}
	}
	
	@Override
	public void startVirtualTradeEnv(User user, WebClient wc, int gameDuration) {
		if(user != null) {
			List<Stock> stocks = getStocks(user.getId());
			MktSimParam param = getLatestMktSimParam();
			if(user.getVtGame().getGameStartDate() == null) {
				VirtualTradingGame vt = new VirtualTradingGame(str, sr, param, stocks, user);
				vt.setGameDuration(gameDuration);
				user.setVtGame(vt);
				ur.save(user);
				Portfolio gamePortfolio = new Portfolio();
				gamePortfolio.setGameStartDateTime(user.getVtGame().getGameStartDate());
				gamePortfolio.setIndex(true);
				gamePortfolio.setPortfolioName("My First Portfolio");
				gamePortfolio.setUser(user);
				pr.save(gamePortfolio);
				LocalDate lastRealWorldDataTradeDate = TradeDate.realWorldTradeDataLastDate(str);
				StockTrade.UpdateRealWorldStockTradeData(
						sr, 
						str, 
						wc, 
						lastRealWorldDataTradeDate, 
						() -> {
							user.loadVtGame(stocks, param, str, sr);
							user.getVtGame().buildTrades();
						});
			} else {
				user.loadVtGame(stocks, param, str, sr);
			}
			
		}
	}
	
	
	
	@Override
	public void buildTrades(long userId, LocalDate tradeDate) {
		User user = ur.findUserById(userId);
		MktSimParam param = getLatestMktSimParam();
		List<Stock> stocks = getStocks(user.getId());
		user.loadVtGame(stocks, param, str, sr);
		user.getVtGame().buildTrades(tradeDate);
	}
	
	@Override
	public Map<String, List<Map<String, String>>> getVirtualStats(User user){
		Map<String, List<Map<String, String>>> stats = new HashMap<>();
		
		Map<String, String> pfc = new HashMap<>();
		List<Portfolio> gamePortfolios = user.getGamePortfolios(user.getVtGame().getGameStartDate());
		pfc.put("pfc", String.valueOf(gamePortfolios.size()));
		stats.put("portfolioCount", List.of(pfc));

		stats.put("portfolios", Portfolio.getPortfolioListMap(gamePortfolios));
		Map<String, String> vdMap = new HashMap<>();
		vdMap.put("amount", String.valueOf(user.getVtGame().getV$()));
		stats.put("v$", List.of(vdMap));
		Map<String, String> gdMap = new HashMap<>();
		gdMap.put("gameDay", String.valueOf(user.getVtGame().getGameDay()));
		stats.put("gameDay", List.of(gdMap));
		return stats;
	}
	
	@Override
	public void prepMkt(User user) {
		MktSimParam param = getLatestMktSimParam();
		List<Stock> stocks = getStocks(user.getId());
		user.loadVtGame(stocks, param, str, sr);
	}
	
	
	@Override
	public boolean nextGameDay(User user) {
		prepMkt(user);
		LocalDate currentGameDate = user.getVtGame().getGameDate();
		boolean gameEnd = user.getVtGame().EndTurn();
		ur.save(user);
		if(!gameEnd) {
			LocalDate gameDate = user.getVtGame().getGameDate();
			user.getVtGame().buildTrades(TradeDate.getLatestTradeDateBefore(gameDate));
		} else {
			user.getVtGame().buildTrades(TradeDate.getLatestTradeDateBefore(currentGameDate));
		}
		return gameEnd;
	}
	
	@Override
	public List<StockTrade> getStockTrades(String stockCode, StockTradeViewMode mode, long userId){
		User user = ur.findUserById(userId);
		switch(mode) {
			case intraDay:
				LocalDate lastTradeDay = TradeDate.getLatestTradeDateBefore(user.getVtGame().getGameDate());
				return str.getStockTradeByStockCodeByIntraDay(stockCode, lastTradeDay, userId);
			case week:
				LocalDate startDateW = user.getVtGame().getGameDate().minusDays(7);
				LocalDate endDateW = user.getVtGame().getGameDate().minusDays(1);
				return str.getStockTradeByStockCodeByDateRange(stockCode, startDateW, endDateW, userId);
			case month:
				LocalDate startDateM = user.getVtGame().getGameDate().minusDays(30);
				LocalDate endDateM = user.getVtGame().getGameDate().minusDays(1);
				return str.getStockTradeByStockCodeByDateRange(stockCode, startDateM, endDateM, userId);
			case year:
				LocalDate startDateY = user.getVtGame().getGameDate().minusYears(1);
				LocalDate endDateY = user.getVtGame().getGameDate().minusDays(1);
				return str.getStockTradeByStockCodeByDateRange(stockCode, startDateY, endDateY, userId);
			case years10:
				return str.getStockTradeByStockCodeByDateRange(stockCode, user.getVtGame().getGameDate().minusYears(10), user.getVtGame().getGameDate().minusDays(1), userId);
			case max:
				return str.getStockTradeByStockCodeMax(stockCode, userId);
			default:
				return str.getStockTradeByStockCodeByIntraDay(stockCode, user.getVtGame().getGameDate().minusDays(1), userId);
		}
	}
	
	@Override
	public MainBoard getLast2TradeDaysTrades(MainBoard mb, User user){
		
		LocalDate previousTradeDate = TradeDate.getLatestTradeDateBefore(user.getVtGame().getGameDate());
		LocalDate tradeDateBeforePreviousTradeDate = TradeDate.getTradeDateBeforePreviousTradeDate(user.getVtGame().getGameDate());
		
		List<StockTrade> trades = str.getBundledSingleDayTradeByDateByUserId(previousTradeDate, user.getId());

		if(!trades.isEmpty()) {
			mb.setTradesOnPreviousTradeDate(trades); 
		} else {
			mb.setTradesOnPreviousTradeDate(str.getBundledSingleDayTradeByDate(previousTradeDate, User.adminUser().getId())); 
		}
		
		List<StockTrade> trades2 = str.getBundledSingleDayTradeByDateByUserId(tradeDateBeforePreviousTradeDate, user.getId());

		if(!trades2.isEmpty()) {
			mb.setTradesOnTradeDateBeforePreviousTradeDate(trades2);
		} else {
			mb.setTradesOnTradeDateBeforePreviousTradeDate(str.getBundledSingleDayTradeByDate(tradeDateBeforePreviousTradeDate, User.adminUser().getId()));
		}
		
		return mb;
	}
	
	@Override
	public List<Portfolio> getPortfolios(long userId){
		User user = ur.findUserById(userId);
		return pr.getPortfolioByUserIdAndStartDateTime(userId, user.getVtGame().getGameStartDate());
	}
	
	@Override
	public List<Portfolio> getGamePortfolios(long userId, LocalDateTime gameStartDateTime){
		User user = ur.findUserById(userId);
		return pr.getPortfolioByUserIdAndStartDateTime(userId, gameStartDateTime);
	}
	
	@Override
	public MainBoard getMainBoardToday(long userId) {
		MainBoard mb = new MainBoard();
		User user = ur.findUserById(userId);
		prepMkt(user);
		mb.setLastTradePrices(user.getVtGame().getMkt());
		List<StockData> latestStockData = user.getVtGame().getLatestStockData(str, sr);
		mb.setStockData(latestStockData);
		mb = getLast2TradeDaysTrades(mb, user);
		try {
			mb.computeBoardData();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exceptions caught when computing board data");
			return null;
		}
		return mb;
	}
	
	@Override
	public void clearStockTrade(Long userId, LocalDate gameStartDate) {
		LocalDate gameFirstTradeDate = TradeDate.getLatestTradeDateBefore(gameStartDate);
		str.clearStockTradeByUserIdAndGameFirstTradeDate(userId, gameFirstTradeDate);
	}
	
}
