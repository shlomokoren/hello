package net.kimleo.hello.context;

import java.lang.reflect.InvocationTargetException;

public interface Context {
    void addComponents(Class[] classes) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException;

    boolean isComponentClass(Class intf);

    <T> T getInstance(Class<? extends T> aClass);
}
