package sg.nus.iss.adproject.repositories.prediction;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nus.iss.adproject.entities.prediction.NewsArticle;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByTickerOrderByDateDesc(String ticker);
}