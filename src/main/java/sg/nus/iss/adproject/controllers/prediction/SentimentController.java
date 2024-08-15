package sg.nus.iss.adproject.controllers.prediction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.prediction.SentimentResult;
import sg.nus.iss.adproject.services.prediction.SentimentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sentiment")
@CrossOrigin(origins = "http://localhost:3000")
public class SentimentController {

    private final SentimentService sentimentService;

    @Autowired
    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<List<SentimentResult>> getSentimentForSymbol(@PathVariable String symbol) {
        List<SentimentResult> sentiments = sentimentService.findByTicker(symbol);
        if (sentiments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sentiments);
    }

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody Map<String, List<String>> request) {
        List<String> headlines = request.get("headlines");
        List<String> tickers = request.get("tickers");

        if (headlines == null || tickers == null || headlines.size() != tickers.size()) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        try {
            sentimentService.analyzeAndSaveSentiment(headlines, tickers);
            return ResponseEntity.ok("Sentiment analysis completed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to analyze sentiment: " + e.getMessage());
        }
    }

}
