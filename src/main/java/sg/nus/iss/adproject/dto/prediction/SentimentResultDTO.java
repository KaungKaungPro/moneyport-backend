package sg.nus.iss.adproject.dto.prediction;

public class SentimentResultDTO {
    private String headline;
    private String ticker;
    private double score;
    private String sentiment;

    // Getters and setters

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSentiments() {
        return sentiment;
    }

    public void setSentiments(String sentiments) {
        this.sentiment = sentiments;
    }
}
