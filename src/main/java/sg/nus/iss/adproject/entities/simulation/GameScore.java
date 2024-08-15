package sg.nus.iss.adproject.entities.simulation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class GameScore implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@JsonIgnore
	@ManyToOne
	private User gameOwner;
	
	@Transient
	private String username;
	
	private double score;
	
	private LocalDateTime gameStartDateTime;
	
	private LocalDateTime gameEndDateTime;
	
	private Double portfolioValue;
	
	private double balancedV$;
	
	public GameScore(long id, User gameOwner, double score, LocalDateTime gameStartDateTime,
			double balancedV$) {
		super();
		this.id = id;
		this.gameOwner = gameOwner;
		this.score = score;
		this.gameStartDateTime = gameStartDateTime;
		this.balancedV$ = balancedV$;
	}

	public GameScore() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getGameOwner() {
		return gameOwner;
	}

	public void setGameOwner(User gameOwner) {
		this.gameOwner = gameOwner;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public LocalDateTime getGameStartDateTime() {
		return gameStartDateTime;
	}

	public void setGameStartDateTime(LocalDateTime gameStartDateTime) {
		this.gameStartDateTime = gameStartDateTime;
	}

	public double getBalancedV$() {
		return balancedV$;
	}

	public void setBalancedV$(double balancedV$) {
		this.balancedV$ = balancedV$;
	}
	
	public Double getPortfolioValue() {
		return portfolioValue;
	}

	public void setPortfolioValue(Double portfolioValue) {
		this.portfolioValue = portfolioValue;
	}

	public LocalDateTime getGameEndDateTime() {
		return gameEndDateTime;
	}

	public void setGameEndDateTime(LocalDateTime gameEndDateTime) {
		this.gameEndDateTime = gameEndDateTime;
	}

	public Map<String, List<Map<String, String>>> getGameStatsMap(List<Portfolio> gamePortfolios){
		Map<String, List<Map<String, String>>> result = new HashMap<>();
		Map<String, String> currentGameScore = new HashMap<>();
		currentGameScore.put("score", String.valueOf(this.score));
		result.put("score", List.of(currentGameScore));
		Map<String, String> pfc = new HashMap<>();
		pfc.put("pfc", String.valueOf(gamePortfolios.size()));
		result.put("portfolioCount", List.of(pfc));
		result.put("portfolios", Portfolio.getPortfolioListMap(gamePortfolios));
		return result;
	}
}
