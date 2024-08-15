package sg.nus.iss.adproject.repositories.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sg.nus.iss.adproject.entities.forum.UserPostLike;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long>, JpaSpecificationExecutor<UserPostLike> {

}
