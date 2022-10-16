package cn.moonmc.limboauthserver.utils;

import cn.moonmc.limbo.Player;
import cn.moonmc.limboauthserver.NanoLimbo;
import cn.moonmc.limboauthserver.listener.OnPlayerJoin;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * package me.xbigriceh.moonlogin.util.common
 * project MoonLogin
 * Created by @author XBigRiceH on date 2021/12/18
 */
public class PlayerUtils {

    private static final SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd");

    public static String getPlayerCondition(Player p) {
        if (NanoLimbo.config.isOnline()) {
            return p.getUUID().toString();
        } else {
            return p.getName();
        }
    }

    public static OnPlayerJoin.LoginStatus getPlayerMark(Player p) {
        return OnPlayerJoin.loginStatusHashMap.getOrDefault(getPlayerCondition(p), OnPlayerJoin.LoginStatus.LOGGED);
    }

    public static void markPlayerLoginStatus(Player p, OnPlayerJoin.LoginStatus status) {
        if (status == OnPlayerJoin.LoginStatus.LOGGED) {
            OnPlayerJoin.loginStatusHashMap.remove(getPlayerCondition(p));
            return;
        }
        OnPlayerJoin.loginStatusHashMap.put(getPlayerCondition(p), status);
    }

    public static void markPlayerMinuteLimit(Player p) {
        OnPlayerJoin.cooldownLimited.add(getPlayerCondition(p));
        NanoLimbo.threadPool.submit(() -> {
            try {
                Thread.sleep(NanoLimbo.config.getCoolDownTime() * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            OnPlayerJoin.cooldownLimited.remove(getPlayerCondition(p));
        });
    }

    public static void addPlayerDaySendCount(Player p) {
        int countAlready = OnPlayerJoin.dayCount.getOrDefault(getPlayerCondition(p), 0);
        if (countAlready + 2 > NanoLimbo.config.getDayLimit()) {
            String date = simpleDateFormatFull.format(new Date());
            List<String> limitedPlayers = OnPlayerJoin.dayLimited.getOrDefault(date, new ArrayList<>());
            limitedPlayers.add(getPlayerCondition(p));
            OnPlayerJoin.dayLimited.put(date, limitedPlayers);
        } else {
            OnPlayerJoin.dayCount.put(getPlayerCondition(p), countAlready + 1);
        }
    }

    public static CannotSendMessageReason isCanSend(Player p) {
        if (OnPlayerJoin.cooldownLimited.contains(getPlayerCondition(p))) {
            return CannotSendMessageReason.COOLDOWN_LIMITED;
        }
        if (OnPlayerJoin.dayLimited.getOrDefault(simpleDateFormatFull.format(new Date()), new ArrayList<>()).contains(getPlayerCondition(p))) {
            return CannotSendMessageReason.DAY_LIMITED;
        }
        if (OnPlayerJoin.dayLimited.keySet().size() != 1) {
            for (String key : OnPlayerJoin.dayLimited.keySet()) {
                if (!simpleDateFormatFull.format(new Date()).equals(key)) {
                    OnPlayerJoin.dayLimited.remove(key);
                }
            }
        }
        return CannotSendMessageReason.SUCCESS;
    }

    public static void savePlayerCode(Player p, String template, long phoneNumber) {

        String code;
        JSONObject result = HttpUtils.doPost(String.format(
                "%s/3rd/sms/%s/%s",
                NanoLimbo.config.getUrl(),
                template,
                phoneNumber
        ), false, new JSONObject(new HashMap<>(2) {{
            put("Authorization", "MoonApi " + NanoLimbo.config.getApiKey());
            put("Content-Type", "application/json");
        }}), null);

        assert result != null;
        assert result.getBoolean("success");

        code = result.getJSONObject("data").getString("code");
        OnPlayerJoin.code.put(getPlayerCondition(p), code);

        NanoLimbo.threadPool.submit(() -> {
            try {
                Thread.sleep(NanoLimbo.config.getExpiredTime() * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            OnPlayerJoin.code.remove(getPlayerCondition(p));
        });
    }

    public static String getPlayerCode(Player p) {
        return OnPlayerJoin.code.getOrDefault(getPlayerCondition(p), null);
    }

    public static void setPlayerTempPhoneNumber(Player p, long phone) {
        OnPlayerJoin.playerTel.put(getPlayerCondition(p), phone);
    }

    public static long getPlayerTempPhoneNumber(Player p) {
        return OnPlayerJoin.playerTel.getOrDefault(getPlayerCondition(p), null);
    }

    public static void setPlayerTempPassword(Player p, String password) {
        OnPlayerJoin.tempPassword.put(getPlayerCondition(p), password);
    }

    public static String getPlayerTempPassword(Player p) {
        return OnPlayerJoin.tempPassword.getOrDefault(getPlayerCondition(p), null);
    }

    public enum CannotSendMessageReason {
        DAY_LIMITED,
        COOLDOWN_LIMITED,
        SUCCESS,
    }

}
