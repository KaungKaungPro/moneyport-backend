package sg.nus.iss.adproject.common.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sg.nus.iss.adproject.common.BaseContext;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.services.UserService;

/**
 * jwt拦截器 Jwt interceptor
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String TOKEN = "token";
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取令牌 token
        String token = request.getHeader(TOKEN);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(TOKEN);
        }
        // 开始执行认证
        if (StrUtil.isBlank(token)) {
            // TODO 自定义异常，返回状态401
        	String msg = "Token authentication not found, please log in again";
        	System.out.println("Interceptor: " + msg);
            throw new RuntimeException(msg);
        }
        User user;
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            user = userService.getUserByUserId(Long.valueOf(userId));
            user.setToken(token);
        } catch (Exception e) {
        	String msg = "Token authentication failed, please log in again";
        	System.out.println("Interceptor: " + msg);
            throw new RuntimeException(msg);
        }
        if (ObjectUtil.isNull(user)) {
            throw new RuntimeException("User does not exist");
        }
        try {
            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
        	String msg = "Token authentication verification failed, please log in again";
        	System.out.println("Interceptor: " + msg);
            throw new RuntimeException(msg);
        }
//        User user = userService.getDefaultUser();
        BaseContext.setUser(user);
        return true;
    }
}
