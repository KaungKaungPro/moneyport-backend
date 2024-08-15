package com.example.questionnaire.dto;

import java.util.List;

public class UserResponsesDto {
    private int userId;
    private List<ResponseDto> responses;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<ResponseDto> getResponses() {
		return responses;
	}
	public void setResponses(List<ResponseDto> responses) {
		this.responses = responses;
	}

    // Getters and Setters
    
}