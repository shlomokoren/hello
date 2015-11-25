package net.kimleo.hello.context;

import net.kimleo.hello.annotation.Application;
import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.annotation.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationRunner {

    Map<Class, Object> context = new ConcurrentHashMap<>();
    Map<Class, Class> components = new ConcurrentHashMap<>();

    public void run(Class<?> appClass) throws Exception {
        assert (appClass.getAnnotation(Application.class) != null);
        Class[] classes = ClassUtils.getClasses(appClass.getPackage().getName());
        for (Class aClass : classes) {
            addToComponentMappings(aClass);
        }

        for (Class aClass : components.keySet()) {
            addToContextIfItIsAComponent(aClass);
        }

        Constructor ctor = appClass.getDeclaredConstructors()[0];
        ArrayList<Object> objects = new ArrayList<>();
        for (Class aClass : ctor.getParameterTypes()) {
            objects.add(context.get(components.get(aClass)));
        }
        Object app = ctor.newInstance(objects.toArray());
        appClass.getDeclaredMethod("run").invoke(app);
    }

    private void addToComponentMappings(Class clz) {
        if (clz.getAnnotation(Component.class) != null && !clz.isInterface()) {
            components.put(clz, clz);
            Class superclass = clz.getSuperclass();
            Class[] interfaces = clz.getInterfaces();
            for (Class intf : interfaces) {
                if (intf.getAnnotation(Component.class) != null) {
                    components.put(intf, clz);
                }
            }
        }
    }


    private void addToContextIfItIsAComponent(Class clz)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!clz.isInterface()) {
            context.put(clz, getInstance(clz));
        }
    }

    public <T> T getInstance(Class<? extends T> clz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (context.containsKey(clz)) return (T) context.get(clz);
        if (clz.getAnnotation(Factory.class) != null) {
            return (T) clz.getDeclaredMethod("getInstance").invoke(clz);
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
        return (T) result;
    }

}
