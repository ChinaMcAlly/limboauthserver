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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Logger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss");
    private static int debugLevel = Level.INFO.getIndex();

    private Logger() {}

    public static int getLevel() {
        return debugLevel;
    }

    public static void info(Object msg, Object... args) {
        print(Level.INFO, msg, null, args);
    }

    public static void debug(Object msg, Object... args) {
        print(Level.DEBUG, msg, null, args);
    }

    public static void warning(Object msg, Object... args) {
        print(Level.WARNING, msg, null, args);
    }

    public static void warning(Object msg, Throwable t, Object... args) {
        print(Level.WARNING, msg, t, args);
    }

    public static void error(Object msg, Object... args) {
        print(Level.ERROR, msg, null, args);
    }

    public static void error(Object msg, Throwable t, Object... args) {
        print(Level.ERROR, msg, t, args);
    }

    public static void print(Level level, Object msg, Throwable t, Object... args) {
        if (debugLevel >= level.getIndex()) {
            String log = String.format("%s: %s%n", getPrefix(level), String.format(msg.toString(), args));
            System.out.print(getFormatLogString(log,level.getColor(),1));
            if (t != null) t.printStackTrace();
        }
    }
    /**
     * @param colour  颜色代号：背景颜色代号(41-46)；前景色代号(31-36)
     * @param type    样式代号：0无；1加粗；3斜体；4下划线
     * @param content 要打印的内容
     */
    private static String getFormatLogString(String content, int colour, int type) {
        boolean hasType = type != 1 && type != 3 && type != 4;
        if (hasType) {
            return String.format("\033[%dm%s\033[0m", colour, content);
        } else {
            return String.format("\033[%d;%dm%s\033[0m", colour, type, content);
        }
    }
    private static String getPrefix(Level level) {
        return String.format("[%s] [%s]", getTime(), level.getDisplay());
    }

    private static String getTime() {
        return LocalTime.now().format(FORMATTER);
    }

    static void setLevel(int level) {
        debugLevel = level;
    }

    public enum Level {

        ERROR("ERROR", 0, 91),
        WARNING("WARNING", 1, 93),
        INFO("INFO", 2, 92),
        DEBUG("DEBUG", 3, 94);

        private final String display;
        private final int index;
        private final int color;

        Level(String display, int index, int color) {
            this.display = display;
            this.index = index;
            this.color = color;
        }

        public String getDisplay() {
            return display;
        }

        public int getIndex() {
            return index;
        }

        public int getColor() {
            return color;
        }
    }
}
