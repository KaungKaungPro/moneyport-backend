package sg.nus.iss.adproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.nus.iss.adproject.entities.simulation.MktSimParam;

public interface MktSimParamRepository extends JpaRepository<MktSimParam, Integer>{

	@Query("SELECT m FROM MktSimParam m ORDER BY m.paramDateCreated DESC")
	List<MktSimParam> getMktSimParamsOrderByCreationDate();
}
