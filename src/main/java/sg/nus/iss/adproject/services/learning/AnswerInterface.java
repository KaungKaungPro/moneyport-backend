package sg.nus.iss.adproject.services.learning;

import sg.nus.iss.adproject.entities.learning.Answer;

import java.util.List;

public interface AnswerInterface {

    List<Answer> getAllAnswer(int questionId);

    void add(Answer answer);

    void editUpVote(int answerId, int userId);
    
    void delete(int answerId);
    
    Integer getQuestionIdByAnswerId(int answerId);

    void deleteAllByQuestionId(int questionId);
}
