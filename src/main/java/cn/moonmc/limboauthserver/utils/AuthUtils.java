package cn.moonmc.limboauthserver.utils;

import cn.moonmc.limboauthserver.NanoLimbo;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * package cn.moonmc.limboauthserver.utils
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class AuthUtils {
    /**
     * 玩家是否注册过
     *
     * @param playerCondition 玩家唯一识别符 online=uuid offline=name
     * @return 是否注册过
     */
    public static boolean isPlayerRegistered(String playerCondition) {
        JSONObject result = HttpUtils.doGet(String.format(
                "%s/user/%s/%s/%s/userExist",
                NanoLimbo.config.getUrl(),
                NanoLimbo.config.isOnline() ? "online" : "offline",
                NanoLimbo.config.getServerName(),
                playerCondition
        ), false, new JSONObject(new HashMap<>(1) {{
            put("Authorization", "MoonApi " + NanoLimbo.config.getApiKey());
        }}));
        assert result != null;
        return result.getJSONObject("data").getBoolean("exist");
    }

    /**
     * 手机号用了几次了
     *
     * @param phoneNumber 手机号
     * @return 用了几次
     */
    public static int phoneNumberUsedTimes(String phoneNumber) {
        JSONObject result = HttpUtils.doGet(String.format(
                "%s/user/%s/%s/%s/phoneNumberExist",
                NanoLimbo.config.getUrl(),
                NanoLimbo.config.isOnline() ? "online" : "offline",
                NanoLimbo.config.getServerName(),
                phoneNumber
        ), false, new JSONObject(new HashMap<>(1) {{
            put("Authorization", "MoonApi " + NanoLimbo.config.getApiKey());
        }}));
        assert result != null;
        return result.getJSONObject("data").getInteger("total");
    }

    /**
     * 玩家登录
     *
     * @param playerCondition 玩家唯一识别符 要登陆一定是离线 所以是玩家游戏名name
     * @param password        明文密码
     * @return 登录是否成功 成功=null 不成功=提示信息
     */
    public static String login(String playerCondition, String password) {
        JSONObject result = HttpUtils.doPost(String.format(
                "%s/user/%s/%s/login",
                NanoLimbo.config.getUrl(),
                NanoLimbo.config.isOnline() ? "online" : "offline",
                NanoLimbo.config.getServerName()
        ), false, new JSONObject(new HashMap<>(2) {{
            put("Authorization", "MoonApi " + NanoLimbo.config.getApiKey());
            put("Content-Type", "application/json");
        }}), new JSONObject(new HashMap<>(2) {{
            put("playerName", playerCondition);
            put("password", password);
        }}));
        assert result != null;

        if (result.getBoolean("success")) {
            return null;
        }
        return result.getString("msg");
    }

    /**
     * 玩家注册
     *
     * @param playerName  玩家名
     * @param playerUuid  玩家uuid
     * @param phoneNumber 手机号
     * @param password    明文密码 online可以置null
     * @return 登录是否成功 成功=null 不成功=提示信息
     */
    public static String playerRegister(String playerUuid, String playerName, String phoneNumber, String password) {
        JSONObject result = HttpUtils.doPost(String.format(
                "%s/user/%s/%s/register",
                NanoLimbo.config.getUrl(),
                NanoLimbo.config.isOnline() ? "online" : "offline",
                NanoLimbo.config.getServerName()
        ), false, new JSONObject(new HashMap<>(2) {{
            put("Authorization", "MoonApi " + NanoLimbo.config.getApiKey());
            put("Content-Type", "application/json");
        }}), new JSONObject(new HashMap<>(4) {{
            put("playerUuid", playerUuid);
            put("playerName", playerName);
            put("phoneNumber", phoneNumber);
            put("password", password);
        }}));
        assert result != null;

        if (result.getBoolean("success")) {
            return null;
        }
        return result.getString("msg");
    }
}
