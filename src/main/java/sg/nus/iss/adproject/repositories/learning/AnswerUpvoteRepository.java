package sg.nus.iss.adproject.repositories.learning;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.learning.AnswerUpvote;

public interface AnswerUpvoteRepository extends JpaRepository<AnswerUpvote, Long>{

	@Query("SELECT au FROM AnswerUpvote au JOIN au.voters v WHERE au.answer.id =:answerId AND v.id =:userId")
	Optional<AnswerUpvote> findByAnswerAndUser(@Param("answerId") int answerId, @Param("userId") int userId);

	@Query("SELECT COUNT(au) FROM AnswerUpvote au WHERE au.answer.id =:answerId")
	Integer findTotalVotes(@Param("answerId") int answerId);
}
