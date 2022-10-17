package cn.moonmc.ability;

import cn.moonmc.ability.login.Login;
import lombok.Getter;
import ru.nanit.limbo.server.LimboServer;

public class AbilityServer {
    @Getter
    private final LimboServer server;
    public AbilityServer(LimboServer server) {
        this.server = server;

        new Login(this);
    }
}
