package sg.nus.iss.adproject.repositories.prediction;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.adproject.entities.prediction.SentimentResult;
import java.util.List;

public interface SentimentResultRepository extends JpaRepository<SentimentResult, Long> {
    List<SentimentResult> findByTicker(String ticker);
}