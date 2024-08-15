package com.example.questionnaire.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.questionnaire.service.StockService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockService stockService;

    private final String FLASK_API_URL = "http://localhost:8082/predict_from_db";

    @GetMapping("/fetch-recommendations")
    public ResponseEntity<Map<String, Object>> fetchRecommendations(@RequestParam("userId") Long userId) {
System.out.println("okkkkkkkkkkkk");
 try {
	
            String url = String.format("%s/%d", FLASK_API_URL, userId);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            Map<String, Object> responseBody = response.getBody();
            
       //     Map<String, Object> responseBody = new HashMap<>(); 
         //   responseBody.put("risk_level", "No risk");
           // responseBody.put("recommended_stocks", stockService.getAllStocks());
            
            if (responseBody != null) {
            	System.out.println("okkkkkkkkkkkk1");
                return ResponseEntity.ok().body(responseBody);
            } else {
            	System.out.println("okkkkkkkkkkkk2");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No recommendations found"));
            }
        } catch (Exception e) {
        	System.out.println("Not OK");
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error fetching recommendations"));
        }
    }
}