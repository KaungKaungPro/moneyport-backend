package sg.nus.iss.adproject.services.prediction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import jakarta.transaction.Transactional; // Correct import for Jakarta EE 9
import sg.nus.iss.adproject.dto.prediction.PredictionResult;
import sg.nus.iss.adproject.entities.prediction.PredictionResultEntity;
import sg.nus.iss.adproject.repositories.prediction.PredictionResultRepository;

import java.time.LocalDate;
import java.util.Map;

@Service
public class PredictionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PredictionResultRepository predictionResultRepository;

    private final String flaskApiUrl = "http://localhost:5082/predict";

    @Transactional
    public PredictionResult predict(String symbol, String date, double userPrediction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestMap = Map.of(
            "symbol", symbol,
            "date", date,
            "userPrediction", userPrediction
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestMap, headers);

        try {
            ResponseEntity<PredictionResult> response = restTemplate.postForEntity(flaskApiUrl, request, PredictionResult.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                PredictionResult result = response.getBody();

                if (result != null) {
                    // Save the prediction result to the database
                    PredictionResultEntity entity = new PredictionResultEntity();
                    entity.setSymbol(symbol);
                    entity.setPredictionDate(LocalDate.parse(date));
                    entity.setUserPrediction(userPrediction);
                    entity.setModelPredictions(result.getModelPredictions()); // Set the entire list
                    entity.setScore(result.getScore());

                    predictionResultRepository.save(entity);
                    System.out.println("Saved prediction result: " + entity);
                    return result;
                } else {
                    throw new RuntimeException("Received null response body");
                }
            } else {
                throw new RuntimeException("Received non-OK response: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("HTTP error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error predicting: " + e.getMessage(), e);
        }
    }
}
