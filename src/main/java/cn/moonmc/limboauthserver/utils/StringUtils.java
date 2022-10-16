package cn.moonmc.limboauthserver.utils;

import cn.moonmc.limboauthserver.NanoLimbo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * package cn.moonmc.limboauthserver.utils
 * project limboauthserver
 * Created by @author XuHang on date 2022/10/16
 */
public class StringUtils {
    private static final Pattern p = Pattern.compile(NanoLimbo.config.getPhoneRegexp());

    public static boolean isChinaPhoneLegal(String str) {
        try {
            Matcher m = p.matcher(str);
            return m.matches();
        } catch (Exception ignored) {
            return false;
        }
    }
}
