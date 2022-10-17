package cn.moonmc.ability.login;

import cn.moonmc.ability.AbilityServer;
import cn.moonmc.ability.login.data.UserManager;
import cn.moonmc.limboAdd.config.ConfigFile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import ru.nanit.limbo.server.Logger;

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
    public Login(AbilityServer abilityServer){
        this.abilityServer = abilityServer;
        try {
            HikariConfig config = new HikariConfig(new ConfigFile("loginDataBase.properties").getFile().toString());
            dataSource = new HikariDataSource(config);
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.error("连接池加载失败！登录功能无法启动。");
            return;
        }
        userManager = new UserManager(this);
    }
}
