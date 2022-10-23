package cn.moonmc.limboAdd.works.message;

import java.util.Map;

/**
 * 鼠标点击执行命令
 * */
public class ClickEventRunCommand implements ClickEvent{
    String command;

    public ClickEventRunCommand(String command) {
        this.command = command;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("action","run_command","value",command);
    }
}
