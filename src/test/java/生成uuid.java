import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.nanit.limbo.util.UuidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class 生成uuid {
    static HikariDataSource hikariDataSource;
    public static void main(String[] args) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1/works");
        hikariDataSource = new HikariDataSource(hikariConfig);

        try (Connection connection = hikariDataSource.getConnection()){
            ResultSet resultSet = connection.createStatement().executeQuery("""
SELECT authme.realname,authme.`password`,users.phone_number,authme.ip
FROM authme,users
WHERE authme.realname = users.player_name;
""");
            while (resultSet.next()) {
                user(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void user(String name, String password, String phone, String ip) {
        UUID uuid = UuidUtil.getOfflineModeUuid(name);
        System.out.print(uuid);
        System.out.print(" ");
        System.out.print(name);
        System.out.print(" ");
        System.out.print(password);
        System.out.print(" ");
        System.out.print(phone);
        System.out.print(" ");
        System.out.println(ip);

        try (
                Connection c = hikariDataSource.getConnection();
                PreparedStatement p = c.prepareStatement("insert into user(uuid,name,password,phone,ip) values (?,?,?,?,?)");
        ){
            p.setString(1,uuid.toString());
            p.setString(2,name);
            p.setString(3,password);
            p.setString(4,phone);
            p.setString(5,ip);
            p.executeLargeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
