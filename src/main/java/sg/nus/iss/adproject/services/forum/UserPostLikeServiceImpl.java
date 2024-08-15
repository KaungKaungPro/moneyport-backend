package sg.nus.iss.adproject.services.forum;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.adproject.common.BaseContext;
import sg.nus.iss.adproject.entities.forum.UserPostLike;
import sg.nus.iss.adproject.entities.forum.Post;
import sg.nus.iss.adproject.repositories.forum.PostRepository;
import sg.nus.iss.adproject.repositories.forum.UserPostLikeRepository;
import sg.nus.iss.adproject.services.forum.IUserPostLikeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserPostLikeServiceImpl implements IUserPostLikeService {
    @Resource
    private UserPostLikeRepository userPostLikeRepository;
    @Resource
    private PostRepository postRepository;

    @Transactional
    @Override
    public void save(UserPostLike userPostLike) {
        if (userPostLike.getId() == null) {
            Post post = postRepository.getReferenceById(userPostLike.getPostId());
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
        }
        userPostLike.setUser(BaseContext.getUser());
        userPostLike.setCreateTime(LocalDateTime.now());
        userPostLikeRepository.save(userPostLike);
    }

    @Transactional
    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        // TODO 取消喜欢减少数量 cancels the preference for reduced quantities
        userPostLikeRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<UserPostLike> getList(UserPostLike userPostLike) {
        List<UserPostLike> userPostLikeList = userPostLikeRepository.findAll(getSpecification(userPostLike));
        return userPostLikeList;
    }

    @Override
    public Page<UserPostLike> getPage(UserPostLike userPostLike) {
        PageRequest pageRequest = PageRequest.of(userPostLike.getPageNo() - 1, userPostLike.getPageSize());
        Page<UserPostLike> page = userPostLikeRepository.findAll(getSpecification(userPostLike), pageRequest);
        return page;
    }

    @Override
    public UserPostLike getOne(UserPostLike userPostLike) {
        return userPostLikeRepository.findOne(getSpecification(userPostLike)).orElse(null);
    }

    /**
     * 组装查询包装器 Assembling the Query Wrapper
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    private Specification<UserPostLike> getSpecification(UserPostLike userPostLike) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userPostLike.getId() != null) {
                predicates.add(cb.equal(root.get("id"), userPostLike.getId()));
            }
            if (userPostLike.getUser() != null && userPostLike.getUser().getId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userPostLike.getUser().getId()));
            }
            if (userPostLike.getPostId() != null) {
                predicates.add(cb.equal(root.get("postId"), userPostLike.getPostId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
