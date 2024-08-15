package sg.nus.iss.adproject.repositories.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.learning.Answer;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findAnswerByQuestionId(int questionId);
    
    @Query("SELECT a.questionId FROM Answer a WHERE a.id=:answerId")
    Integer findQuestionIdByAnswerId(@Param("answerId") int answerId);

    void deleteAllByQuestionId( int questionId);
}
