package sg.nus.iss.adproject.repositories.prediction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.adproject.entities.prediction.NewsArticle;

import java.util.List;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
}