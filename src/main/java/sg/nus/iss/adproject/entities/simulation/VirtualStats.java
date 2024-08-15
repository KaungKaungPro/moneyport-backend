package sg.nus.iss.adproject.entities.simulation;

import java.util.List;

import sg.nus.iss.adproject.entities.User;

public class VirtualStats {
	
	private int gameDay;
	
	private List<Portfolio> portfolios;
	
	private double v$;
	
	private User user;
	
	public VirtualStats() {}

	public VirtualStats(int gameDay, List<Portfolio> portfolios, double v$, User user) {
		super();
		this.gameDay = gameDay;
		this.portfolios = portfolios;
		this.v$ = v$;
		this.user = user;
	}

	public int getGameDay() {
		return gameDay;
	}

	public void setGameDay(int gameDay) {
		this.gameDay = gameDay;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public double getV$() {
		return v$;
	}

	public void setV$(double v$) {
		this.v$ = v$;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
