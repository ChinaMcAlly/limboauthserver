package cn.moonmc.limboauthserver;

import cn.moonmc.limbo.works.menu.InventoryManager;
import cn.moonmc.limboauthserver.listener.OnPlayerJoin;
import cn.moonmc.limboauthserver.listener.OnPlayerQuit;
import ru.nanit.limbo.configuration.LimboConfig;
import ru.nanit.limbo.server.LimboServer;
import ru.nanit.limbo.server.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NanoLimbo {
    public static LimboConfig config;
    public static AtomicInteger windowIdCounter = new AtomicInteger(2);
    public static ExecutorService threadPool;

    public static void main(String[] args) {
        try {
            LimboServer server = new LimboServer();
            server.start();
            config = server.getConfig();
            InventoryManager.run();
            threadPool = Executors.newCachedThreadPool();

            OnPlayerJoin.register();
            OnPlayerQuit.register();
        } catch (Exception e) {
            Logger.error("Cannot start server: "+e);
        }
    }
}
