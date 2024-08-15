package com.example.questionnaire.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "response_value")
    private Integer responseValue;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(long i) { this.questionId = i; }

    public Long getUserId() { return userId; }
    public void setUserId(Long i) { this.userId = i; }

    public Integer getResponseValue() { return responseValue; }
    public void setResponseValue(Integer responseValue) { this.responseValue = responseValue; }
}
