package cn.moonmc.jja8.limbo.eventWork;

import cn.moonmc.jja8.limbo.eventWork.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    static Map<Class<? extends Event>, List<Lister<? extends Event>>> listerMap = new HashMap<>();

    /**
     * 注册指定事件的监听器
     * */
    public static void regLister(Lister<?> lister){
        List<Lister<? extends Event>> listerList = listerMap.computeIfAbsent(lister.eventClass, k -> new ArrayList<>());
        listerList.add(lister);
    }
    /**
     * 通知事件到所有监听器
     * */
    public static void call(Event event){
        List<Lister<? extends Event>> listerList = listerMap.get(event.getClass());
        if (listerList==null){
            return;
        }
        for (Lister<? extends Event> lister : listerList) {
            try {
                Method method = lister.getClass().getMethod("listen",event.getClass());
                method.setAccessible(true);
                method.invoke(lister,event);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
