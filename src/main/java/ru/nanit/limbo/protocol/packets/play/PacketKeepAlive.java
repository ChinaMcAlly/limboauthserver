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

package ru.nanit.limbo.protocol.packets.play;

import ru.nanit.limbo.protocol.ByteMessage;
import ru.nanit.limbo.protocol.Packet;
import ru.nanit.limbo.protocol.registry.Version;

public class PacketKeepAlive implements Packet {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            msg.writeLong(id);
        } else {
            msg.writeVarInt((int) id);
        }
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            this.id = msg.readLong();
        } else {
            this.id = msg.readVarInt();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
