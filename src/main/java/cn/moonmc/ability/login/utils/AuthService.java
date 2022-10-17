package cn.moonmc.ability.login.utils;

import cn.moonmc.ability.login.entity.User;
import cn.moonmc.ability.login.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author CNLuminous
 */
@Component
@Order(1)
public class AuthService {
    private static UserMapper userMapper;

    @Autowired
    public void autoWired(UserMapper userMapper){
        AuthService.userMapper = userMapper;
    }

    /**
     * 用户登录
     */
    public static boolean login(User user, String password){
        return password.equals(user.getPassword());
    }
    /**
     * 用户注册
     */
    public static boolean register(User user){
        return userMapper.createUser(user);
    }
    /**
     * 用户信息更新
     */
    public static boolean update(User user){
        return userMapper.updateUser(user);
    }
    /**
     * 查询用户数据
     */
    public static User selectUser(String uuid){
        return userMapper.selectUser(uuid);
    }
}