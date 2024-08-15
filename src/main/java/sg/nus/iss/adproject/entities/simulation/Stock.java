package sg.nus.iss.adproject.entities.simulation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.AssetClass;


@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String stockCode;
	
	private String stockName;
	
	private String currency;
	
	private double openPrice;
	
	private String sector;
	
	private int IPOyear;
	
	private AssetClass aClass;
	
	@JsonIgnore
	@OneToMany(mappedBy = "stock", fetch=FetchType.LAZY)
	private List<PortfolioStock> associatedPortfolios;
	
	@JsonIgnore
	@OneToMany(mappedBy = "stock", fetch=FetchType.LAZY)
	private List<StockTrade> trades;
	
	@JsonIgnore
	@OneToMany(mappedBy = "stock", fetch=FetchType.LAZY)
	private List<TradeInstruction> instructions;
	
	public Stock(String stockCode, String stockName, String currency) {
		super();
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.currency = currency;
		this.openPrice = 0.0;
	}

	public Stock(String stockCode, String stockName, String currency, double openPrice) {
		super();
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.currency = currency;
		this.openPrice = openPrice;
	}
	
	public Stock() {}

	public String getStockCode() {
		return stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public String getCurrency() {
		return currency;
	}

	public double getOpenPrice() {
		return openPrice;
	}
	
	@Override
	public boolean equals(Object other) {
		return other.getClass() == this.getClass() && this.stockCode == ((Stock)other).getStockCode();
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AssetClass getaClass() {
		return aClass;
	}

	public void setaClass(AssetClass aClass) {
		this.aClass = aClass;
	}

	public List<PortfolioStock> getAssociatedPortfolios() {
		return associatedPortfolios;
	}

	public void setAssociatedPortfolios(List<PortfolioStock> associatedPortfolios) {
		this.associatedPortfolios = associatedPortfolios;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	public int getIPOyear() {
		return IPOyear;
	}

	public void setIPOyear(int iPOyear) {
		IPOyear = iPOyear;
	}
	
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
}

