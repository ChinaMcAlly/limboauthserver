package cn.moonmc.limboauthserver.listener;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.EventManager;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerJoinEvent;
import cn.moonmc.limboauthserver.NanoLimbo;
import cn.moonmc.limboauthserver.gui.Bind;
import cn.moonmc.limboauthserver.gui.LoginOnly;
import cn.moonmc.limboauthserver.gui.RegisterAndBind;
import cn.moonmc.limboauthserver.utils.AuthUtils;
import cn.moonmc.limboauthserver.utils.PlayerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * package cn.moonmc.limboauthserver.listener
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class OnPlayerJoin {

    public static HashMap<String, LoginStatus> loginStatusHashMap = new HashMap<>();
    public static HashMap<String, List<String>> dayLimited = new HashMap<>();
    public static HashMap<String, Integer> dayCount = new HashMap<>();
    public static HashMap<String, String> code = new HashMap<>();
    public static List<String> cooldownLimited = new ArrayList<>();
    public static HashMap<String, Long> playerTel = new HashMap<>();
    public static HashMap<String, String> tempPassword = new HashMap<>();

    public static void register() {
        EventManager.regLister(new Lister<>(PlayerJoinEvent.class) {
            @Override
            public void listen(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                String condition = NanoLimbo.config.isOnline() ? player.getUUID().toString() : player.getName();
                if (NanoLimbo.config.isOnline()) {
                    // 正版且已经绑定手机 可以不用任何流程直接进入
                    if (AuthUtils.isPlayerRegistered(condition)) {
                        return;
                    }
                    PlayerUtils.markPlayerLoginStatus(player, LoginStatus.INPUT_PHONE);
                    // 打开仅绑定手机GUI
                    Bind.open(player);
                } else {
                    if (AuthUtils.isPlayerRegistered(condition)) {
                        PlayerUtils.markPlayerLoginStatus(player, LoginStatus.UNLOGGED);
                        // 打开输入密码GUI
                        LoginOnly.open(player);
                    } else {
                        PlayerUtils.markPlayerLoginStatus(player, LoginStatus.INPUT_PASSWORD);
                        // 打开注册界面 先输入密码
                        RegisterAndBind.open(player);
                    }
                }
            }
        });
    }

    public enum LoginStatus {
        RESET_INPUT_PHONE,
        UNLOGGED,
        INPUT_PASSWORD_REPEAT,
        INPUT_PASSWORD,
        INPUT_PHONE,
        INPUT_MSGCODE,
        LOGGED,
        RESET_PWD_INPUT,
        RESET_PWD_INPUT_REPEAT,
        RESET_PWD_INPUT_MSGCODE,
    }
}
