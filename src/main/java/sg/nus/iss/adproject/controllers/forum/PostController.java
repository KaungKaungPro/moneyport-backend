package sg.nus.iss.adproject.controllers.forum;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.Result;
import sg.nus.iss.adproject.entities.forum.Post;
import sg.nus.iss.adproject.services.forum.PostService;

import java.util.List;

/**
 * <p>
 * 帖子前端控制器 Post front-end controller
 * </p>
 */
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Resource
    private PostService postService;

    /**
     * 添加、修改帖子 Adding and modifying posts
     *
     * @param post 帖子
     * @return 结果
     */
    @PostMapping
    public Result<Void> save(@RequestBody Post post) {
        postService.save(post);
        return Result.success();
    }

    /**
     * 删除帖子 Delete post
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public Result<Void> deleteAllByIdInBatch(@PathVariable List<Long> ids) {
        postService.deleteAllByIdInBatch(ids);
        return Result.success();
    }

    /**
     * 查询帖子列表 Query Post List
     *
     * @param post 帖子
     * @return 结果
     */
    @GetMapping("/list")
    public Result<List<Post>> getList(Post post) {
        List<Post> list = postService.getList(post);
        return Result.success(list);
    }

    /**
     * 查询帖子分页 Query Post Pagination
     *
     * @param post 帖子
     * @return 结果
     */
    @GetMapping("/page")
    public Result<Page<Post>> getPage(Post post) {
        Page<Post> page = postService.getPage(post);
        return Result.success(page);
    }

    /**
     * 查询帖子 Enquiry Post
     *
     * @param post 帖子
     * @return 结果
     */
    @GetMapping
    public Result<Post> getOne(Post post) {
        post = postService.getOne(post);
        return Result.success(post);
    }
}
