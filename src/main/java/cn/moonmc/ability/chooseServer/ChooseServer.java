package cn.moonmc.ability.chooseServer;

import cn.moonmc.ability.AbilityServer;
import cn.moonmc.ability.login.event.LoginSuccessfulEvent;
import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerCommandEvent;
import lombok.Getter;

public class ChooseServer {
    @Getter
    private final AbilityServer server;
    public ChooseServer(AbilityServer server) {
        this.server = server;
        /**
         * 监听玩家登录成功事件
         * */
        EventManager.regLister(LoginSuccessfulEvent.class, event -> {

        });

        /**
         * 监听玩家命令事件
         * */
        EventManager.regLister(PlayerCommandEvent.class,event -> {

        });
    }
}
