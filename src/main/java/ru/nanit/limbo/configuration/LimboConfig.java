/*
 * Copyright (C) 2020 Nan1t
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.nanit.limbo.configuration;

import lombok.Data;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import ru.nanit.limbo.server.data.BossBar;
import ru.nanit.limbo.server.data.InfoForwarding;
import ru.nanit.limbo.server.data.PingData;
import ru.nanit.limbo.server.data.Title;
import ru.nanit.limbo.util.Colors;
import ru.nanit.limbo.world.Location;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public final class LimboConfig {

    private final Path root;
    private String phoneRegexp;

    private SocketAddress address;
    private int maxPlayers;
    private PingData pingData;

    private String dimensionType;
    private Location spawnPosition;
    private int gameMode;

    private boolean online;
    private boolean useBrandName;
    private boolean useJoinMessage;
    private boolean useBossBar;
    private boolean useTitle;
    private boolean usePlayerList;
    private boolean useHeaderAndFooter;

    private String brandName;
    private String joinMessage;
    private BossBar bossBar;
    private Title title;

    private String playerListUsername;
    private String playerListHeader;
    private String playerListFooter;

    private InfoForwarding infoForwarding;
    private long readTimeout;
    private int debugLevel;

    private boolean useEpoll;
    private int bossGroupSize;
    private int workerGroupSize;

    private String apiKey;
    private String url;
    private String serverName;

    private int dayLimit;
    private int telLimit;
    private int coolDownTime;
    private int expiredTime;


    public LimboConfig(Path root) {
        this.root = root;
    }

    public void load() throws Exception {
        ConfigurationOptions options = ConfigurationOptions.defaults().serializers(getSerializers());
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .source(this::getReader)
                .defaultOptions(options)
                .build();

        ConfigurationNode conf = loader.load();

        address = conf.node("bind").get(SocketAddress.class);
        maxPlayers = conf.node("maxPlayers").getInt();
        pingData = conf.node("ping").get(PingData.class);
        dimensionType = conf.node("dimension").getString();
        if (dimensionType.equalsIgnoreCase("nether")) {
            dimensionType = "the_nether";
        }
        if (dimensionType.equalsIgnoreCase("end")) {
            dimensionType = "the_end";
        }
        spawnPosition = conf.node("spawnPosition").get(Location.class);
        gameMode = conf.node("gameMode").getInt();
        online = conf.node("online", "true").getBoolean();
        useBrandName = conf.node("brandName", "enable").getBoolean();
        useJoinMessage = conf.node("joinMessage", "enable").getBoolean();
        useBossBar = conf.node("bossBar", "enable").getBoolean();
        useTitle = conf.node("title", "enable").getBoolean();
        usePlayerList = conf.node("playerList", "enable").getBoolean();
        playerListUsername = conf.node("playerList", "username").getString();
        useHeaderAndFooter = conf.node("headerAndFooter", "enable").getBoolean();

        apiKey = conf.node("api", "key").getString();
        url = conf.node("api", "url").getString();
        serverName = conf.node("api", "server-name").getString();

        dayLimit = conf.node("auth", "day-limit").getInt();
        telLimit = conf.node("auth", "tel-limit").getInt();
        coolDownTime = conf.node("auth", "cool-down-time").getInt();
        expiredTime = conf.node("auth", "exp-time").getInt();

        phoneRegexp = conf.node("auth", "phone-reg-exp").getString();

        if (useBrandName)
            brandName = conf.node("brandName", "content").getString();

        if (useJoinMessage)
            joinMessage = Colors.of(conf.node("joinMessage", "text").getString(""));

        if (useBossBar)
            bossBar = conf.node("bossBar").get(BossBar.class);

        if (useTitle)
            title = conf.node("title").get(Title.class);

        if (useHeaderAndFooter) {
            playerListHeader = Colors.of(conf.node("headerAndFooter", "header").getString());
            playerListFooter = Colors.of(conf.node("headerAndFooter", "footer").getString());
        }

        infoForwarding = conf.node("infoForwarding").get(InfoForwarding.class);
        readTimeout = conf.node("readTimeout").getLong();
        debugLevel = conf.node("debugLevel").getInt();

        useEpoll = conf.node("netty", "useEpoll").getBoolean(true);
        bossGroupSize = conf.node("netty", "threads", "bossGroup").getInt(1);
        workerGroupSize = conf.node("netty", "threads", "workerGroup").getInt(4);
    }

    private BufferedReader getReader() throws IOException {
        String name = "settings.yml";
        Path filePath = Paths.get(root.toString(), name);

        if (!Files.exists(filePath)) {
            InputStream stream = getClass().getResourceAsStream( "/" + name);

            if (stream == null)
                throw new FileNotFoundException("Cannot find settings resource file");

            Files.copy(stream, filePath);
        }

        return Files.newBufferedReader(filePath);
    }

    private TypeSerializerCollection getSerializers() {
        return TypeSerializerCollection.builder()
                .register(SocketAddress.class, new SocketAddressSerializer())
                .register(InfoForwarding.class, new InfoForwarding.Serializer())
                .register(PingData.class, new PingData.Serializer())
                .register(BossBar.class, new BossBar.Serializer())
                .register(Title.class, new Title.Serializer())
                .register(Location.class, new Location.Serializer())
                .build();
    }

}
