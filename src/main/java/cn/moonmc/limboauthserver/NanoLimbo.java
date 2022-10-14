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

package cn.moonmc.limboauthserver;

import cn.moonmc.text.Text;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.nanit.limbo.server.LimboServer;
import ru.nanit.limbo.server.Logger;

@Component
@Order(0)
public final class NanoLimbo implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            new LimboServer().start();
        } catch (Exception e) {
            Logger.error("Cannot start server: "+e);
        }
        Text.main();
    }
}
