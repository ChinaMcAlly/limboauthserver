package cn.moonmc.limboauthserver.entity;

import lombok.Data;

/**
 * @author CNLuminous
 */
@Data
public class User {
    final String uuid;
    String username;
    String password;
    String phone;
    String status;

    public User(String uuid, String username, String password, String phone, String status) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
