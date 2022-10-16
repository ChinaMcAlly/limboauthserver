package cn.moonmc.limboauthserver.listener;

import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboauthserver.utils.PlayerUtils;

/**
 * package cn.moonmc.limboauthserver.listener
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class OnPlayerQuit {
    public static void register() {
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class) {
            @Override
            public void listen(PlayerJoinEvent event) {
                OnPlayerJoin.loginStatusHashMap.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
                OnPlayerJoin.playerTel.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
                OnPlayerJoin.code.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
                OnPlayerJoin.tempPassword.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
            }
        });
    }
}
