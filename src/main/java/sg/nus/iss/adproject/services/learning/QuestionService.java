package sg.nus.iss.adproject.services.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.adproject.common.BaseContext;
import sg.nus.iss.adproject.entities.learning.*;
import sg.nus.iss.adproject.repositories.learning.LQuestionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService implements QuestionInterface {

    @Autowired
    private LQuestionRepository repository;

    @Autowired
    private AnswerInterface answerInterface;

    @Override
    public List<LQuestion> getAllQuestion() {
        List<LQuestion> list = repository.findAll();
        list.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        return list;
    }

    @Override
    @Transactional
    public void add(LQuestion question) {
        repository.save(question);
    }

    @Override
    public LQuestion getQuestion(int questionId) {
        Optional<LQuestion> optional = repository.findById(questionId);
        return optional.orElse(null);
    }

    @Override
    @Transactional
    public void delete(int questionId) {

        answerInterface.deleteAllByQuestionId(questionId);

        repository.deleteById(questionId);
    }
}
