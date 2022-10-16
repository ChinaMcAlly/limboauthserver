package cn.moonmc.limboauthserver.gui;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import cn.moonmc.limbo.works.event.playerEvent.PlayerRenameItem;
import cn.moonmc.limbo.works.menu.AnvilInventory;
import cn.moonmc.limbo.works.message.JsonText;
import cn.moonmc.limboauthserver.listener.OnPlayerJoin;
import cn.moonmc.limboauthserver.utils.AuthUtils;
import cn.moonmc.limboauthserver.utils.GUIUtils;
import cn.moonmc.limboauthserver.utils.PlayerUtils;

/**
 * 已绑定 仅登录 GUI
 * package cn.moonmc.limboauthserver.gui
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class LoginOnly {

    public static void open(Player p) {
        open(p, "登录 | 请输入密码");
    }

    public static void open(Player p, String title) {
        AnvilInventory anvilInventory = new AnvilInventory(new JsonText(title));
        anvilInventory.setOut(GUIUtils.oKButton);
        anvilInventory.setIn1(GUIUtils.inputButton);

        anvilInventory.setRenameItemLister(new Lister<>(PlayerRenameItem.class) {
            @Override
            public void listen(PlayerRenameItem event) {
                anvilInventory.setInput(GUIUtils.inputTextRevert(event.getName()));
            }
        });

        //注册点击监听器
        anvilInventory.setClickLister(new Lister<>(PlayerClickContainer.class) {
            @Override
            public void listen(PlayerClickContainer event) {
                String loginResult = AuthUtils.login(event.getPlayer().getName(), anvilInventory.getInput());
                if (loginResult == null) {
                    PlayerUtils.markPlayerLoginStatus(event.getPlayer(), OnPlayerJoin.LoginStatus.LOGGED);
                    // TODO 登陆成功 传送到子服务器吧

                } else {
                    // 出现错误 提示给玩家
                    open(p, loginResult.split(",")[0] + " | " + loginResult.split(",")[1]);
                }
            }
        });

        p.openInventory(anvilInventory);
    }
}
