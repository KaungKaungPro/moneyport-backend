package sg.nus.iss.adproject.repositories.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.learning.Sensitive;

import java.util.List;

@Repository
public interface SensitiveRepository extends JpaRepository<Sensitive, Integer> {

    @Query("SELECT se FROM Sensitive se WHERE :inputContent LIKE CONCAT('%', se.content, '%')")
    List<Sensitive> findSensitiveByContent(@Param("inputContent") String inputContent);
}
