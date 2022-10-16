package cn.moonmc.limboauthserver.gui;

import cn.moonmc.limbo.Player;
import cn.moonmc.limbo.works.event.Lister;
import cn.moonmc.limbo.works.event.playerEvent.PlayerClickContainer;
import cn.moonmc.limbo.works.event.playerEvent.PlayerRenameItem;
import cn.moonmc.limbo.works.menu.AnvilInventory;
import cn.moonmc.limbo.works.message.JsonText;
import cn.moonmc.limboauthserver.NanoLimbo;
import cn.moonmc.limboauthserver.listener.OnPlayerJoin;
import cn.moonmc.limboauthserver.utils.AuthUtils;
import cn.moonmc.limboauthserver.utils.GUIUtils;
import cn.moonmc.limboauthserver.utils.PlayerUtils;
import cn.moonmc.limboauthserver.utils.StringUtils;

/**
 * 正版 仅绑定
 * package cn.moonmc.limboauthserver.gui
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class Bind {

    public static void open(Player p) {
        String condition;
        switch (PlayerUtils.getPlayerMark(p)) {
            case LOGGED:
                return;
            case INPUT_PHONE:
                condition = "手机号";
                break;
            case INPUT_MSGCODE:
                condition = "验证码";
                break;
            default:
                return;
        }
        condition = "请输入" + condition;
        open(p, "绑定 | " + condition);
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
                switch (PlayerUtils.getPlayerMark(event.getPlayer())) {
                    case INPUT_PHONE -> {
                        long tel;
                        try {
                            tel = Long.parseLong(anvilInventory.getInput());
                        } catch (Exception ignored) {
                            open(p, "绑定 | 手机号格式不正确请重输");
                            return;
                        }
                        if (!StringUtils.isChinaPhoneLegal(anvilInventory.getInput())) {
                            open(p, "绑定 | 手机号格式不正确请重输");
                            return;
                        }

                        int count = AuthUtils.phoneNumberUsedTimes(anvilInventory.getInput());
                        if (count >= NanoLimbo.config.getTelLimit()) {
                            open(p, "绑定 | 已达手机号绑定上限[" + NanoLimbo.config.getTelLimit() + "]");
                        }
                        switch (PlayerUtils.isCanSend(event.getPlayer())) {
                            case COOLDOWN_LIMITED -> {
                                open(p, "绑定 | 于冷却时间, 稍后重试");
                                return;
                            }
                            case DAY_LIMITED -> {
                                open(p, "绑定 | 达每日上限, 明日再试");
                                return;
                            }
                            default -> {
                            }
                        }

                        PlayerUtils.setPlayerTempPhoneNumber(event.getPlayer(), tel);
                        PlayerUtils.savePlayerCode(event.getPlayer(), "SMS_230676030", tel);
                        PlayerUtils.markPlayerLoginStatus(event.getPlayer(), OnPlayerJoin.LoginStatus.INPUT_MSGCODE);
                        open(p, "绑定 | 请输入验证码");
                    }
                    case INPUT_MSGCODE -> {
                        if (PlayerUtils.getPlayerCode(event.getPlayer()).equalsIgnoreCase(anvilInventory.getInput())) {
                            String result = AuthUtils.playerRegister(
                                    event.getPlayer().getUUID().toString(),
                                    event.getPlayer().getName(),
                                    String.valueOf(PlayerUtils.getPlayerTempPhoneNumber(event.getPlayer())),
                                    "ONLINE_NO_PWD"
                            );
                            if (result == null) {
                                PlayerUtils.markPlayerLoginStatus(p, OnPlayerJoin.LoginStatus.LOGGED);
                                OnPlayerJoin.playerTel.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
                                OnPlayerJoin.code.remove(PlayerUtils.getPlayerCondition(event.getPlayer()));
                                // TODO 绑定成功 传送至下一步服务器

                            } else {
                                // 出现错误 提示给玩家
                                open(p, result.split(",")[0] + " | " + result.split(",")[1]);
                            }
                        } else {
                            open(p, "绑定 | 验证码错误请重输");
                        }
                    }
                    default -> {
                    }
                }
            }
        });

        p.openInventory(anvilInventory);
    }

}
