package sg.nus.iss.adproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.repositories.UserRepository;


@Service
@Transactional
public class UserService implements UserInterface{

	@Autowired
	private UserRepository ur;

	@Override
	public User login(String username, String password) {
		return ur.findUserByUsernameAndPassword(username, password);
	}
	
	@Override
	public User getDefaultUser() {
		return ur.findDefaultUser();
	}
	
	@Override
	public void create(User user) {
		ur.save(user);
	}
	
	@Override
	public User getUserByUsername(String username) {
		return ur.findUserByUsername(username);
	}
	
	@Override
	public User getUserByUserId(Long userId) {
		return ur.findUserById(userId);
	}
}
