package sg.nus.iss.adproject.repositories.questionnaire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.adproject.entities.questionnaire.AnswerOption;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
}
