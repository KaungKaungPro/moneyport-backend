package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class TradeInstruction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private TradeOp op;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private User tradeEnvOwner;
	
	@NonNull
	private LocalDateTime gameStartDateTime;
	
	private int quantity;
	
	private double price;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Stock stock;
	
	public TradeInstruction() {}
	
	public TradeInstruction(TradeOp op, User user, int quantity, double price, Stock stock, LocalDateTime gameStartDateTime) {
		super();
		this.op = op;
		this.tradeEnvOwner = user;
		this.quantity = quantity;
		this.price = price;
		this.stock = stock;
		this.gameStartDateTime = gameStartDateTime;
	}

	public TradeOp getOp() {
		return op;
	}

	public void setOp(TradeOp op) {
		this.op = op;
	}

	public User getUser() {
		return tradeEnvOwner;
	}

	public void setUser(User user) {
		this.tradeEnvOwner = user;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getTradeEnvOwner() {
		return tradeEnvOwner;
	}

	public void setTradeEnvOwner(User tradeEnvOwner) {
		this.tradeEnvOwner = tradeEnvOwner;
	}

	public LocalDateTime getGameStartDateTime() {
		return gameStartDateTime;
	}

	public void setGameStartDateTime(LocalDateTime gameStartDateTime) {
		this.gameStartDateTime = gameStartDateTime;
	}
	
	
}
