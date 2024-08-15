package sg.nus.iss.adproject.services;

import sg.nus.iss.adproject.entities.simulation.StockRecommendation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRecommendationService {

    public abstract StockRecommendation createRecommendation(StockRecommendation newRecommendation);

    public abstract StockRecommendation updateRecommendation(StockRecommendation existing, StockRecommendation newRecommendation);

    public abstract Optional<StockRecommendation> findRecommendationByDate(LocalDate recommendationDate);

	public abstract List<StockRecommendation> findAll();

	public abstract Optional<StockRecommendation> findById(Long id);

	public abstract void deleteRecommendation(Long id);
}

