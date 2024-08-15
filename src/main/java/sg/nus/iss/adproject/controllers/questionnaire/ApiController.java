package sg.nus.iss.adproject.controllers.questionnaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RestTemplate restTemplate;


    private final String FLASK_API_URL = "http://localhost:5082/predict_from_db";

    @GetMapping("/fetch-recommendations")
    public ResponseEntity<Map<String, Object>> fetchRecommendations(@RequestParam("userId") Long userId) {

		try {
			String url = String.format("%s/%d", FLASK_API_URL, userId);
			ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

			Map<String, Object> responseBody = response.getBody();

			// Map<String, Object> responseBody = new HashMap<>();
			// responseBody.put("risk_level", "No risk");
			// responseBody.put("recommended_stocks", stockService.getAllStocks());

			if (responseBody != null) {
				return ResponseEntity.ok().body(responseBody);
			} else {

				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Collections.singletonMap("error", "No recommendations found"));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Error fetching recommendations"));
		}
	}
    
    
}
