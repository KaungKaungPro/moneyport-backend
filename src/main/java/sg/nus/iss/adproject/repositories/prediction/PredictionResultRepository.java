package sg.nus.iss.adproject.repositories.prediction;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.adproject.entities.prediction.PredictionResultEntity;

import java.time.LocalDate;

public interface PredictionResultRepository extends JpaRepository<PredictionResultEntity, Long> {
    PredictionResultEntity findBySymbolAndPredictionDate(String symbol, LocalDate predictionDate);
}