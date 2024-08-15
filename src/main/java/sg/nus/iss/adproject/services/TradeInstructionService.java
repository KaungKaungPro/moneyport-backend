package sg.nus.iss.adproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.simulation.Portfolio;
import sg.nus.iss.adproject.entities.simulation.PortfolioStock;
import sg.nus.iss.adproject.entities.simulation.TradeInstruction;
import sg.nus.iss.adproject.entities.simulation.TradeOp;
import sg.nus.iss.adproject.repositories.PortfolioRepository;
import sg.nus.iss.adproject.repositories.PortfolioStockRepository;
import sg.nus.iss.adproject.repositories.TradeInstructionRepository;
import sg.nus.iss.adproject.repositories.UserRepository;

@Service
public class TradeInstructionService implements TradeInstructionInterface {
	
	@Autowired
	TradeInstructionRepository tir;
	
	@Autowired
	private PortfolioStockRepository psr;
	
	@Autowired
	private PortfolioRepository pr;
	
	@Autowired
	private UserRepository ur;
	
	@Override
	public void saveTradeInstruction(TradeInstruction ti) {
		tir.save(ti);
	}
	
	@Override
	public void executeTradeInstruction(TradeInstruction ti, User user) {
		List<Portfolio> userPortfolios = pr.getPortfolioByUserIdAndStartDateTime(user.getId(), user.getVtGame().getGameStartDate());
		if(ti.getOp().equals(TradeOp.Buy)) {
			Portfolio dftPortfolio = user.getDefaultPortfolio(user.getVtGame().getGameStartDate());
			for(PortfolioStock ps : dftPortfolio.getPortfolioStocks()) {
				if(ps.getStock().getStockCode().equals(ti.getStock().getStockCode())) {
					ps.setQuantity(ps.getQuantity() + ti.getQuantity());
					ps.setLastDateTraded(user.getVtGame().getGameDate());
					ps.setPurchasedPrice(Float.parseFloat("" + ti.getPrice()));
					psr.save(ps);
					double value = ti.getQuantity() * ti.getPrice();
					user.getVtGame().minusV$(value);
					ur.save(user);
					saveTradeInstruction(ti);
					return;
				}
			}
			PortfolioStock ps = new PortfolioStock(
					ti.getQuantity(), 
					Float.parseFloat("" + ti.getPrice()), 
					user.getDefaultPortfolio(user.getVtGame().getGameStartDate()),
					ti.getStock(), 
					user.getVtGame().getGameDate()
					);
			psr.save(ps);
			double value = ti.getQuantity() * ti.getPrice();
			user.getVtGame().minusV$(value);
			ur.save(user);
			saveTradeInstruction(ti);
			return;
		} else if (ti.getOp().equals(TradeOp.Sell)) {
			for(Portfolio p : userPortfolios) {
				for(PortfolioStock ps : p.getPortfolioStocks()) {
					if(ps.getStock().getStockCode().equals(ti.getStock().getStockCode())) {
						ps.setQuantity(ps.getQuantity() - ti.getQuantity());
						ps.setLastDateTraded(user.getVtGame().getGameDate());
						ps.setPurchasedPrice(Float.parseFloat("" + ti.getPrice()));
						psr.save(ps);
						double value = ti.getQuantity() * ti.getPrice();
						user.getVtGame().addV$(value);
						ur.save(user);
						
						saveTradeInstruction(ti);
						return;
					}
				}
			}
		}
		
	}
}
