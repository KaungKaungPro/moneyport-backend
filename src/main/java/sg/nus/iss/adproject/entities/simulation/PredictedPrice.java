package sg.nus.iss.adproject.entities.simulation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class PredictedPrice {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private User gamePlayer;
	
	private LocalDateTime gameStartDateTime;
	
	private LocalDate gameDate;
	
	@OneToOne
	private Stock stock;
	
	private Double price;
	
	public PredictedPrice() {}

	public PredictedPrice(User gamePlayer, LocalDateTime gameStartDateTime, LocalDate gameDate,
			Stock stock, Double price) {
		super();
		this.gamePlayer = gamePlayer;
		this.gameStartDateTime = gameStartDateTime;
		this.gameDate = gameDate;
		this.stock = stock;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(User gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public LocalDateTime getGameStartDateTime() {
		return gameStartDateTime;
	}

	public void setGameStartDateTime(LocalDateTime gameStartDateTime) {
		this.gameStartDateTime = gameStartDateTime;
	}

	public LocalDate getGameDate() {
		return gameDate;
	}

	public void setGameDate(LocalDate gameDate) {
		this.gameDate = gameDate;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public static void getPredictedPrice(WebClient wc, User user, List<StockTrade> closeTradeData) {}
}
