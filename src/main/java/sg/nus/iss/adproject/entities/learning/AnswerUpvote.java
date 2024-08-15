package sg.nus.iss.adproject.entities.learning;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import sg.nus.iss.adproject.entities.User;

@Entity
public class AnswerUpvote {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private Answer answer;

	@ManyToMany
	private List<User> voters;
	
	public AnswerUpvote() {}

	public AnswerUpvote(Answer answer) {
		super();
		this.answer = answer;
		this.voters = new ArrayList<User>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public List<User> getVoter() {
		return voters;
	}

	public void setVoter(List<User> voters) {
		this.voters = voters;
	}
	
	public void addVoter(User user) {
		voters.add(user);
	}
}
