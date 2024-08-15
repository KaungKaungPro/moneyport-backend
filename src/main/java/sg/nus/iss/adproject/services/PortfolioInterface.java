package sg.nus.iss.adproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.StockData;

public interface PortfolioInterface {

	void create(Portfolio portfolio);
	
	List<Portfolio> getPortfolioByUserAndGameStartDate(long userId, LocalDateTime gameStartDate);
	
	Double getOverallPortfolioValue(User user, LocalDateTime gameStartDate, List<StockData> latestStockData);
	
	List<StockData> getLatestStockDataFromGameDate(User user, LocalDate gameDate);
}
