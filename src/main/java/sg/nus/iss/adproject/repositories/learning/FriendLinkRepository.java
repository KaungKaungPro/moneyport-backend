package sg.nus.iss.adproject.repositories.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.learning.FriendLink;

@Repository
public interface FriendLinkRepository extends JpaRepository<FriendLink, Integer> {

}
