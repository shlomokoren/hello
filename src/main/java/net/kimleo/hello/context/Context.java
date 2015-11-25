package net.kimleo.hello.context;

import java.lang.reflect.InvocationTargetException;

public interface Context {
    void addComponents(Class[] classes) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException;

    boolean isComponentClass(Class clz);

    <T> T getInstance(Class<? extends T> aClass);

    Class getRealComponent(Class param);

    boolean isContextComponent(Class param);

    void addComponent(Class component) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
