package sg.nus.iss.adproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.adproject.entities.simulation.PortfolioStock;

public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Integer>{

	
	PortfolioStock findPortfolioStockById(int id);
}
