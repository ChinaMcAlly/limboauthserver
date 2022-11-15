package cn.moonmc.ability.login.data;

import cn.moonmc.ability.login.data.password.HashedPassword;
import cn.moonmc.ability.login.utils.HashUtils;
import lombok.Data;

import java.util.UUID;

@Data
public class User {
    /**
     * 用户uuid
     */
    final UUID uuid;
    /**
     *用户名
     */
    String name;
    /**
     * 密码
     * */
    HashedPassword password;
    /**
     * 手机号
     * */
    String phone;
    /**
     * ip地址
     * */
    String ip;
    /**
     * 上次登录时间
     * */
    long lastLogin;

    User(UUID uuid, String name, String ciphertextPassword, String phone, String ip,long lastLogin) {
        this.uuid = uuid;
        this.name = name;
        this.password =ciphertextPassword==null?null:new HashedPassword(ciphertextPassword);
        this.phone = phone;
        this.ip = ip;
        this.lastLogin = lastLogin;
    }

    public User(UUID uuid, String name, HashedPassword password, String phone, String ip,long lastLogin) {
        this.uuid = uuid;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.ip = ip;
        this.lastLogin = lastLogin;
    }
}
