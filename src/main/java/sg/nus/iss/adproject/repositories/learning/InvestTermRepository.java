package sg.nus.iss.adproject.repositories.learning;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.adproject.entities.learning.InvestTerm;

public interface InvestTermRepository extends JpaRepository<InvestTerm, Integer> {

	@Query("SELECT it FROM InvestTerm it WHERE it.name like %:searchTerm%")
	public List<InvestTerm> findInvestTermsByName(@Param("searchTerm") String searchTerm);
}
