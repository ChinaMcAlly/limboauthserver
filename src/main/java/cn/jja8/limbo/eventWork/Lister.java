package cn.jja8.limbo.eventWork;

import cn.jja8.limbo.eventWork.event.Event;

public abstract class Lister<T extends Event>{
     Class<T> eventClass;
     public Lister(Class<T> eventClass) {
          this.eventClass = eventClass;
     }
     public abstract void listen(T event);
}
