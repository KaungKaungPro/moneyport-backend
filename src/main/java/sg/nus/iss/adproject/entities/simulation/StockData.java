package sg.nus.iss.adproject.entities.simulation;

public class StockData {
	
	private Stock stock;
	
	private double open;
	
	private double close;
	
	private double prevClose;
	
	private double high;
	
	private double low;
	
	private long volume;
	
	private double gain;
	
	public StockData() {}

	public Stock getStock() {
		return stock;
	}
	
	public String getStockCode() {
		return stock.getStockCode();
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getPrevClose() {
		return prevClose;
	}

	public void setPrevClose(double prevClose) {
		this.prevClose = prevClose;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}
	
	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	@Override
	public String toString() {
		return "Stock " + stock.getStockName() + ", volume: " + volume + ", open: " + open + ", close: " + close + ", high: " + high + ", low: " + low;
	}

}
