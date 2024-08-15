package sg.nus.iss.adproject.repositories.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.learning.LQuestion;

@Repository
public interface LQuestionRepository extends JpaRepository<LQuestion, Integer> {

}
