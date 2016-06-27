package net.kimleo.inject.context;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kimleo.inject.annotation.*;
import net.kimleo.inject.injection.ConstructorInjector;
import net.kimleo.inject.injection.FieldInjector;
import net.kimleo.inject.injection.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

public class ApplicationContext implements Context {


    private static final String SINGLETON_CREATION_METHOD = "getInstance";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);
    private Map<Class, Object> context = new ConcurrentHashMap<>();
    private Map<Class, Class> components = new ConcurrentHashMap<>();
    private List<Injector> injectors = asList(new ConstructorInjector(this), new FieldInjector(this));

    private Multimap<Class, QualifiedComponent> qualifiedContext = HashMultimap.create();

    private Map<Class, Method> configurations = new ConcurrentHashMap<>();

    public ApplicationContext() {
    }

    @Override
    public void addComponents(Class... classes) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        for (Class aClass : classes) {
            if (aClass.getAnnotation(Config.class) != null) {
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if (method.getAnnotation(Bean.class) != null) {
                        Class<?> type = method.getReturnType();
                        configurations.put(type, method);
                        components.put(type, type);
                    }
                }
            }
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

    @Override
    public <T> void addQualifiedInstance(Class<? extends T> clz, String qualifier, T instance) {
        qualifiedContext.put(clz, new QualifiedComponent(qualifier, instance));
    }

    @Override
    public Object getQualifiedInstance(Class<?> finalType, String qualified) {
        Collection<QualifiedComponent> components = qualifiedContext.get(finalType);
        for (QualifiedComponent component : components) {
            if (component.getQualifier().equals(qualified)) {
                return component.getObject();
            }
        }
        return null;
    }

    private void addToComponentMappings(Class clz) {
        if (isComponentClass(clz) && !clz.isInterface()) {
            LOGGER.debug("Component {} found", clz);
            components.put(clz, clz);
            Class[] interfaces = clz.getInterfaces();
            for (Class theInterface : interfaces) {
                if (isComponentClass(theInterface)) {
                    LOGGER.debug("Components {} registered for interface {}", clz, theInterface);
                    components.put(theInterface, clz);
                }
            }
        }
    }

    private void initializeContext() {
        components.keySet().forEach(this::addToContextIfItIsAComponent);
    }

    public void addToContextIfItIsAComponent(Class clz) {
        if (configurations.containsKey(clz) || !clz.isInterface() && isComponentClass(clz)) {
            context.put(clz, createInstance(clz));
        }
    }

    private Object createInstance(Class clz) {
        if (context.containsKey(clz)) return context.get(clz);
        else if (configurations.containsKey(clz)) {
            return createFromConfiguration(clz);
        }
        if (clz.getAnnotation(Factory.class) != null) {
            Object object = createNewFactory(clz);
            Factory annotation = (Factory) clz.getAnnotation(Factory.class);
            if (annotation.qualifier().isEmpty()) {
                qualifiedContext.put(clz, new QualifiedComponent("", object));
            } else {
                qualifiedContext.put(clz, new QualifiedComponent(annotation.qualifier(), object));
            }
            return object;
        }
        for (Injector injector : injectors) {
            Object instance = injector.inject(clz);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    private Object createFromConfiguration(Class clz) {
        Method method = configurations.get(clz);
        Bean annotation = method.getAnnotation(Bean.class);
        Object configuration = createInstance(method.getDeclaringClass());
        ArrayList<Object> params = new ArrayList<>();
        for (Class<?> paramType : method.getParameterTypes()) {
            params.add(getInstance(paramType));
        }
        Object[] paramArray = params.toArray();
        try {
            Object object = method.invoke(configuration, paramArray);
            qualifiedContext.put(clz, new QualifiedComponent(method.getName(), object));
            if (annotation.qualifier().isEmpty()) {
                qualifiedContext.put(clz, new QualifiedComponent("", object));
            } else {
                qualifiedContext.put(clz, new QualifiedComponent(annotation.qualifier(), object));
            }
            return object;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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
