package com.example.questionnaire.repository;

import com.example.questionnaire.entity.AnswerOption;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
}
