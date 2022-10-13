package ru.nanit.limbo.server.commands;

import ru.nanit.limbo.server.Command;
import ru.nanit.limbo.server.Logger;

public class CmdMem implements Command {

    @Override
    public void execute() {
        Runtime runtime = Runtime.getRuntime();
        long mb = 1024 * 1024;
        long used = (runtime.totalMemory() - runtime.freeMemory()) / mb;
        long total = runtime.totalMemory() / mb;
        long free = runtime.freeMemory() / mb;
        long max = runtime.maxMemory() / mb;

        Logger.info("内存使用情况:");
        Logger.info("使用中: "+used+" MB");
        Logger.info("已申请: "+total+" MB");
        Logger.info("剩余: "+free+" MB");
        Logger.info("最大: "+max+" MB");
    }

    @Override
    public String description() {
        return "显示内存使用情况";
    }
}
