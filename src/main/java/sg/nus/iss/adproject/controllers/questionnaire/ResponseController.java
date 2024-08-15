package sg.nus.iss.adproject.controllers.questionnaire;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import sg.nus.iss.adproject.entities.questionnaire.Response;
import sg.nus.iss.adproject.entities.questionnaire.ResponseRequest;
import sg.nus.iss.adproject.repositories.questionnaire.ResponseRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    @Autowired
    private ResponseRepository responseRepository;
    
    private WebClient webClient;

	public ResponseController(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5082").build();
	}

    @PostMapping("/")
    public ResponseEntity<?> createResponse(@RequestBody ResponseRequest request) {
        try {
            if (request.getResponses() == null || request.getResponses().isEmpty()) {
                return new ResponseEntity<>("Responses cannot be null or empty", HttpStatus.BAD_REQUEST);
            }
            
         // Delete existing responses for the user
            responseRepository.deleteByUserId(request.getUserId());

            for (ResponseRequest.ResponseDto responseDto : request.getResponses()) {
                Response response = new Response();
                response.setUserId(request.getUserId());
                response.setQuestionId(responseDto.getQuestionId());
                response.setResponseValue(responseDto.getResponseValue());
                responseRepository.save(response);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>("Error saving response: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }
    
    @GetMapping("/get_risk_level/{uid}")
    public ResponseEntity<Void> getRiskLevel(@PathVariable("uid") Long uid){
    	webClient
    	.get()
    	.uri(uriBuilder -> {
    		return uriBuilder
    				.path("/api/predict_from_db/" + uid)
    				.build();
    		
    	}).retrieve().bodyToMono(Object.class).subscribe(value -> {}, error -> {}, () -> {System.out.println("Completed.");});
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}