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

package cn.moonmc.limboAdd;

import cn.moonmc.ability.AbilityServer;
import cn.moonmc.limboAdd.works.menu.InventoryManager;
import lombok.Getter;
import ru.nanit.limbo.server.LimboServer;
public final class AddServer {
    @Getter
    LimboServer server;
    @Getter
    InventoryManager inventoryManager;
    public AddServer(LimboServer server) {
        this.server = server;
        this.inventoryManager = new InventoryManager(this);
        new AbilityServer(server);
    }
}
