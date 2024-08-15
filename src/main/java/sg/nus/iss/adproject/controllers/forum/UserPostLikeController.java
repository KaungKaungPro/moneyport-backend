package sg.nus.iss.adproject.controllers.forum;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.Result;
import sg.nus.iss.adproject.entities.forum.UserPostLike;
import sg.nus.iss.adproject.services.forum.IUserPostLikeService;

import java.util.List;

/**
 * <p>
 * 用户帖子点赞前端控制器
 * </p>
 */
@RestController
@RequestMapping("/api/userPostLike")
public class UserPostLikeController {
    @Resource
    private IUserPostLikeService userPostLikeService;

    /**
     * 添加、修改用户帖子点赞 Add and modify user post likes
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    @PostMapping
    public Result<Void> save(@RequestBody UserPostLike userPostLike) {
        userPostLikeService.save(userPostLike);
        return Result.success();
    }

    /**
     * 删除用户帖子点赞 Delete user post likes
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public Result<Void> deleteAllByIdInBatch(@PathVariable List<Long> ids) {
        userPostLikeService.deleteAllByIdInBatch(ids);
        return Result.success();
    }

    /**
     * 查询用户帖子点赞列表 Query the list of user post likes
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    @GetMapping("/list")
    public Result<List<UserPostLike>> getList(UserPostLike userPostLike) {
        List<UserPostLike> list = userPostLikeService.getList(userPostLike);
        return Result.success(list);
    }

    /**
     * 查询用户帖子点赞分页 Query User Post Likes Paging
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    @GetMapping("/page")
    public Result<Page<UserPostLike>> getPage(UserPostLike userPostLike) {
        Page<UserPostLike> page = userPostLikeService.getPage(userPostLike);
        return Result.success(page);
    }

    /**
     * 查询用户帖子点赞 Checking user post likes
     *
     * @param userPostLike 用户帖子点赞
     * @return 结果
     */
    @GetMapping
    public Result<UserPostLike> getOne(UserPostLike userPostLike) {
        userPostLike = userPostLikeService.getOne(userPostLike);
        return Result.success(userPostLike);
    }
}
