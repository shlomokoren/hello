package net.kimleo.hello.context;

import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.annotation.Factory;
import net.kimleo.hello.injection.ConstructorInjector;
import net.kimleo.hello.injection.FieldInjector;
import net.kimleo.hello.injection.Injector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

public class ApplicationContext implements Context {


    public static final String SINGLETON_CREATION_METHOD = "getInstance";
    Map<Class, Object> context = new ConcurrentHashMap<>();
    Map<Class, Class> components = new ConcurrentHashMap<>();
    List<Injector> injectors = asList(new ConstructorInjector(this), new FieldInjector(this));

    public ApplicationContext() {
    }

    @Override
    public void addComponents(Class... classes) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        for (Class aClass : classes) {
            addToComponentMappings(aClass);
        }
        initializeContext();
    }

    @Override
    public boolean isComponentClass(Class clz) {
        return clz.getAnnotation(Component.class) != null;
    }

    @Override
    public <T> T getInstance(Class<? extends T> aClass) {
        if (isContextComponent(aClass)) {
            addComponent(aClass);
            return (T) context.get(getRealComponent(aClass));
        }
        return null;
    }

    @Override
    public Class getRealComponent(Class param) {
        return components.get(param);
    }

    @Override
    public boolean isContextComponent(Class param) {
        return components.containsKey(param);
    }

    @Override
    public void addComponent(Class component) {
        addToContextIfItIsAComponent(getRealComponent(component));
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

    private void initializeContext() {
        for (Class aClass : components.keySet()) {
            addToContextIfItIsAComponent(aClass);
        }
    }

    public void addToContextIfItIsAComponent(Class clz) {
        if (!clz.isInterface() && isComponentClass(clz)) {
            context.put(clz, createInstance(clz));
        }
    }

    private Object createInstance(Class clz) {
        if (context.containsKey(clz)) return context.get(clz);
        if (clz.getAnnotation(Factory.class) != null) {
            return createNewFactory(clz);
        }
        for (Injector injector : injectors) {
            Object instance = injector.inject(clz);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    private Object createNewFactory(Class clz) {
        try {
            return clz.getDeclaredMethod(SINGLETON_CREATION_METHOD).invoke(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
