package cn.moonmc.ability.login.event;

import cn.moonmc.limboAdd.works.entity.Player;
import cn.moonmc.limboAdd.works.event.Event;
import lombok.Getter;

/**
 * 登录成功事件
 * */
public class LoginSuccessfulEvent implements Event {
    @Getter
    final Player player;

    public LoginSuccessfulEvent(Player player) {
        this.player = player;
    }
}
