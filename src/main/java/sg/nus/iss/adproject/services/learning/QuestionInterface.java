package sg.nus.iss.adproject.services.learning;

import sg.nus.iss.adproject.entities.learning.LQuestion;

import java.util.List;

public interface QuestionInterface {

    List<LQuestion> getAllQuestion();

    void add(LQuestion question);

    LQuestion getQuestion(int questionId);
    
    void delete(int questionId);
}
