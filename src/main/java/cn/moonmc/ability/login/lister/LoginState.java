package cn.moonmc.ability.login.lister;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class LoginState {
    @Getter
    String quitCmd = UUID.randomUUID().toString();
    @Getter
    String regCmd = UUID.randomUUID().toString();

    /**
     * 是否已登录
     * */
    @Getter @Setter
    boolean logined = false;
}
