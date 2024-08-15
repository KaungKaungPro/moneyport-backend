package sg.nus.iss.adproject.services.forum;

import cn.hutool.core.util.StrUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sg.nus.iss.adproject.common.BaseContext;
import sg.nus.iss.adproject.entities.forum.Post;
import sg.nus.iss.adproject.repositories.forum.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

//    @Override
//    public List<Post> getAllPosts() {
//        return postRepository.findAllByOrderByPinnedOrderAscPostTimeDesc();
//    }

//	@Override
//	public Post pinPost(Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
//        
//        if (!post.isPinned()) {
//            post.setPinned(true);
//            // 获取当前最小的 pinnedOrder，并将新的置顶帖子放在最前面
//            Integer smallestPinnedOrder = postRepository.findAllByOrderByPinnedOrderAsc()
//                    .stream()
//                    .filter(p -> p.getPinnedOrder() != null)
//                    .findFirst()
//                    .map(p -> p.getPinnedOrder() - 1)
//                    .orElse(0);
//            post.setPinnedOrder(smallestPinnedOrder);
//        }
//        
//        return postRepository.save(post);
//    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

//	@Override
//	public Post unpinPost(Long postId) {
//		Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
//        
//        if (post.isPinned()) {
//            post.setPinned(false);
//            post.setPinnedOrder(null);
//        }
//        
//        return postRepository.save(post);
//	}

    @Override
    public void reorderPinnedPosts(List<Long> pinnedPostIds) {
        List<Post> pinnedPosts = postRepository.findAllById(pinnedPostIds);
        for (int i = 0; i < pinnedPosts.size(); i++) {
            Post post = pinnedPosts.get(i);
            post.setPinnedOrder(i);
            postRepository.save(post);

        }

    }

    @Override
    public void save(Post post) {
        if (post.getPostId() == null) {
            post.setUser(BaseContext.getUser());
            post.setPostTime(LocalDateTime.now());
            post.setReplyCount(0L);
            post.setLikeCount(0L);
            post.setCollectCount(0L);
            post.setPinned(false);
            postRepository.save(post);
            return;
        }
        Post reference = postRepository.findById(post.getPostId()).get();
        reference.setPinned(post.getPinned());
        postRepository.save(reference);
    }

    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        postRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<Post> getList(Post post) {
        List<Post> postList = postRepository.findAll(getSpecification(post));
        return postList;
    }

    @Override
    public Page<Post> getPage(Post post) {
        PageRequest pageRequest = PageRequest.of(post.getPageNo() - 1, post.getPageSize());
        Page<Post> page = postRepository.findAll(getSpecification(post), pageRequest);
        return page;
    }

    @Override
    public Post getOne(Post post) {
        return postRepository.findOne(getSpecification(post)).orElse(null);
    }

    /**
     * 组装查询包装器 Assembling the Query Wrapper
     *
     * @param post 帖子
     * @return 结果
     */
    private Specification<Post> getSpecification(Post post) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (post.getPostId() != null) {
                predicates.add(cb.equal(root.get("postId"), post.getPostId()));
            }
            if (post.getPinnedOrder() != null) {
                predicates.add(cb.equal(root.get("pinnedOrder"), post.getPinnedOrder()));
            }
            if (post.getUserId() != null) {
                predicates.add(cb.equal(root.get("userid"), post.getUserId()));
            }
            if (StrUtil.isNotBlank(post.getPostContent())) {
                predicates.add(cb.like(root.get("postContent"), "%" + post.getPostContent() + "%"));
            }
            if (StrUtil.isNotBlank(post.getTitle())) {
                predicates.add(cb.like(root.get("title"), "%" + post.getTitle() + "%"));
            }
            query.orderBy(
                    cb.desc(root.get("pinned")),
                    cb.desc(root.get("postTime"))
            );
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
