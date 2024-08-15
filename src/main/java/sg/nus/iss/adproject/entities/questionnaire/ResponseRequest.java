package sg.nus.iss.adproject.entities.questionnaire;

import java.util.List;

public class ResponseRequest {
    private Long userId;
    private List<ResponseDto> responses;

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ResponseDto> getResponses() {
        return responses;
    }

    public void setResponses(List<ResponseDto> responses) {
        this.responses = responses;
    }

    public static class ResponseDto {
        private Integer questionId;
        private Integer responseValue;

        // Getters and setters
        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public Integer getResponseValue() {
            return responseValue;
        }

        public void setResponseValue(Integer responseValue) {
            this.responseValue = responseValue;
        }
    }
}
