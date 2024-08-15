//package com.example.questionnaire.service;
//
//import com.example.questionnaire.entity.Question;
//import com.example.questionnaire.entity.Option;
//import com.example.questionnaire.repository.QuestionRepository;
//import com.example.questionnaire.repository.OptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class QuestionService {
//    @Autowired
//    private QuestionRepository questionRepository;
//    @Autowired
//    private OptionRepository optionRepository;
//
//    public List<Question> getAllQuestions() {
//        return questionRepository.findAll();
//    }
//
//    public List<Option> getOptionsByQuestionId(Long questionId) {
//        return optionRepository.findByQuestionId(questionId);
//    }
//}
