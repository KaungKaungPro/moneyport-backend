package sg.nus.iss.adproject.repositories.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sg.nus.iss.adproject.entities.forum.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    List<Post> findAllByOrderByPinnedOrderAscPostTimeDesc();

}
