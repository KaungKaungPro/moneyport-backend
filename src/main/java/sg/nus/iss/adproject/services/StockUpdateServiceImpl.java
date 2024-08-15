package sg.nus.iss.adproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.repositories.StockRepository;
import sg.nus.iss.adproject.services.StockUpdateService;

import java.util.List;
import java.util.Optional;

@Service
public class StockUpdateServiceImpl implements StockUpdateService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    @Override
    public Stock updateStock(long id, Stock stockDetails) {
        Optional<Stock> stockOptional = stockRepository.findById(id);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            stock.setStockCode(stockDetails.getStockCode());
            stock.setStockName(stockDetails.getStockName());
            stock.setCurrency(stockDetails.getCurrency());
            stock.setOpenPrice(stockDetails.getOpenPrice());
            stock.setSector(stockDetails.getSector());
            stock.setIPOyear(stockDetails.getIPOyear());
            stock.setaClass(stockDetails.getaClass());

            return stockRepository.save(stock);
        } else {
            return null;
        }
    }

//    @Override
//    public Optional<Stock> getStockById(long id) {
//        return stockRepository.findById(id);
//    }
//
//    @Override
//    public Stock createStock(Stock stock) {
//        return stockRepository.save(stock);
//    }
//
//    @Override
//    public void deleteStock(long id) {
//        if (stockRepository.existsById(id)) {
//            stockRepository.deleteById(id);
//        }
//    }
}
