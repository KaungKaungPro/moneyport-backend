package sg.nus.iss.adproject.controllers.forum;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.Result;
import sg.nus.iss.adproject.entities.forum.Reply;
import sg.nus.iss.adproject.services.forum.IReplyService;

import java.util.List;

/**
 * <p>
 * 回复前端控制器
 * </p>
 */
@RestController
@RequestMapping("/api/reply")
public class ReplyController {
    @Resource
    private IReplyService replyService;

    /**
     * 添加、修改回复 Add, modify replies
     *
     * @param reply 回复
     * @return 结果
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody Reply reply) {
        replyService.save(reply);
        return Result.success();
    }

//    @PostMapping("/sendnotification")
//    public Result<Void> sendNotifications(@RequestBody Long replyId) {
//        replyService.sendNotifications(replyId);
//        return Result.success();
//    }

    /**
     * 删除回复 Delete Reply
     *
     * @param ids ID列表
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public Result<Void> deleteAllByIdInBatch(@PathVariable List<Long> ids) {
        replyService.deleteAllByIdInBatch(ids);
        return Result.success();
    }

    /**
     * 查询回复列表 Query Response List
     *
     * @param reply 回复
     * @return 结果
     */
    @GetMapping("/list")
    public Result<List<Reply>> getList(Reply reply) {
        List<Reply> list = replyService.getList(reply);
        return Result.success(list);
    }

    /**
     * 查询回复分页  Query Response Paging
     *
     * @param reply 回复
     * @return 结果
     */
    @GetMapping("/page")
    public Result<Page<Reply>> getPage(Reply reply) {
        Page<Reply> page = replyService.getPage(reply);
        return Result.success(page);
    }

    /**
     * 查询回复 Reply to enquiry
     *
     * @param reply 回复
     * @return 结果
     */
    @GetMapping
    public Result<Reply> getOne(Reply reply) {
        reply = replyService.getOne(reply);
        return Result.success(reply);
    }
}
