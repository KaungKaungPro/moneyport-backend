package sg.nus.iss.adproject.services.prediction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.cache.annotation.Cacheable;
import sg.nus.iss.adproject.entities.prediction.NewsArticle;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Value("${finnhub.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public NewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final List<String> STOCK_NAMES = List.of(
            "Johnson & Johnson", "Procter & Gamble", "Coca-Cola", "Verizon", "Walmart",
            "McDonald's", "PepsiCo", "AT&T", "Unilever", "ExxonMobil",
            "Apple Inc.", "Microsoft", "Alphabet (Google)", "Amazon", "Facebook",
            "Visa", "JPMorgan Chase", "Disney", "Netflix", "Cisco Systems",
            "Tesla", "NVIDIA", "Advanced Micro Devices", "Coinbase", "Square",
            "Palantir Technologies", "NIO Inc.", "Beyond Meat", "Plug Power", "Virgin Galactic"
        );
    public double getStockPriceChange(String symbol) {
        // Implement this method to fetch the actual stock price change
        // This could involve calling an external API or querying your database
        // For now, let's return a dummy value
        return 1.5; // Placeholder: 1.5% change
    }
    
    public List<NewsArticle> getCompanyNews() {
        String today = LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, List<NewsArticle>> newsByCompany = new HashMap<>();

        for (String companyName : STOCK_NAMES) {
            String ticker = getTickerForCompany(companyName);
            if (ticker.isEmpty()) {
                logger.warn("No ticker found for company: {}", companyName);
                continue;
            }

            String url = String.format(
                "https://finnhub.io/api/v1/company-news?symbol=%s&from=%s&to=%s&token=%s",
                ticker, today, today, apiKey
            );
            logger.info("Fetching news for: {}", companyName);

            try {
                ResponseEntity<Map[]> response = restTemplate.getForEntity(url, Map[].class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    Map[] newsArticles = response.getBody();
                    if (newsArticles != null) {
                        List<NewsArticle> companyNews = Arrays.stream(newsArticles)
                            .map(articleData -> {
                                NewsArticle article = new NewsArticle();
                                article.setHeadline((String) articleData.get("headline"));
                                article.setSummary((String) articleData.get("summary"));
                                article.setUrl((String) articleData.get("url"));

                                // Parse the datetime attribute
                                int timestamp = (Integer) articleData.get("datetime");
                                LocalDate date = LocalDate.ofEpochDay(timestamp / 86400L); // Convert UNIX timestamp to LocalDate
                                article.setDate(date);

                                // Set the ticker
                                article.setTicker(ticker);

                                return article;
                            })
                            .sorted(Comparator.comparing(NewsArticle::getDate).reversed()) // Sort by LocalDate
                            .limit(1) // Limit to top 5 articles
                            .collect(Collectors.toList());

                        newsByCompany.put(companyName, companyNews);
                        logger.info("Found {} articles for {}", companyNews.size(), companyName);
                    } else {
                        logger.warn("No articles found for {}", companyName);
                    }
                } else {
                    logger.error("Failed to fetch news for {}. Status code: {}", companyName, response.getStatusCode());
                }
            } catch (Exception e) {
                logger.error("Exception while fetching news for {}: {}", companyName, e.getMessage());
            }
        }

        List<NewsArticle> filteredNews = newsByCompany.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        logger.info("Total filtered articles: {}", filteredNews.size());
        return filteredNews;
    }

    private String getTickerForCompany(String companyName) {
        return switch (companyName) {
            case "Johnson & Johnson" -> "JNJ";
            case "Procter & Gamble" -> "PG";
            case "Coca-Cola" -> "KO";
            case "Verizon" -> "VZ";
            case "Walmart" -> "WMT";
            case "McDonald's" -> "MCD";
            case "PepsiCo" -> "PEP";
            case "AT&T" -> "T";
            case "Unilever" -> "UL";
            case "ExxonMobil" -> "XOM";
            case "Apple Inc." -> "AAPL";
            case "Microsoft" -> "MSFT";
            case "Alphabet (Google)" -> "GOOGL";
            case "Amazon" -> "AMZN";
            case "Facebook" -> "FB";
            case "Visa" -> "V";
            case "JPMorgan Chase" -> "JPM";
            case "Disney" -> "DIS";
            case "Netflix" -> "NFLX";
            case "Cisco Systems" -> "CSCO";
            case "Tesla" -> "TSLA";
            case "NVIDIA" -> "NVDA";
            case "Advanced Micro Devices" -> "AMD";
            case "Coinbase" -> "COIN";
            case "Square" -> "SQ";
            case "Palantir Technologies" -> "PLTR";
            case "NIO Inc." -> "NIO";
            case "Beyond Meat" -> "BYND";
            case "Plug Power" -> "PLUG";
            case "Virgin Galactic" -> "SPCE";
            default -> "";
        };
    }
    private List<NewsArticle> filterNewsByCompanyNames(List<NewsArticle> newsArticles, List<String> stockNames) {
        Set<String> stockNamesSet = stockNames.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        return newsArticles.stream()
                .filter(article -> stockNamesSet.stream().anyMatch(stockName ->
                    article.getHeadline().toUpperCase().contains(stockName.toUpperCase()) || 
                    article.getSummary().toUpperCase().contains(stockName.toUpperCase())))
                .collect(Collectors.toList());
    }
}
