package sg.nus.iss.adproject.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.GameScore;
import sg.nus.iss.adproject.entities.simulation.MainBoard;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.entities.simulation.StockData;
import sg.nus.iss.adproject.entities.simulation.StockTrade;
import sg.nus.iss.adproject.entities.simulation.StockTradeViewMode;
import sg.nus.iss.adproject.entities.simulation.TradeInstruction;
import sg.nus.iss.adproject.entities.simulation.TradeOp;
import sg.nus.iss.adproject.entities.simulation.VirtualStats;
import sg.nus.iss.adproject.entities.simulation.VirtualTradingGame;
import sg.nus.iss.adproject.services.GameScoreService;
import sg.nus.iss.adproject.services.PortfolioInterface;
import sg.nus.iss.adproject.services.PortfolioStockInterface;
import sg.nus.iss.adproject.services.TradeInstructionService;
import sg.nus.iss.adproject.services.UserService;
import sg.nus.iss.adproject.services.VirtualTradeInterface;
import sg.nus.iss.adproject.utils.ParamUtils;

@CrossOrigin
@RestController
@RequestMapping("/api/vt")
public class VirtualTradingController {

	@Autowired
	private VirtualTradeInterface vtService;
	
	@Autowired
	private UserService uService;
	
	@Autowired
	private TradeInstructionService tService;
	
	@Autowired
	private PortfolioInterface pService;
	
	
	@Autowired
	private GameScoreService gService;
	
	private WebClient webClient;

	public VirtualTradingController(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5082").build();
	}
	
	@GetMapping("/getStocks/{userId}")
	public ResponseEntity<List<Stock>> getStocks(@PathVariable("userId") long userId){
		return new ResponseEntity<>(vtService.getStocks(userId), HttpStatus.OK);
	}
	
	
	
	@GetMapping("/startVirtualTrade/{userId}/{gameDuration}")
	public ResponseEntity<Map> startVirtualTrade(@PathVariable("userId") long userId, @PathVariable int gameDuration){
		System.out.println("UserId: " + userId);
		User loggedInUser = uService.getUserByUserId(userId);
		if(loggedInUser == null) {
			loggedInUser = uService.getDefaultUser();
		} 
		Map<String,List<Map<String,String>>> data;
		try {
			vtService.startVirtualTradeEnv(loggedInUser, webClient, gameDuration);
			data = vtService.getVirtualStats(loggedInUser);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error starting virtual trade.");
			data = null;
		}
		
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@GetMapping("/viewStock1D/{userId}/{stockCode}")
	public ResponseEntity<List<Map<String, String>>> viewStock1D(@PathVariable("userId") long userId, @PathVariable("stockCode") String stockCode){
		List<StockTrade> trades = vtService.getStockTrades(stockCode, StockTradeViewMode.intraDay, userId);
		if(trades != null) {
			System.out.println("Found trade data: " + trades.size() + " records.");
			List<Map<String, String>> tradeMapList = trades.stream().map(t -> t.getStockTradeMap()).toList();
			return new ResponseEntity<>(tradeMapList, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/viewStock1W/{userId}/{stockCode}")
	public ResponseEntity<List<Map<String, String>>> viewStock1W(@PathVariable("userId") long userId, @PathVariable("stockCode") String stockCode){
		List<StockTrade> trades = vtService.getStockTrades(stockCode, StockTradeViewMode.week, userId);
		if(trades != null) {
			System.out.println("Found trade data: " + trades.size() + " records.");
			List<Map<String, String>> tradeMapList = trades.stream().map(t -> t.getStockTradeMap()).toList();
			return new ResponseEntity<>(tradeMapList, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/viewStock1M/{userId}/{stockCode}")
	public ResponseEntity<List<Map<String, String>>> viewStock1M(@PathVariable("userId") long userId, @PathVariable("stockCode") String stockCode){
		List<StockTrade> trades = vtService.getStockTrades(stockCode, StockTradeViewMode.month, userId);
		if(trades != null) {
			System.out.println("Found trade data: " + trades.size() + " records.");
			List<Map<String, String>> tradeMapList = trades.stream().map(t -> t.getStockTradeMap()).toList();
			return new ResponseEntity<>(tradeMapList, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/viewStock1Y/{userId}/{stockCode}")
	public ResponseEntity<List<Map<String, String>>> viewStock1Y(@PathVariable("userId") long userId, @PathVariable("stockCode") String stockCode){
		List<StockTrade> trades = vtService.getStockTrades(stockCode, StockTradeViewMode.year, userId);
		if(trades != null) {
			System.out.println("Found trade data: " + trades.size() + " records.");
			List<Map<String, String>> tradeMapList = trades.stream().map(t -> t.getStockTradeMap()).toList();
			return new ResponseEntity<>(tradeMapList, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/getMainBoardData/{userId}")
	public ResponseEntity<Map<String, List<Map<String, String>>>> getMainBoard(@PathVariable("userId") long userId){
		MainBoard mainBoardToday = vtService.getMainBoardToday(userId);
		if(mainBoardToday == null) {
			// attempt to build trades and recompute the main board
			System.out.println("Main board retrieved as null.");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("Main board " + mainBoardToday);
		return new ResponseEntity<>(mainBoardToday.getMainBoardMap(), HttpStatus.OK);
	}
	
	@GetMapping("/getPortfolios/{userId}")
	public ResponseEntity<List<Portfolio>> getPortfolios(@PathVariable("userId") long userId){
		List<Portfolio> portfolios = vtService.getPortfolios(userId);
		return new ResponseEntity<List<Portfolio>>(portfolios, HttpStatus.OK);
	}
	
	
	@PostMapping("/sendTrade/{userId}")
	public ResponseEntity<Void> sendTrade(@PathVariable("userId") long userId, @RequestParam Map<String, String> instr){

		String tiString = instr.toString();	
		User user = uService.getUserByUserId(userId);
		TradeInstruction ti = new TradeInstruction();
		ti.setOp(TradeOp.ofValue(Integer.parseInt(ParamUtils.extract("op", tiString))));
		ti.setPrice(Double.parseDouble(ParamUtils.extract("price", tiString)));
		ti.setQuantity(Integer.parseInt(ParamUtils.extract("quantity", tiString)));
		ti.setStock(vtService.getStockByStockCode(ParamUtils.extract("stockCode", tiString)));
		ti.setUser(user);
		ti.setGameStartDateTime(user.getVtGame().getGameStartDate());
		tService.executeTradeInstruction(ti, user);
		System.out.println("Trade OK!");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/nextGameDay/{userId}")
	public ResponseEntity<Map> nextGameDay(@PathVariable("userId") long userId){
		User user = uService.getUserByUserId(userId);
		LocalDateTime currentGameStartDateTime = user.getVtGame().getGameStartDate();
		LocalDate currentGameStartDate = user.getVtGame().getGameStartDateAsDate();
		LocalDate currentGameDate = user.getVtGame().getGameDate();
		double balanceV$ = user.getVtGame().getV$();
		
		boolean gameEnd = vtService.nextGameDay(user);
		List<StockData> latestStockData = pService.getLatestStockDataFromGameDate(user, currentGameDate);
		System.out.println("Next Day! Game ends:" + gameEnd);
		if(gameEnd) {
			System.out.println("VirtualTradeController: Game ended!");
			Double overallPortfolioValue;
			try {
				overallPortfolioValue = pService.getOverallPortfolioValue(user, currentGameStartDateTime, latestStockData);
			} catch(Exception e) {
				e.printStackTrace();
				overallPortfolioValue = 0.0;
			}
			
			Double score = overallPortfolioValue + balanceV$;
			
			GameScore gameScore = new GameScore();
			gameScore.setGameOwner(user);
			gameScore.setBalancedV$(balanceV$);
			gameScore.setPortfolioValue(overallPortfolioValue);
			gameScore.setGameStartDateTime(currentGameStartDateTime);
			gameScore.setGameEndDateTime(LocalDateTime.now());
			gameScore.setScore(score);			
			gService.saveGameScore(gameScore);
						
			vtService.clearStockTrade(user.getId(), currentGameStartDate);
			Map<String, String> scoreMap = new HashMap<>();
			scoreMap.put("game_end", "true");
			scoreMap.put("game_sign", currentGameStartDateTime.toString()+","+user.getId());
			scoreMap.put("score", score.toString());

			scoreMap = MainBoard.getStockDataMap(scoreMap, latestStockData);
			System.out.println("Sending game score ... " + score.toString());
			return new ResponseEntity<>(scoreMap, HttpStatus.OK);
		}
		return new ResponseEntity<>(vtService.getVirtualStats(user), HttpStatus.OK);
	}
	
	@PostMapping(value="/gameScore", consumes="application/json")
	public ResponseEntity<Map<String, List<Map<String,String>>>> getGameScore(@RequestBody Map gameSign){
		String gameStartDateTimeString = (String)gameSign.get("gameDateTime");
		Long userId = Long.parseLong((String)gameSign.get("userId"));
		LocalDateTime gameStartDateTime = LocalDateTime.parse(gameStartDateTimeString);
		System.out.println("Retrieving game score " + gameStartDateTime + ", " + userId);
		GameScore gs = gService.getGameScore(userId, gameStartDateTime);
		List<Portfolio> portfolios = vtService.getGamePortfolios(userId, gameStartDateTime);
		return new ResponseEntity<>(gs.getGameStatsMap(portfolios), HttpStatus.OK);
	}
} 
