package com.example.questionnaire.controller;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.AnswerOption;
import com.example.questionnaire.repository.QuestionRepository;
import com.example.questionnaire.repository.AnswerOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/questions/{questionId}/options")
    public List<AnswerOption> getOptionsByQuestionId(@PathVariable Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }
}
