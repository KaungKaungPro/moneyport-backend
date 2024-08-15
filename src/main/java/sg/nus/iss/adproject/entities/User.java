package sg.nus.iss.adproject.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.simulation.MktSimParam;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.PortfolioStock;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.entities.simulation.VirtualTradingGame;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.repositories.StockTradeRepository;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name="users")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=50)
	private String username;
	
	private String password;
	
	@Column(length=50)
	private String email;
	
	@Column(length=50)
	private String firstName;
	
	@Column(length=50)
	private String lastName;
	
	@Getter
	@Transient
	private String token;

	@JsonIgnore
	@Embedded
	private VirtualTradingGame vtGame;
	
	private Role role;

	@JsonIgnore
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Portfolio> portfolios;
	
	public User(Long id, String username, String email, String firstName, String lastName) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.vtGame = new VirtualTradingGame();
	}
	
	public static User adminUser() {
		return new User(3L, "admin", "admin@mymail.com", "admin", "admin");
	}

	public User() {}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setStockHoldings(List<Portfolio> newPortfolios) {
		this.portfolios = newPortfolios;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}
	
	public List<Portfolio> getGamePortfolios(LocalDateTime gameStartDateTime){
		return portfolios
		.stream()
		.filter(p -> p.getGameStartDateTime().equals(gameStartDateTime))
		.toList();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "User (username: " +username+ ", email: " +email+", first name: " +firstName+ ", last name: " +lastName+")" ; 
	}
	
	public static boolean validateUser(User user) {
		return user.getUsername() != null && user.getPassword() != null && user.getEmail() != null;
	}

	public VirtualTradingGame getVtGame() {
		return vtGame;
	}

	public void setVtGame(VirtualTradingGame vtGame) {
		this.vtGame = vtGame;
	}
	
	public VirtualTradingGame loadVtGame(List<Stock> stocks, MktSimParam param, StockTradeRepository str, StockRepository sr) {
		return vtGame.loadMktSim(stocks, param, str, sr, this, vtGame.getGameDate());
	}


	@JsonIgnore
	public Portfolio getDefaultPortfolio(LocalDateTime gameStartDateTime) {

		for(Portfolio p : portfolios) {
			if(p.isIndex() && p.getGameStartDateTime().equals(gameStartDateTime)) {
				return p;
			}
		}
		return null;
	}
	
	public User setToken(String token) {
        this.token = token;
        return this;
    }
	
}
