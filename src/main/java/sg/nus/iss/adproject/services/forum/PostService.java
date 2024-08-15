package sg.nus.iss.adproject.services.forum;

import org.springframework.data.domain.Page;
import sg.nus.iss.adproject.entities.forum.Post;

import java.util.List;

public interface PostService {
	
//	List<Post> getAllPosts();
	
//	Post pinPost(Long postId);
	
	Post createPost(Post post);
	
//	Post unpinPost(Long postId);
	
	void reorderPinnedPosts(List<Long> pinnedPostIds);

	/**
	 * 添加、修改帖子 Add, modify posts
	 *
	 * @param post 帖子
	 */
	void save(Post post);

	/**
	 * 删除帖子 Delete posts
	 *
	 * @param ids ID列表
	 */
	void deleteAllByIdInBatch(List<Long> ids);

	/**
	 * 查询帖子列表 		Query post list
	 *
	 * @param post 帖子
	 * @return 结果
	 */
	List<Post> getList(Post post);

	/**
	 * 查询帖子分页 		Query post paging
	 *
	 * @param post 帖子
	 * @return 结果
	 */
	Page<Post> getPage(Post post);

	/**
	 * 查询帖子 		Query post
	 *
	 * @param post 帖子
	 * @return 结果
	 */
	Post getOne(Post post);
}
