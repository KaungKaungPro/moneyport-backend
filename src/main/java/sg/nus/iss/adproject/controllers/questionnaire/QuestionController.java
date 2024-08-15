package sg.nus.iss.adproject.controllers.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sg.nus.iss.adproject.entities.questionnaire.AnswerOption;
import sg.nus.iss.adproject.entities.questionnaire.Question;
import sg.nus.iss.adproject.repositories.questionnaire.AnswerOptionRepository;
import sg.nus.iss.adproject.repositories.questionnaire.QuestionRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/questions/{questionId}/options")
    public List<AnswerOption> getOptionsByQuestionId(@PathVariable Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }
}
