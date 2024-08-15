package sg.nus.iss.adproject.repositories.prediction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.nus.iss.adproject.entities.prediction.StockDataEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockDataRepository extends JpaRepository<StockDataEntity, Long> {
    List<StockDataEntity> findBySymbolOrderByDateAsc(String symbol);
    StockDataEntity findBySymbolAndDate(String symbol, LocalDate date);
}