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

package ru.nanit.limbo.server;

import cn.moonmc.limboAdd.works.event.EventManager;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerConnectEvent;
import cn.moonmc.limboAdd.works.event.playerEvent.PlayerQuitEvent;
import ru.nanit.limbo.connection.ClientConnection;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Connections {

    private final Map<UUID, ClientConnection> connections;

    public Connections() {
        connections = new ConcurrentHashMap<>();
    }

    public Collection<ClientConnection> getAllConnections() {
        return Collections.unmodifiableCollection(connections.values());
    }

    public int getCount() {
        return connections.size();
    }

    public void addConnection(ClientConnection connection) {
        connections.put(connection.getUuid(), connection);
        Logger.info("玩家 %s 加入服务器 (%s) [%s]", connection.getUsername(),
                connection.getAddress(), connection.getClientVersion());
        EventManager.call(new PlayerConnectEvent(connection.getPlayer()));
    }

    public void removeConnection(ClientConnection connection) {
        connections.remove(connection.getUuid());
        Logger.info("玩家 %s 离开服务器", connection.getUsername());
        EventManager.call(new PlayerQuitEvent(connection.getPlayer()));
    }
}
