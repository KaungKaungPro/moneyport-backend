	package sg.nus.iss.adproject.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息返回实体
 *
 * @param <T> 泛型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3709531800279000172L;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 响应体
     */
    private T data;

    /**
     * 消息返回实体
     *
     * @param <Void> 空泛型
     * @return 响应结果
     */
    public static <Void> Result<Void> success() {
        return Result.<Void>builder()
                .code(200)
                .msg("Success！")
                .data(null)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param msg    消息
     * @param <Void> 空泛型
     * @return 响应结果
     */
    public static <Void> Result<Void> success(String msg) {
        return Result.<Void>builder()
                .code(200)
                .msg(msg)
                .data(null)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param data 响应体
     * @param <T>  泛型对象
     * @return 响应结果
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .msg("Success！")
                .data(data)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param msg  消息
     * @param data 响应体
     * @param <T>  泛型对象
     * @return 响应结果
     */
    public static <T> Result<T> success(String msg, T data) {
        return Result.<T>builder()
                .code(200)
                .msg(msg)
                .data(data)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param <Void> 空泛型
     * @return 响应结果
     */
    public static <Void> Result<Void> error() {
        return Result.<Void>builder()
                .code(500)
                .msg("Error！")
                .data(null)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param msg    消息
     * @param <Void> 空泛型
     * @return 响应结果
     */
    public static <Void> Result<Void> error(String msg) {
        return Result.<Void>builder()
                .code(500)
                .msg(msg)
                .data(null)
                .build();
    }

    /**
     * 消息返回实体
     *
     * @param code   状态码
     * @param msg    消息
     * @param <Void> 空泛型
     * @return 响应结果
     */
    public static <Void> Result<Void> error(Integer code, String msg) {
        return Result.<Void>builder()
                .code(code)
                .msg(msg)
                .data(null)
                .build();
    }
}
