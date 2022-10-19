package cn.moonmc.ability.login;

import cn.moonmc.ability.AbilityServer;
import cn.moonmc.ability.login.data.UserManager;
import cn.moonmc.ability.login.lister.PlayerJoin;
import cn.moonmc.limboAdd.config.ConfigFile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import ru.nanit.limbo.server.Logger;

import java.sql.Connection;

/**
 * 主要负责加载登录功能
 * */
public class Login {
    @Getter
    final AbilityServer abilityServer;
    @Getter
    HikariDataSource dataSource;
    @Getter
    UserManager userManager;
    @Getter
    PlayerJoin playerJoin;
    public Login(AbilityServer abilityServer){
        this.abilityServer = abilityServer;
        try {
            HikariConfig config = new HikariConfig(new ConfigFile("loginDataBase.properties").getFile().toString());
            dataSource = new HikariDataSource(config);
            try (Connection connection = dataSource.getConnection()){
                connection.createStatement().execute("""
CREATE TABLE IF NOT EXISTS `user`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `id`(`ip` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
""");
            }catch (Throwable e){
                e.printStackTrace();
                Logger.error("创建表失败，无法启动");
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.error("连接池加载失败！登录功能无法启动。");
            return;
        }
        userManager = new UserManager(this);
        playerJoin = new PlayerJoin(this);
    }
}
