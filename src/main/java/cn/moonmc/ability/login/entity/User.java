package cn.moonmc.ability.login.entity;

import cn.moonmc.limboAdd.works.entity.Player;
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
    public User(Player player){
        this.uuid = player.getUUID().toString();
        this.username = player.getName();
        this.password = null;
        this.phone = null;
        this.status = "0";


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
