package sg.nus.iss.adproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import sg.nus.iss.adproject.entities.learning.FriendLink;
import sg.nus.iss.adproject.entities.learning.LQuestion;
import sg.nus.iss.adproject.entities.learning.Terminology;
import sg.nus.iss.adproject.services.learning.AnswerInterface;
import sg.nus.iss.adproject.services.learning.FriendLinkInterface;
import sg.nus.iss.adproject.services.learning.LearningCentreInterface;
import sg.nus.iss.adproject.services.learning.QuestionInterface;
import sg.nus.iss.adproject.services.learning.TerminologyInterface;
import sg.nus.iss.adproject.entities.learning.Answer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/learn")
public class LearningCentreController {

    @Autowired
    private FriendLinkInterface friendLinkInterface;
    
    @Autowired
    private TerminologyInterface terminologyInterface;
    
    @Autowired
    private QuestionInterface questionInterface;
    
    @Autowired
    private AnswerInterface answerInterface;


    @GetMapping("/terminology")
    public ResponseEntity<?> learnCenter(@RequestParam(value = "term", defaultValue = "") String term) {
        List<Terminology> list = terminologyInterface.getAllTerminology(term);
        Map<String, Object> map = new HashMap<>();
        map.put("terminologies", list);
        map.put("term", term);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/forum")
    public ResponseEntity<?> forum() {
        List<LQuestion> list = questionInterface.getAllQuestion();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "/question/add")
    public ResponseEntity<?> addQuestion(@RequestBody LQuestion question) {
        question.setCreateTime(new Date());
        questionInterface.add(question);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/question/{questionId}")
    public ResponseEntity<?> detailQuestion(@PathVariable("questionId") int questionId) {
        LQuestion question = questionInterface.getQuestion(questionId);
        List<Answer> list = answerInterface.getAllAnswer(questionId);
        Map<String, Object> map = new HashMap<>();
        map.put("question", question);
        map.put("answers", list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping(value = "/answer/add")
    public ResponseEntity<?> addAnswer(@RequestBody Answer answer) {
        answer.setCreateTime(new Date());
        answerInterface.add(answer);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(value = "/answer/upvote/{userId}/{answerId}")
    public ResponseEntity<?> upVoteAnswer(@PathVariable("answerId") int answerId, @PathVariable("userId") int userId) {
        answerInterface.editUpVote(answerId, userId);
        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/link")
    public ResponseEntity<?> link() {
        List<FriendLink> list = friendLinkInterface.getAllFriendLink();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/question/del-{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") int questionId) {
        LQuestion question = questionInterface.getQuestion(questionId);
        if (question == null) {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
        questionInterface.delete(questionId);
        LQuestion deletedQuestion = questionInterface.getQuestion(questionId);
        if (deletedQuestion != null) {
            return new ResponseEntity<>("Failed to delete question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/answer/del-{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") int answerId) {
    	int questionId = answerInterface.getQuestionIdByAnswerId(answerId);
    	answerInterface.delete(answerId);
        
        LQuestion question = questionInterface.getQuestion(questionId);
        List<Answer> list = answerInterface.getAllAnswer(questionId);
        Map<String, Object> map = new HashMap<>();
        map.put("question", question);
        map.put("answers", list);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
