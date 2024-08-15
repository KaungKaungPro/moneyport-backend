package sg.nus.iss.adproject.entities.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sg.nus.iss.adproject.entities.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
public class Post {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PostID")
    private Long postId;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private User user;
    @Setter
    @Getter
    @Column(nullable = false)
    private String title;
    @Getter
    @Setter
    @NotBlank(message = "Post content is required")
    @Size(max = 10000, message = "Post content must not exceed 10000 characters")
    @Column(name = "PostContent", nullable = false, columnDefinition = "TEXT")
    private String postContent;
    @Setter
    @Getter
    @NotNull(message = "Post time is required")
    @Column(name = "PostTime", nullable = false)
    private LocalDateTime postTime;
    @Column(name = "is_pinned")
    private Boolean pinned;
    @Getter
    @Setter
    @Column(name = "pinned_order")
    private Integer pinnedOrder;

    @Getter
    @Column(name = "reply_count")
    private Long replyCount;

    @Getter
    @Column(name = "like_count")
    private Long likeCount;

    @Getter
    @Column(name = "collect_count")
    private Long collectCount;

    @Getter
    @JsonIgnore
    @Transient
    private Integer pageNo;

    @Getter
    @JsonIgnore
    @Transient
    private Integer pageSize;

    public Post(Long postId, User user, String postContent, LocalDateTime postTime) {
        super();
        this.postId = postId;
        this.user = user;
        this.postContent = postContent;
        this.postTime = postTime;

    }
    protected Post() {
    }

    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @JsonProperty("username")
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public Post setPinned(Boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    public Post setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public Post setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Post setReplyCount(Long replyCount) {
        this.replyCount = replyCount;
        return this;
    }

    public Post setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public Post setCollectCount(Long collectCount) {
        this.collectCount = collectCount;
        return this;
    }
}
