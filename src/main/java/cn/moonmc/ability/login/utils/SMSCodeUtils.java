package cn.moonmc.ability.login.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.grack.nanojson.JsonWriter;
import ru.nanit.limbo.server.Logger;

import java.util.Map;

/**
 * 短信验证码工具，暂时先写死吧，反正也不会改awa
 * */
public class SMSCodeUtils {
    static final String ak = "LTAI5tHQ45k8nSgVMs9a6pkt";
    static final String sk = "Tz0IwuXNb5L2rftZM9AZkT4Pn3g3XS";
    static final String sign = "沙盒世界视角";
    static final String template_bind = "SMS_230676030";
    static final String template_resetpwd = "SMS_231454649";
    static DefaultAcsClient aliClient = new DefaultAcsClient(DefaultProfile.getProfile("cn-shenzhen",ak, sk));

    /**
     * 会阻塞线程哦，在新线程调用哦
     * */
    public static void sendMessage(String code, Long phone, Type templateType) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", String.valueOf(phone));
        request.putQueryParameter("SignName", sign);
        switch (templateType){
            case bind -> request.putQueryParameter("TemplateCode", template_bind);
            case resetpwd -> request.putQueryParameter("TemplateCode", template_resetpwd);
        }
        request.putQueryParameter("TemplateParam", JsonWriter.string(Map.of("code", code)));
        try {
            CommonResponse response = aliClient.getCommonResponse(request);
            Logger.info("Phone=" + phone + ";template=" + templateType + ";code=" + code + " 's req result => " + response.getData());
        } catch (Exception ignored) {
        }
    }

    public enum Type {
        bind,resetpwd
    }
}
