package sg.nus.iss.adproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.simulation.Stock;
import sg.nus.iss.adproject.services.StockUpdateService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockUpdateController {

    @Autowired
    private StockUpdateService stockUpdateService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockUpdateService.getAllStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable long id, @RequestBody Stock stockDetails) {
        Stock updatedStock = stockUpdateService.updateStock(id, stockDetails);
        if (updatedStock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Stock> getStockById(@PathVariable long id) {
    //     Optional<Stock> stock = stockService.getStockById(id);
    //     return stock.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
    //             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    // }

    // @PostMapping
    // public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
    //     Stock savedStock = stockService.createStock(stock);
    //     return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteStock(@PathVariable long id) {
    //     stockService.deleteStock(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }
}
