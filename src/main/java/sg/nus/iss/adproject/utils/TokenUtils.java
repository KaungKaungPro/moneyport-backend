package sg.nus.iss.adproject.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Token工具类 Token utility class
 */
@Component
public class TokenUtils {
    /**
     * 生成token Create token
     */
    public static String createToken(Long userId, String password) {
        return JWT.create()
                .withAudience(String.valueOf(userId))
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(password));
    }
}

