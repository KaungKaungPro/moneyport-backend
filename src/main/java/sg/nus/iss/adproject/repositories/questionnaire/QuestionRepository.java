package sg.nus.iss.adproject.repositories.questionnaire;

import sg.nus.iss.adproject.entities.questionnaire.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
