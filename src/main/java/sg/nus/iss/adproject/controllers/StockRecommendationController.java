package sg.nus.iss.adproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.simulation.StockRecommendation;
import sg.nus.iss.adproject.services.StockRecommendationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
public class StockRecommendationController {

    @Autowired
    private StockRecommendationService stockRecommendationService;

    @GetMapping
    public ResponseEntity<List<StockRecommendation>> getAllRecommendations() {
        List<StockRecommendation> recommendations = stockRecommendationService.findAll();
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<StockRecommendation> getRecommendationByDate(@PathVariable("date") String date) {
        LocalDate recommendationDate = LocalDate.parse(date);
        Optional<StockRecommendation> recommendation = stockRecommendationService.findRecommendationByDate(recommendationDate);
        return recommendation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StockRecommendation> createRecommendation(@RequestBody StockRecommendation newRecommendation) {
        StockRecommendation savedRecommendation = stockRecommendationService.createRecommendation(newRecommendation);
        return new ResponseEntity<>(savedRecommendation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockRecommendation> updateRecommendation(
            @PathVariable("id") Long id, @RequestBody StockRecommendation updatedRecommendation) {
        Optional<StockRecommendation> existingRecommendation = stockRecommendationService.findById(id);
        if (existingRecommendation.isPresent()) {
            StockRecommendation updated = stockRecommendationService.updateRecommendation(existingRecommendation.get(), updatedRecommendation);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRecommendation(@PathVariable("id") Long id) {
//        Optional<StockRecommendation> existingRecommendation = stockRecommendationService.findById(id);
//        if (existingRecommendation.isPresent()) {
//            stockRecommendationService.deleteRecommendation(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

}

