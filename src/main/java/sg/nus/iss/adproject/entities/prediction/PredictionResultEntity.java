package sg.nus.iss.adproject.entities.prediction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Entity
@Table(name = "prediction_results")
public class PredictionResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "prediction_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate predictionDate;

    @Column(name = "user_prediction")
    private double userPrediction;

    @Column(name = "model_predictions")
    @Convert(converter = ModelPredictionsConverter.class)
    private List<Double> modelPredictions; // Store as a list of Double

    @Column(name = "score")
    private int score;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDate predictionDate) {
        this.predictionDate = predictionDate;
    }

    public double getUserPrediction() {
        return userPrediction;
    }

    public void setUserPrediction(double userPrediction) {
        this.userPrediction = userPrediction;
    }

    public List<Double> getModelPredictions() {
        return modelPredictions;
    }

    public void setModelPredictions(List<Double> modelPredictions) {
        this.modelPredictions = modelPredictions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PredictionResultEntity{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", predictionDate=" + predictionDate +
                ", userPrediction=" + userPrediction +
                ", modelPredictions=" + modelPredictions +
                ", score=" + score +
                '}';
    }

    @Converter
    public static class ModelPredictionsConverter implements AttributeConverter<List<Double>, String> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<Double> modelPredictions) {
            try {
                return objectMapper.writeValueAsString(modelPredictions);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error converting list to JSON", e);
            }
        }

        @Override
        public List<Double> convertToEntityAttribute(String dbData) {
            try {
                return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error converting JSON to list", e);
            }
        }
    }
}
