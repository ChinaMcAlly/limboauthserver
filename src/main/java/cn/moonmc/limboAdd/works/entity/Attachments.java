package cn.moonmc.limboAdd.works.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 代表可以被添加附件
 * @author jja8
 * */
public class Attachments {
    Map<Class<?>,Object> attachmentMap = new HashMap<>();

    /**
     * 设置附件，每个类只能附一个，如果赋了新的对象旧的就失效了
     * */
    public <T> void set(Class<T> attachmentClass, T attachment){
        attachmentMap.put(attachmentClass,attachment);
    }

    /**
     * 获取某附件
     * */
    public <T> T get(Class<T> attachmentClass){
        return (T) attachmentMap.get(attachmentClass);
    }
    
}
