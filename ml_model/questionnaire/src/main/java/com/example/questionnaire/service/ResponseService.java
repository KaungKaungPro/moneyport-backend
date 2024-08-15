package com.example.questionnaire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.questionnaire.dto.ResponseDto;
import com.example.questionnaire.dto.UserResponsesDto;
import com.example.questionnaire.entity.Response;
import com.example.questionnaire.repository.ResponseRepository;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    public void saveResponses(UserResponsesDto userResponses) {
        // Iterate over responses and save to the database
        for (ResponseDto response : userResponses.getResponses()) {
            Response responseEntity = new Response();
            responseEntity.setUserId((long) userResponses.getUserId());
            responseEntity.setQuestionId(response.getQuestionId());
            responseEntity.setResponseValue(response.getResponseValue());

            responseRepository.save(responseEntity);
        }
        
        
    }
    
   
}
