package sg.nus.iss.adproject.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;


public class StockTracking {

	public static LocalDate originalRealWorldDataCutoffDate = LocalDate.of(2024, 7, 26);
	
	
	
	public static String[] R1Stocks = {"JNJ", "PG", "KOF", "VZ", "WMT", "MCD", "PEP", "T", "UTG.MU", "XOM"};
	public static String[] R2Stocks = {"AAPL", "MSFT", "GOOGL", "AMZN", "META", "V", "JPM", "DIS", "NFLX", "CSCO"};
	public static String[] R3Stocks = {"TSLA", "NVDA", "AMD", "COIN", "SQ", "PLTR", "NIO", "BYND", "PLUG", "SPCE"};
	public static String[] Stocks = {"JNJ", "PG", "KOF", "VZ", "WMT", "MCD", "PEP", "T", "UTG.MU", "XOM", "AAPL", "MSFT", "GOOGL", "AMZN", "META", "V", "JPM", "DIS", "NFLX", "CSCO", "TSLA", "NVDA", "AMD", "COIN", "SQ", "PLTR", "NIO", "BYND", "PLUG", "SPCE"};
	
	public static List<Stock> AllStocks(StockRepository sr){
		return sr.findAll();
	}

}
