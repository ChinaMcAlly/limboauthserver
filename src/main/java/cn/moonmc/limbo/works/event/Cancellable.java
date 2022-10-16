package cn.moonmc.limbo.works.event;

/**
 * 可取消事件
 *
 * @author jja8
 */
public interface Cancellable extends Event {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
