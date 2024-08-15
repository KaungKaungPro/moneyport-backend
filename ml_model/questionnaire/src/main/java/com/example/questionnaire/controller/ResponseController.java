package com.example.questionnaire.controller;

import com.example.questionnaire.dto.ResponseRequest;
import com.example.questionnaire.entity.Response;
import com.example.questionnaire.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    @Autowired
    private ResponseRepository responseRepository;

    @PostMapping
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

    @GetMapping
    public List<Response> getAllResponses() {
        return responseRepository.findAll();
    }
}
