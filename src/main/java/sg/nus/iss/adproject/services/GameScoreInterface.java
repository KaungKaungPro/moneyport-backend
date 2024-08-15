package sg.nus.iss.adproject.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import sg.nus.iss.adproject.entities.simulation.GameScore;

public interface GameScoreInterface {
	
	void saveGameScore(GameScore gameScore);

	GameScore getGameScore(Long userId, LocalDateTime gameStartDateTime);
	
	Page<GameScore> getAllGameScores(int page, int size);
}
