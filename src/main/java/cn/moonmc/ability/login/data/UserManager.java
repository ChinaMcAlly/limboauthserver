package cn.moonmc.ability.login.data;

import cn.moonmc.ability.login.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * 管理用户持久化
 * */
public class UserManager {
    Login login;
    public UserManager(Login login) throws SQLException {
        this.login = login;
        try (Connection connection = login.getDataSource().getConnection()){
            connection.createStatement().execute("""
CREATE TABLE IF NOT EXISTS `user`  (
  `uuid` char(36) CHARACTER SET utf8mb4 NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4,
  `password` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4,
  `lastLogin` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `id`(`ip` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;
""");
        }
    }

    /**
     * 插入一条用户数据
     * uuid不能为空，其他都可空
     * @return 插入成功返回对象，插入失败返回null
     * */
    public User insert(User u){
        if (u.uuid==null){
            throw new RuntimeException("插入数据时uuid不能为空");
        }
        try (
                Connection c = login.getDataSource().getConnection();
                PreparedStatement p = c.prepareStatement("insert into user(uuid,name,password,phone,ip,lastLogin) values (?,?,?,?,?,?)");
        ){
            p.setString(1,u.uuid.toString());
            p.setString(2,u.name);
            p.setString(3,u.password.getHash());
            p.setString(4,u.phone);
            p.setString(5,u.ip);
            p.setLong(6,u.lastLogin);
            if (p.executeLargeUpdate()>0) {
                return u;
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
                Connection c = login.getDataSource().getConnection();
                PreparedStatement p = c.prepareStatement("update user set name=?,password=?,phone=?,ip=? where uuid=?");
                ){
            p.setString(1,user.name);
            p.setString(2,user.password.getHash());
            p.setString(3,user.phone);
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
                Connection c = login.getDataSource().getConnection();
                PreparedStatement p = c.prepareStatement("select uuid,name,password,phone,ip,lastLogin from user where uuid=?");
                ){
            p.setString(1,uuid.toString());
            try (ResultSet r = p.executeQuery()){
                if (r.next()){
                    return new User(
                            UUID.fromString(r.getString(1)),
                            r.getString(2),
                            r.getString(3),
                            r.getString(4),
                            r.getString(5),
                            r.getLong(6)
                    );
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 通过手机号查询用户
     * */
    public User selectOfPhone(String phone){
        try (
                Connection c = login.getDataSource().getConnection();
                PreparedStatement p = c.prepareStatement("select uuid,name,password,phone,ip,lastLogin from user where phone=?");
        ){
            p.setString(1,phone);
            try (ResultSet r = p.executeQuery()){
                if (r.next()){
                    return new User(
                            UUID.fromString(r.getString(1)),
                            r.getString(2),
                            r.getString(3),
                            r.getString(4),
                            r.getString(5),
                            r.getLong(6)
                    );
                }else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
