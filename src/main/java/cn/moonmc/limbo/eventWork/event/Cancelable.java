package cn.moonmc.limbo.eventWork.event;

/**
 * 可取消事件
 * @author jja8
 * */
public interface Cancelable extends Event{
    boolean isCancel();

    void setCancel(boolean cancel);
}