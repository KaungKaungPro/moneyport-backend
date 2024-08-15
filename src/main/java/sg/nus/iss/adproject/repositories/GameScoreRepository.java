package sg.nus.iss.adproject.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.simulation.GameScore;

public interface GameScoreRepository extends JpaRepository<GameScore, Long>{

	@Query("SELECT gs FROM GameScore gs WHERE gs.gameOwner.id =:userId AND gs.gameStartDateTime =:gameStartDateTime")
	GameScore getGameScoreByUserIdAndGameStartDateTime(@Param("userId") Long userId, @Param("gameStartDateTime") LocalDateTime gameStartDateTime);
	
	
}
