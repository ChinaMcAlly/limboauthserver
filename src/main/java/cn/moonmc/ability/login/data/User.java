package cn.moonmc.ability.login.data;

import cn.moonmc.ability.login.utils.HashUtils;
import lombok.Data;

import java.util.UUID;

@Data
public class User {
    /**
     * 用户uuid
     */
    final UUID uuid;
    /**
     *用户名
     */
    String name;
    /**
     * 密码
     * */
    Password password;
    /**
     * 手机号
     * */
    long phone;
    /**
     * ip地址
     * */
    String ip;

    User(UUID uuid, String name, String ciphertextPassword, long phone, String ip) {
        this.uuid = uuid;
        this.name = name;
        this.password =ciphertextPassword==null?null:new Password(ciphertextPassword);
        this.phone = phone;
        this.ip = ip;
    }

    /**
     * 代表用户的密码
     * */
    public static class Password {
        final String ciphertext;

        private Password(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        /**
         * 通过明文创建密码
         * */
        public static Password plaintextPassword(String plaintextPassword){
            return new Password(HashUtils.sha256(plaintextPassword));
        }

        /**
         * 检查密码是否正确
         * @param plaintextPassword 明文密码
         * */
        public boolean check(String plaintextPassword){
            return ciphertext.equals(HashUtils.sha256(plaintextPassword));
        }
    }
}
