package sg.nus.iss.adproject.common;

import sg.nus.iss.adproject.entities.User;

public class BaseContext {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    /**
     * 获取用户 user acquisition
     *
     * @return 当前用户 current user
     */
    public static User getUser() {
        return threadLocal.get();
    }

    /**
     * 设置用户 Setting up users
     *
     * @param user 当前用户 current user
     */
    public static void setUser(User user) {
        threadLocal.set(user);
    }
}
