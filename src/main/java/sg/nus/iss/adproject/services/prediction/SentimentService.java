package sg.nus.iss.adproject.services.prediction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sg.nus.iss.adproject.repositories.prediction.SentimentResultRepository;
import sg.nus.iss.adproject.entities.prediction.SentimentResult;
import sg.nus.iss.adproject.entities.prediction.NewsArticle;

@Service
public class SentimentService {

    private static final Logger logger = LoggerFactory.getLogger(SentimentService.class);

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    private final RestTemplate restTemplate;
    private final SentimentResultRepository sentimentResultRepository;

    public SentimentService(RestTemplate restTemplate, SentimentResultRepository sentimentResultRepository) {
        this.restTemplate = restTemplate;
        this.sentimentResultRepository = sentimentResultRepository;
    }
    public SentimentResult saveSentimentResult(SentimentResult sentimentResult) {
        return sentimentResultRepository.save(sentimentResult);
    }
    public List<SentimentResult> findByTicker(String ticker) {
        logger.info("Fetching sentiment results for ticker: {}", ticker);
        List<SentimentResult> results = sentimentResultRepository.findByTicker(ticker);
        logger.info("Found {} sentiment results for ticker: {}", results.size(), ticker);
        return results;
    }


    public void analyzeAndSaveSentiment(List<String> headlines, List<String> tickers) {
        logger.info("Received headlines: {}", headlines);
        logger.info("Received tickers: {}", tickers);
        String url = flaskApiUrl + "/api/sentiment/analyze";

        Map<String, Object> requestPayload = Map.of(
            "headlines", headlines,
            "tickers", tickers
        );

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestPayload),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            logger.info("Response from Flask API: {}", response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> results = response.getBody();
                for (int i = 0; i < results.size(); i++) {
                    Map<String, Object> result = results.get(i);
                    String headline = headlines.get(i);
                    String ticker = tickers.get(i);
                    Double score = ((Number) result.get("sentimentScore")).doubleValue();
                    String sentiments = (String) result.get("sentimentLabel");

                    SentimentResult sentimentResult = new SentimentResult();
                    sentimentResult.setHeadline(headline);
                    sentimentResult.setTicker(ticker);
                    sentimentResult.setScore(score);
                    sentimentResult.setSentiments(sentiments);

                    logger.info("Saving sentiment result: {}", sentimentResult);
                    SentimentResult savedResult = sentimentResultRepository.save(sentimentResult);
                    logger.info("Saved sentiment result with ID: {}", savedResult.getId());
                }
            } else {
                logger.error("Failed to fetch sentiment results. Status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error in sentiment analysis: ", e);
        }
    }
}