package sg.nus.iss.adproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.iss.adproject.entities.simulation.StockRecommendation;
import sg.nus.iss.adproject.repositories.StockRecommendationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockRecommendationServiceImpl implements StockRecommendationService {

    @Autowired
    private StockRecommendationRepository stockRecommendationRepository;

    @Override
    public StockRecommendation createRecommendation(StockRecommendation newRecommendation) {
        return stockRecommendationRepository.save(newRecommendation);
    }

    @Override
    public StockRecommendation updateRecommendation(StockRecommendation existing, StockRecommendation newRecommendation) {
        existing.setRecommendationDate(newRecommendation.getRecommendationDate());
    	
        existing.setA1Stock1(newRecommendation.getA1Stock1());
        existing.setA1Stock2(newRecommendation.getA1Stock2());
        existing.setA1Stock3(newRecommendation.getA1Stock3());
        
        existing.setA2Stock1(newRecommendation.getA2Stock1());
        existing.setA2Stock2(newRecommendation.getA2Stock2());
        existing.setA2Stock3(newRecommendation.getA2Stock3());
        
        existing.setA3Stock1(newRecommendation.getA3Stock1());
        existing.setA3Stock2(newRecommendation.getA3Stock2());
        existing.setA3Stock3(newRecommendation.getA3Stock3());
        
        return stockRecommendationRepository.save(existing);
    }

    @Override
    public Optional<StockRecommendation> findRecommendationByDate(LocalDate recommendationDate) {
        return stockRecommendationRepository.findByRecommendationDate(recommendationDate);
    }
    @Override
    public List<StockRecommendation> findAll() {
        return stockRecommendationRepository.findAll();
    }

    @Override
    public Optional<StockRecommendation> findById(Long id) {
        return stockRecommendationRepository.findById(id);
    }

    @Override
    public void deleteRecommendation(Long id) {
        stockRecommendationRepository.deleteById(id);
    }
    
}