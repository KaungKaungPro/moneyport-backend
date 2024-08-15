package sg.nus.iss.adproject.services.forum;

import org.springframework.data.domain.Page;
import sg.nus.iss.adproject.entities.forum.UserPostLike;

import java.util.List;


public interface IUserPostLikeService {
    /**
     * 添加、修改用户帖子点赞  Add, modify user post likes
     *
     * @param userPostLike 用户帖子点赞
     */
    void save(UserPostLike userPostLike);

    /**
     * 删除用户帖子点赞 Delete user post likes
     *
     * @param ids ID列表
     */
    void deleteAllByIdInBatch(List<Long> ids);

    /**
     * 查询用户帖子点赞列表 Query user post likes
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    List<UserPostLike> getList(UserPostLike userPostLike);

    /**
     * 查询用户帖子点赞分页 Query user post likes paging
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    Page<UserPostLike> getPage(UserPostLike userPostLike);

    /**
     * 查询用户帖子点赞 Query user post likes
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    UserPostLike getOne(UserPostLike userPostLike);
}
