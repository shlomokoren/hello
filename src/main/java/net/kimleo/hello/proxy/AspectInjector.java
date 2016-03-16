package net.kimleo.hello.proxy;

import net.kimleo.hello.aspect.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AspectInjector implements InvocationHandler {

    private final Class<? extends Transform> transformer;

    public AspectInjector(Class<? extends Transform> transformer) {
        this.transformer = transformer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Before) {
                if (((Before) annotation).runner().equals(transformer)) {
                    Object arg = args[0];
                    arg =  transformer.newInstance().apply(arg);
                    return method.invoke(proxy, arg);
                }
            }
        }
        return null;
    }
}
