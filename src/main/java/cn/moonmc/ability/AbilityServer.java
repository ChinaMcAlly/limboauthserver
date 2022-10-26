package cn.moonmc.ability;

import cn.moonmc.ability.login.Login;
import cn.moonmc.ability.chooseServer.ChooseServer;
import lombok.Getter;
import ru.nanit.limbo.server.LimboServer;

public class AbilityServer {
    /**
     * 登录功能
     * */
    @Getter
    Login login;
    /**
     * 选择服务器功能
     * */
    @Getter
    ChooseServer chooseServer;
    @Getter
    private final LimboServer server;
    public AbilityServer(LimboServer server) {
        this.server = server;
        chooseServer = new ChooseServer(this);
        login = new Login(this);
    }
}
