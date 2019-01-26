package com.fastsky.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: BeanRegister
 * Description: 服务注册
 *
 * @author: starryfei
 * @date: 2019-01-26 23:13
 **/
public class BeanRegister {
    private static Map<String,Class<?>> classMap ;

    private static BeanRegister beanRegister = null;

    public synchronized static BeanRegister getInstance(){
       if (beanRegister == null) {
           beanRegister = new BeanRegister();
       }
       return beanRegister;
    }

    /**
     * 注册服务
     * @param clazz
     */
    public void registerBeans(Class<?> clazz) {
        if (classMap == null) {
            classMap = new ConcurrentHashMap<>(16);
        }
        for (Class<?> cla : clazz.getInterfaces()) {
            System.out.println(cla.getName());
            classMap.put(cla.getName(), clazz);
        }

    }

    public Class<?> getBean(String className) {
        return classMap.get(className);
    }
}
