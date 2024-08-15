package sg.nus.iss.adproject.entities.simulation;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class PortfolioStock {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private long quantity;
	
	private float purchasedPrice;
	
	private LocalDate lastDateTraded;
	
	@ManyToOne
	private Portfolio portfolio;
	
	@ManyToOne
	private Stock stock;
	
	public PortfolioStock(long quantity, float purchasedPrice, Portfolio portfolio, Stock stock, LocalDate lastDateTraded) {
		super();
		this.quantity = quantity;
		this.purchasedPrice = purchasedPrice;
		this.portfolio = portfolio;
		this.stock = stock;
		this.lastDateTraded = lastDateTraded;
	}

	public PortfolioStock() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public float getPurchasedPrice() {
		return purchasedPrice;
	}

	public void setPurchasedPrice(float purchasedPrice) {
		this.purchasedPrice = purchasedPrice;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public LocalDate getLastDateTraded() {
		return lastDateTraded;
	}

	public void setLastDateTraded(LocalDate lastDateTraded) {
		this.lastDateTraded = lastDateTraded;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Double getValue() {
		return (double) quantity * purchasedPrice;
	}
	
	public Double getCurrentValue(List<StockData> currentStockData) {
		Double currentPrice = currentStockData
				.stream()
				.filter(sd -> sd.getStockCode().equals(this.getStock().getStockCode()))
				.map(sd -> sd.getClose())
				.findFirst()
				.orElse(0.0);
		return (double) quantity * currentPrice;
	}
}
