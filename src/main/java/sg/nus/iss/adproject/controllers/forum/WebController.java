package sg.nus.iss.adproject.controllers.forum;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.adproject.entities.Result;
import sg.nus.iss.adproject.entities.User;
import sg.nus.iss.adproject.services.UserInterface;
import sg.nus.iss.adproject.utils.TokenUtils;

@RestController
@RequestMapping("/api/web")
@CrossOrigin
// TODO 全局跨域
public class WebController {
    @Resource
    private UserInterface uService;

    /**
     * PC端登录 PC Login
     *
     * @param user PC端登录请求体
     * @return 结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User info = uService.login(user.getUsername(), user.getPassword());
        if (info != null) {
            String token = TokenUtils.createToken(info.getId(), info.getPassword());
            info.setToken(token);
            return Result.success(info);
        }
        return Result.error("Login failed! Wrong username or password!");
    }


    /**
     * 注册用户 registered user
     *
     * @param user info 用户信息
     * @return 结果
     */
//    @PostMapping("/register")
//    public Result<Void> register(@RequestBody User user) {
//        uService.register(user);
//        return Result.success();
//    }
}
