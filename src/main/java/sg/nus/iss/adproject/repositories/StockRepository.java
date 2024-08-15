package sg.nus.iss.adproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;

import sg.nus.iss.adproject.entities.simulation.Stock;

public interface StockRepository extends JpaRepository<Stock, Long>{

	@Query("SELECT s FROM Stock s WHERE s.stockCode =:stockCode")
	Stock findByStockCode(@Param("stockCode") String stockCode);
}
