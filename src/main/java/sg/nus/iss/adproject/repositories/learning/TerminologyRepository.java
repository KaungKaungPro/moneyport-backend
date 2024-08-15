package sg.nus.iss.adproject.repositories.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.learning.Terminology;

import java.util.List;

@Repository
public interface TerminologyRepository extends JpaRepository<Terminology, Integer> {

	@Query("SELECT t FROM Terminology t WHERE t.definition like %:term% OR t.term like %:term% ORDER BY t.definition, t.term")
    List<Terminology> findByTermContaining(@Param("term") String term);

}
