package sg.nus.iss.adproject.services;

import sg.nus.iss.adproject.entities.simulation.Stock;
import java.util.List;
import java.util.Optional;

public interface StockUpdateService {
	
    List<Stock> getAllStocks();
    
    Stock updateStock(long id, Stock stockDetails);

//    Optional<Stock> getStockById(long id);
//    Stock createStock(Stock stock);
//    void deleteStock(long id);
}
