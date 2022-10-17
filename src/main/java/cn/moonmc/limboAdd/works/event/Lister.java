package cn.moonmc.limboAdd.works.event;

/**
 * 事件监听器的抽象类
 * @author jja8
 * */
public interface Lister<T extends Event>{
     void listen(T event);
}
