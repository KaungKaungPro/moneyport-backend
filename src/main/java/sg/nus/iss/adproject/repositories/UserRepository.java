package sg.nus.iss.adproject.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
	User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	@Query("SELECT u FROM User u WHERE u.id = 1")
	User findDefaultUser();
	
	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findUserById(@Param("id") Long id);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findUserByUsername(@Param("username") String username);
}
