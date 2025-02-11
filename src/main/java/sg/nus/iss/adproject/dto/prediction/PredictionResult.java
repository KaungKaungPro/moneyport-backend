package sg.nus.iss.adproject.dto.prediction;

import java.util.List;

public class PredictionResult {
    private List<Double> modelPredictions; // List of Double for model predictions
    private double userPrediction;
    private int score;

    // Constructors
    public PredictionResult() {}

    public PredictionResult(List<Double> modelPredictions, double userPrediction, int score) {
        this.modelPredictions = modelPredictions;
        this.userPrediction = userPrediction;
        this.score = score;
    }

    // Getters and Setters
    public List<Double> getModelPredictions() {
        return modelPredictions;
    }

    public void setModelPredictions(List<Double> modelPredictions) {
        this.modelPredictions = modelPredictions;
    }

    public double getUserPrediction() {
        return userPrediction;
    }

    public void setUserPrediction(double userPrediction) {
        this.userPrediction = userPrediction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PredictionResult{" +
                "modelPredictions=" + modelPredictions +
                ", userPrediction=" + userPrediction +
                ", score=" + score +
                '}';
    }
}
