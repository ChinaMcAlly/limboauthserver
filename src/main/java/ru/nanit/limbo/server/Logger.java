package ru.nanit.limbo.server;

import lombok.extern.slf4j.Slf4j;

/**
 * @author admin
 */
@Slf4j
public final class Logger {
    private Logger() {}

    public static void info(String msg) {
        log.info(msg);
    }
    public static void debug(String msg) {
        log.debug(msg);
    }
    public static void warning(String msg) {
        log.warn(msg);
    }
    public static void error(String msg) {
        log.error(msg);
    }

}
