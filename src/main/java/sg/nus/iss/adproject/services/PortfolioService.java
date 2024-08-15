package sg.nus.iss.adproject.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.StockData;
import sg.nus.iss.adproject.repositories.MktSimParamRepository;
import sg.nus.iss.adproject.repositories.PortfolioRepository;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;

@Service
@Transactional
public class PortfolioService implements PortfolioInterface{
	
	@Autowired
	private StockRepository sr;
	
	@Autowired
	private StockTradeRepository str;
	
	
	@Autowired
	private PortfolioRepository pr;
	
	@Override
	public void create(Portfolio portfolio) {
		pr.save(portfolio);
	}
	
	@Override
	public List<Portfolio> getPortfolioByUserAndGameStartDate(long userId, LocalDateTime gameStartDateTime){
		return pr.getPortfolioByUserIdAndStartDateTime(userId, gameStartDateTime);
	}
	
	@Override
	public List<StockData> getLatestStockDataFromGameDate(User user, LocalDate gameDate){
		return user.getVtGame().getLatestStockDataFromGameDate(str, sr, gameDate);
	}
	
	@Override
	public Double getOverallPortfolioValue(User user, LocalDateTime gameStartDateTime, List<StockData> latestStockData) {
		
		List<Portfolio> userPortfolios = pr.getPortfolioByUserIdAndStartDateTime(user.getId(), gameStartDateTime);
		if(!userPortfolios.isEmpty()) {
			return userPortfolios
					.stream()
					.map(p -> p.getPortfolioValue(latestStockData))
					.reduce(0.0, (pv1, pv2) -> pv1 + pv2);
		} else {
			return 0.0;
		}
		
	}
	
}
