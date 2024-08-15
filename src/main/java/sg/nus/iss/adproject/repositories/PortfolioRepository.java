package sg.nus.iss.adproject.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.simulation.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer>{

	@Query("SELECT p FROM Portfolio p WHERE p.user.id =:userId and p.gameStartDateTime is null")
	Portfolio getPortfoliosByUserId(@Param("userId") long userId);
	
	Portfolio findPortfolioById(long id);
	
	@Query("SELECT p FROM Portfolio p WHERE p.user.id =:userId and p.gameStartDateTime=:startDateTime")
	List<Portfolio> getPortfolioByUserIdAndStartDateTime(@Param("userId") long userId, @Param("startDateTime") LocalDateTime startDateTime);
}
