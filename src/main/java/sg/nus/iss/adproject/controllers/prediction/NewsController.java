package sg.nus.iss.adproject.controllers.prediction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.prediction.NewsArticle;
import sg.nus.iss.adproject.services.prediction.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:3000")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/fetch-news")
    public ResponseEntity<List<NewsArticle>> fetchNews() {
        List<NewsArticle> newsArticles = newsService.getCompanyNews();
        if (newsArticles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(newsArticles);
    }
}