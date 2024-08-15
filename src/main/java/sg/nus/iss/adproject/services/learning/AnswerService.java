package sg.nus.iss.adproject.services.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.learning.Answer;
import sg.nus.iss.adproject.entities.learning.AnswerUpvote;
import sg.nus.iss.adproject.repositories.UserRepository;
import sg.nus.iss.adproject.repositories.learning.AnswerRepository;
import sg.nus.iss.adproject.repositories.learning.AnswerUpvoteRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AnswerService implements AnswerInterface {
    @Autowired
    private AnswerRepository repository;
    
    @Autowired
    private AnswerUpvoteRepository aur;
    
    @Autowired
    private UserRepository ur;

    @Override
    public List<Answer> getAllAnswer(int questionId) {
        List<Answer> list = repository.findAnswerByQuestionId(questionId);
        Optional<Answer> maxUpvoteAnswer = list.stream().max(Comparator.comparingInt(Answer::getUpVote));
        if (maxUpvoteAnswer.isPresent()) {
            Answer topAnswer = maxUpvoteAnswer.get();
            list.remove(topAnswer);
            list.sort(Comparator.comparing(Answer::getCreateTime).reversed());
            list.add(0, topAnswer);
        }
        return list;
    }

    @Override
    @Transactional
    public void add(Answer answer) {
        repository.save(answer);
    }

    @Override
    @Transactional
    public void editUpVote(int answerId, int userId) {
    	Optional<AnswerUpvote> optional = aur.findByAnswerAndUser(answerId, userId);
    	Answer entityAns = repository.findById(answerId).orElse(null);
        if (optional.isPresent()) {
            AnswerUpvote item = optional.get();
            aur.delete(item);
            if(entityAns != null) {
            	entityAns.setUpVote((int)Math.max(entityAns.getUpVote() - 1, 0.0));
            	repository.save(entityAns);
            }
        } else {
        	User user = ur.findById(userId).orElse(null);
        	if(user != null) {
        		AnswerUpvote newUpvote = new AnswerUpvote(entityAns);
        		newUpvote.addVoter(user);
                aur.save(newUpvote);
                if(entityAns != null) {
                	entityAns.setUpVote(entityAns.getUpVote() + 1);
                	repository.save(entityAns);
                }
        	}
        }
    }
    
    @Override
    @Transactional
    public void delete(int answerId) {
    	repository.deleteById(answerId);
    }
    
    @Override
    @Transactional
    public Integer getQuestionIdByAnswerId(int answerId) {
    	return repository.findQuestionIdByAnswerId(answerId);
    }

    @Override
    public void deleteAllByQuestionId(int questionId) {
        repository.deleteAllByQuestionId(questionId);
    }
}
