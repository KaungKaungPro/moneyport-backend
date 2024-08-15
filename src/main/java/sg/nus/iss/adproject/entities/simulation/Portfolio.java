package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class Portfolio {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String portfolioName;
	
	private Boolean first = false;
	
	@ManyToOne
	private User user;
	
	private LocalDateTime gameStartDateTime;
	
	@OneToMany(mappedBy="portfolio", fetch=FetchType.EAGER)
	private List<PortfolioStock> portfolioStocks;
	
	public Portfolio() {
		
	}

	public Portfolio(long id, User user, List<PortfolioStock> portfolioStocks, Boolean isIndex) {
		super();
		this.id = id;
		this.user = user;
		this.portfolioStocks = portfolioStocks;
		this.first = isIndex;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<PortfolioStock> getPortfolioStocks() {
		return portfolioStocks;
	}

	public void setPortfolioStocks(List<PortfolioStock> portfolioStocks) {
		this.portfolioStocks = portfolioStocks;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	
	
	
	public LocalDateTime getGameStartDateTime() {
		return gameStartDateTime;
	}

	public void setGameStartDateTime(LocalDateTime gameStartDateTime) {
		this.gameStartDateTime = gameStartDateTime;
	}

	public Boolean isIndex() {
		return first;
	}

	public void setIndex(Boolean index) {
		this.first = index;
	}

	public Double getPortfolioValue(List<StockData> stockData) {
		return portfolioStocks
		.stream()
		.map(ps -> ps.getCurrentValue(stockData))
		.reduce(0.0, (v1, v2) -> v1 + v2);
	}

	public LocalDate getLastTradeDate() {
		return portfolioStocks
				.stream()
				.map(ps -> ps.getLastDateTraded())
				.max((d1, d2) -> d1.isAfter(d2) ? 1 : -1)
				.get();
	}
	
	public PortfolioStock isInPortfolio(String stockCode) {
		for(PortfolioStock ps : portfolioStocks) {
			if(ps.getStock().getStockCode().equals(stockCode)) {
				return ps;
			}
		}
		return null;
	}
	
	public static List<Map<String, String>> getPortfolioListMap(List<Portfolio> userPortfolios){
		List<Map<String, String>> portfolioListMap = new ArrayList<>();
		
		for(Portfolio p : userPortfolios) {
			Map<String, String> portfolioMap = new HashMap<>();
			portfolioMap.put("name", p.getPortfolioName());
			portfolioMap.put("portfolioId", String.valueOf(p.getId()));
			List<PortfolioStock> pStockList = p.getPortfolioStocks();
			int count = 0;
			portfolioMap.put("stockCount", String.valueOf(pStockList.size()));
			for(PortfolioStock ps : pStockList) {
				portfolioMap.put("ps"+count+"StockCode", ps.getStock().getStockCode());
				portfolioMap.put("ps"+count+"StockName", ps.getStock().getStockName());
				portfolioMap.put("ps"+count+"Quantity", String.valueOf(ps.getQuantity()));
				portfolioMap.put("ps"+count+"PurchasedPrice", String.valueOf(ps.getPurchasedPrice()));
				portfolioMap.put("ps"+count+"LastTradeDate", String.valueOf(ps.getLastDateTraded()));
				count++;
			}
			portfolioListMap.add(portfolioMap);
		}
		return portfolioListMap;
	}
}
