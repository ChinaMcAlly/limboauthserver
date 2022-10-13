package cn.moonmc.limbo.eventWork;

import cn.moonmc.limbo.eventWork.event.Event;

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
