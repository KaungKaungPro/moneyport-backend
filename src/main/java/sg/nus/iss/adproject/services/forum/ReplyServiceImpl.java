package sg.nus.iss.adproject.services.forum;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.adproject.common.BaseContext;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.entities.forum.Reply;
import sg.nus.iss.adproject.entities.forum.Post;
import sg.nus.iss.adproject.repositories.UserRepository;
import sg.nus.iss.adproject.repositories.forum.PostRepository;
import sg.nus.iss.adproject.repositories.forum.ReplyRepository;
import sg.nus.iss.adproject.services.forum.IReplyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ReplyServiceImpl implements IReplyService {
    @Resource
    private ReplyRepository replyRepository;
    @Resource
    private PostRepository postRepository;
    @Resource
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Transactional
    public void save(Reply reply) {
        User currentUser = BaseContext.getUser();
        reply.setUser(currentUser);
        reply.setCreateTime(LocalDateTime.now());

        if (reply.getId() == null) {
            Post post = postRepository.getReferenceById(reply.getPostId());
            post.setReplyCount(post.getReplyCount() + 1);
            postRepository.save(post);

            replyRepository.save(reply);

            // 异步发送邮件
            sendNotificationEmails(reply, currentUser, post);
        } else {
            replyRepository.save(reply);
        }
    }

    @Async
    public void sendNotificationEmails(Reply reply, User currentUser, Post post) {
        // 给帖子作者发邮件
        User postAuthor = userRepository.getReferenceById(post.getUserId().intValue());
        String subject = "There is a new reply to your post";
        String content = String.format("User %s replied to your post \"%s\"", currentUser.getUsername(), post.getTitle());
        emailService.sendEmail(postAuthor.getEmail(), subject, content);

        // 如果是回复其他回复，给被回复者也发邮件
        if (reply.getReplyId() != null) {
            Reply parentReply = replyRepository.getReferenceById(reply.getReplyId());
            User parentReplyAuthor = parentReply.getUser();
            String parentSubject = "Someone replied to your comment";
            String parentContent = String.format("User %s replied to your comment on the post \"%s\"", currentUser.getUsername(), post.getTitle());
            emailService.sendEmail(parentReplyAuthor.getEmail(), parentSubject, parentContent);
        }
    }

//    @Transactional
//    @Override
//    public void save(Reply reply) {
//        if (reply.getId() == null) {
//            Post post = postRepository.getReferenceById(reply.getPostId());
//            post.setReplyCount(post.getReplyCount() + 1);
//            postRepository.save(post);
//        }
//        reply.setUser(BaseContext.getUser());
//        reply.setCreateTime(LocalDateTime.now());
//        replyRepository.save(reply);
////        sendNotifications(reply.getId());
//    }
//
//    @Override
//    public void sendNotifications(Long replyId) {
//        Reply reply = replyRepository.getReferenceById(replyId);
//        User currentUser = reply.getUser();
//        Post post = postRepository.getReferenceById(reply.getPostId());
//
//        // 发送邮件给帖子作者
//        User postAuthor = userRepository.getReferenceById(post.getUserId().intValue());
//        String subject = "您的帖子有新回复";
//        String content = String.format("用户 %s 回复了您的帖子 \"%s\"", currentUser.getUsername(), post.getTitle());
//        emailService.sendEmail(postAuthor.getEmail(), subject, content);
//
//        // 如果是回复其他回复，也给被回复的用户发送邮件
//        if (reply.getReplyId() != null) {
//            Reply parentReply = replyRepository.getReferenceById(reply.getReplyId());
//            User parentReplyAuthor = parentReply.getUser();
//            String parentSubject = "有人回复了您的评论";
//            String parentContent = String.format("用户 %s 回复了您在帖子 \"%s\" 下的评论", currentUser.getUsername(), post.getTitle());
//            emailService.sendEmail(parentReplyAuthor.getEmail(), parentSubject, parentContent);
//        }
//    }

    @Transactional
    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        replyRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<Reply> getList(Reply reply) {
        List<Reply> replyList = replyRepository.findAll(getSpecification(reply));
        return replyList;
    }

    @Override
    public Page<Reply> getPage(Reply reply) {
        PageRequest pageRequest = PageRequest.of(reply.getPageNo() - 1, reply.getPageSize());
        List<Reply> replyList = replyRepository.findAll(getSpecification(reply));
        List<Reply> treeList = buildTree(replyList);
        int start = Math.min((int) pageRequest.getOffset(), treeList.size());
        int end = Math.min((start + pageRequest.getPageSize()), treeList.size());
        return new PageImpl<>(treeList.subList(start, end), pageRequest, treeList.size());

    }

    @Override
    public Reply getOne(Reply reply) {
        return replyRepository.findOne(getSpecification(reply)).orElse(null);
    }

    private List<Reply> buildTree(List<Reply> list) {
        List<Reply> replyList = new ArrayList<>();
        Map<Long, Reply> map = list.stream().collect(Collectors.toMap(Reply::getId, item -> item));
        for (Reply item : list) {
            Long replyId = item.getReplyId();
            if (replyId == null) {
                replyList.add(item);
            }
        }
        // 寻找顶级节点
        for (Reply item : list) {
            List<Reply> specifiedItems = replyList.stream()
                    .filter(reply -> reply.getId().equals(item.getId()))
                    .toList();
            if (!specifiedItems.isEmpty()) {
                continue;
            }
            Reply topParent = findTopParent(item, map);
            Long topParentId = topParent.getId();
            Reply bizPostsReply = replyList.stream()
                    .filter(reply -> reply.getId().equals(topParentId))
                    .findFirst().get();
            List<Reply> children = bizPostsReply.getChildren();
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(item);
            bizPostsReply.setChildren(children);
        }
        replyList.forEach(item -> {
            item.getChildren().forEach(child -> {
                Reply ancestor = Reply.builder().build();
                BeanUtils.copyProperties(item, ancestor, "children");
                child.setAncestor(ancestor);
            });
        });
        return replyList;
    }

    private static Reply findTopParent(Reply reply, Map<Long, Reply> nodeMap) {
        Long parentId = reply.getReplyId();
        if (parentId == null) {
            return reply;
        }
        Reply parentReply = nodeMap.get(parentId);
        return findTopParent(parentReply, nodeMap);
    }

    /**
     * 组装查询包装器 Assembling the Query Wrapper
     *
     * @param reply 回复
     * @return 结果
     */
    private Specification<Reply> getSpecification(Reply reply) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (reply.getId() != null) {
                predicates.add(cb.equal(root.get("id"), reply.getId()));
            }
            if (StrUtil.isNotBlank(reply.getContent())) {
                predicates.add(cb.like(root.get("content"), "%" + reply.getContent() + "%"));
            }
            if (reply.getPostId() != null) {
                predicates.add(cb.equal(root.get("postId"), reply.getPostId()));
            }
            if (reply.getReplyId() != null) {
                predicates.add(cb.equal(root.get("replyId"), reply.getReplyId()));
            }
            if (reply.getUser() != null && reply.getUser().getId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), reply.getUser().getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
