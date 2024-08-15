package sg.nus.iss.adproject.repositories.questionnaire;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.adproject.entities.questionnaire.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

	@Transactional
	void deleteByUserId(Long userId);

	
	//@Query("DELETE R FROM Response R WHERE R.userId = :userId")
	//public void deleteByUserId(@Param("userId") Long userId);
	
	
}
