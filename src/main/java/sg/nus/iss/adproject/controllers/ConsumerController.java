package sg.nus.iss.adproject.controllers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sg.nus.iss.adproject.entities.simulation.GameScore;
import sg.nus.iss.adproject.services.GameScoreInterface;

@CrossOrigin
@RestController
@RequestMapping("/api/pp")
public class ConsumerController {
	
	private WebClient webClient;
	
	@Autowired
	private GameScoreInterface gsService;

	public ConsumerController(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5052").build();
	}
	
	public void consumeYFStockInfo(String symbol) {
//		Mono<Object> response = webClient.get()
//				.uri("/api/get_ticker_info/" + symbol)
//				.retrieve()
//				.bodyToMono(Object.class);
//		System.out.println("Retrieved response from /api/get_ticker_info/" + symbol);
//		response.subscribe(
//				value -> {
//					Map stockData = (HashMap<String, String>) value;
//					System.out.println(stockData);
//				},
//				error -> {},
//				() -> {System.out.println("Completed");}
//				);		
//		System.out.println(subscription); 
	}
	
	@GetMapping("/getScoreboard")
	public ResponseEntity<Page<GameScore>> getScoreboard(@RequestParam int page, @RequestParam int size){
		
		Page<GameScore> gs = gsService.getAllGameScores(page, size);
		gs.forEach(s -> s.setUsername(s.getGameOwner().getUsername()));
		return new ResponseEntity<>(gs, HttpStatus.OK);
	}

}
