//package com.example.questionnaire.service;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.example.questionnaire.entity.NewsArticle;
//
//@Service
//public class FinnhubNewsService {
//
//    @Value("${finnhub.api.key}")
//    private String apiKey;
//
//    private final RestTemplate restTemplate;
//
//    public FinnhubNewsService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<NewsArticle> getCompanyNews(List<String> symbols) {
//        String symbolsQuery = String.join(",", symbols);
//        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
//        String url = String.format(
//            "https://finnhub.io/api/v1/company-news?symbol=%s&from=%s&to=%s&token=%s",
//            symbolsQuery, today, today, apiKey
//        );
//
//        ResponseEntity<NewsArticle[]> response = restTemplate.getForEntity(url, NewsArticle[].class);
//        NewsArticle[] newsArticles = response.getBody();
//        if (newsArticles != null) {
//            return filterNewsBySymbols(newsArticles, symbols);
//        }
//        return List.of();
//    }
//
//    private List<NewsArticle> filterNewsBySymbols(NewsArticle[] newsArticles, List<String> symbols) {
//        return List.of(newsArticles).stream()
//                .filter(article -> symbols.stream().anyMatch(symbol -> article.getHeadline().contains(symbol)))
//                .collect(Collectors.toList());
//    }
//}