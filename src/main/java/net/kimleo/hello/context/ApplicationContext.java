package net.kimleo.hello.context;

import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.annotation.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext implements Context {


    Map<Class, Object> context = new ConcurrentHashMap<>();
    Map<Class, Class> components = new ConcurrentHashMap<>();

    @Override
    public void addComponents(Class[] classes) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        for (Class aClass : classes) {
            addToComponentMappings(aClass);
        }

        initializeContext();
    }

    @Override
    public boolean isComponentClass(Class intf) {
        return intf.getAnnotation(Component.class) != null;
    }

    @Override
    public <T> T getInstance(Class<? extends T> aClass) {
        return (T) context.get(components.get(aClass));
    }

    private void addToComponentMappings(Class clz) {
        if (isComponentClass(clz) && !clz.isInterface()) {
            components.put(clz, clz);
            Class[] interfaces = clz.getInterfaces();
            for (Class theInterface : interfaces) {
                if (isComponentClass(theInterface)) {
                    components.put(theInterface, clz);
                }
            }
        }
    }

    private void initializeContext() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Class aClass : components.keySet()) {
            addToContextIfItIsAComponent(aClass);
        }
    }

    private void addToContextIfItIsAComponent(Class clz)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!clz.isInterface() && isComponentClass(clz)) {
            context.put(clz, createInstance(clz));
        }
    }

    private Object createInstance(Class clz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (context.containsKey(clz)) return context.get(clz);
        if (clz.getAnnotation(Factory.class) != null) {
            return clz.getDeclaredMethod("getInstance").invoke(clz);
        }
        Object result = null;
        Constructor[] constructors = clz.getDeclaredConstructors();
        for (Constructor ctor : constructors) {
            Class[] parameterTypes = ctor.getParameterTypes();
            ArrayList<Object> objects = new ArrayList<>();
            for (Class param : parameterTypes) {
                addToContextIfItIsAComponent(components.get(param));
                objects.add(context.get(components.get(param)));
            }
            result = ctor.newInstance(objects.toArray());
        }
        return result;
    }
}
