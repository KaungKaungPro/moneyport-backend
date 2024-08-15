package sg.nus.iss.adproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.adproject.entities.simulation.StockRecommendation;

import java.time.LocalDate;
import java.util.Optional;

public interface StockRecommendationRepository extends JpaRepository<StockRecommendation, Long> {
    Optional<StockRecommendation> findByRecommendationDate(LocalDate recommendationDate);
}

