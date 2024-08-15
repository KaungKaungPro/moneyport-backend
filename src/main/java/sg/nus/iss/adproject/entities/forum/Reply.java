package sg.nus.iss.adproject.entities.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import sg.nus.iss.adproject.entities.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 回复
 * </p>
 */
@Entity
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Reply implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 回复内容
     */
    @Size(max = 10000, message = "Content must not exceed 10000 characters")
    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;
    /**
     * 帖子ID
     */
    private Long postId;
    /**
     * 回复ID
     */
    private Long replyId;
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 页码
     */
    @JsonIgnore
    @Transient
    private Integer pageNo;
    /**
     * 页面大小
     */
    @JsonIgnore
    @Transient
    private Integer pageSize;
    /**
     * 子回复
     */
    @Transient
    private List<Reply> children = new ArrayList<>();
    /**
     * 祖节点
     */
    @Transient
    private Reply ancestor;
}
