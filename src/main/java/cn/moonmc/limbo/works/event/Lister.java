package cn.moonmc.limbo.works.event;

/**
 * 事件监听器的抽象类
 * @author jja8
 * */
public abstract class Lister<T extends Event>{
     Class<T> eventClass;
     public Lister(Class<T> eventClass) {
          this.eventClass = eventClass;
     }
     public abstract void listen(T event);
}
