package cn.moonmc.ability.login.data;

import cn.moonmc.ability.login.Login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 管理用户持久化
 * */
public class UserManager {
    Login login;
    public UserManager(Login login) {
        this.login = login;
    }

    /**
     * 插入一条用户数据
     * @param uuid uuid不能为空，其他都可空
     * @return 插入成功返回对象，插入失败返回null
     * */
    public User insert(UUID uuid, String name, User.Password password, long phone, String ip){
        if (uuid==null){
            throw new RuntimeException("插入数据时uuid不能为空");
        }
        try (
                PreparedStatement p = login.getDataSource().getConnection().prepareStatement("insert into user(uuid,name,password,phone,ip) values (?,?,?,?,?)");
        ){
            p.setString(1,uuid.toString());
            p.setString(2,name);
            p.setString(3,password.ciphertext);
            p.setLong(4,phone);
            p.setString(5,ip);
            if (p.executeLargeUpdate()>0) {
                return new User(uuid,name,password.ciphertext,phone,ip);
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 更新用户数据到数据库
     * @return 更新成功返回user 失败返回null
     * */
    public User update(User user){
        try (
                PreparedStatement p = login.getDataSource().getConnection().prepareStatement("update user set name=?,password=?,phone=?,ip=? where uuid=?");
                ){
            p.setString(1,user.name);
            p.setString(2,user.password.ciphertext);
            p.setLong(3,user.phone);
            p.setString(4,user.ip);
            p.setString(5,user.uuid.toString());
            if (p.executeUpdate()>0) {
                return user;
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过uuid查询用户
     * */
    public User selectOfUUID(UUID uuid){
        try (
                PreparedStatement p = login.getDataSource().getConnection().prepareStatement("select uuid,name,password,phone,ip from user where uuid=?");
                ){
            p.setString(1,uuid.toString());
            ResultSet r = p.executeQuery();
            if (r.next()){
                return new User(
                        UUID.fromString(r.getString(1)),
                        r.getString(2),
                        r.getString(3),
                        r.getLong(4),
                        r.getString(5)
                );
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
