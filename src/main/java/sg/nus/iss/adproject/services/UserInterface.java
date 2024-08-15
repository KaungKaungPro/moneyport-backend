package sg.nus.iss.adproject.services;

import sg.nus.iss.adproject.entities.User;

public interface UserInterface {
	
	User login(String username, String password);

	User getDefaultUser();
	
	void create(User user);
	
	User getUserByUsername(String username);
	
	User getUserByUserId(Long userId);
}
