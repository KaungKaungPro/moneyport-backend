package sg.nus.iss.adproject.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.nus.iss.adproject.entities.simulation.GameScore;
import sg.nus.iss.adproject.repositories.GameScoreRepository;

@Service
@Transactional
public class GameScoreService implements GameScoreInterface{

	@Autowired
	private GameScoreRepository gr;
	
	@Override
	public void saveGameScore(GameScore gameScore) {
		gr.save(gameScore);
	}

	@Override
	public GameScore getGameScore(Long userId, LocalDateTime gameStartDateTime) {
		return gr.getGameScoreByUserIdAndGameStartDateTime(userId, gameStartDateTime);
	}
	
	@Override
	public Page<GameScore> getAllGameScores(int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("score").descending());
		return gr.findAll(pageable);
	}
}
