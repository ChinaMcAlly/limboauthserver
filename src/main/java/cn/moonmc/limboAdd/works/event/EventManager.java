package cn.moonmc.limboAdd.works.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理器
 * @author jja8
 * */
public class EventManager {
    static Map<Class<? extends Event>, List<Lister<? extends Event>>> listerMap = new HashMap<>();

    /**
     * 注册指定事件的监听器
     * */
    public static <T extends Event> void regLister(Class<T> t,Lister<T> lister){
        List<Lister<? extends Event>> listerList = listerMap.computeIfAbsent(t, k -> new ArrayList<>());
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
                Method method = lister.getClass().getMethod("listen",Event.class);
                method.setAccessible(true);
                method.invoke(lister,event);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
