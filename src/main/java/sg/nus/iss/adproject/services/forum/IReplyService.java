package sg.nus.iss.adproject.services.forum;

import org.springframework.data.domain.Page;
import sg.nus.iss.adproject.entities.forum.Reply;

import java.util.List;


public interface IReplyService {
    /**
     * 添加、修改回复 Add, modify replies
     *
     * @param reply 回复
     */
    void save(Reply reply);
//    void sendNotifications(Long replyId);

    /**
     * 删除回复 Delete Reply
     *
     * @param ids ID列表
     */
    void deleteAllByIdInBatch(List<Long> ids);

    /**
     * 查询回复列表 Query Response List
     *
     * @param reply 回复
     * @return 结果
     */
    List<Reply> getList(Reply reply);

    /**
     * 查询回复分页 Query Response Paging
     *
     * @param reply 回复
     * @return 结果
     */
    Page<Reply> getPage(Reply reply);

    /**
     * 查询回复 Reply to enquiry
     *
     * @param reply 回复
     * @return 结果
     */
    Reply getOne(Reply reply);
}
