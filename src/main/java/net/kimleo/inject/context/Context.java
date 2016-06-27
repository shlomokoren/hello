package net.kimleo.inject.context;

import net.kimleo.inject.annotation.Qualified;

import java.lang.reflect.InvocationTargetException;

public interface Context {
    <T> T getInstance(Class<? extends T> aClass);

    boolean isContextComponent(Class param);

    void addComponent(Class component) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    <T> void addQualifiedInstance(Class<? extends T> clz, String qualifier, T instance);

    Object getQualifiedInstance(Class<?> finalType, String qualified);
}
