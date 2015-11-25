package net.kimleo.hello.injection;

import net.kimleo.hello.annotation.Construct;
import net.kimleo.hello.context.ApplicationContext;
import net.kimleo.hello.context.Context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static java.util.Arrays.asList;

public class ConstructorInjector implements Injector {

    private final Context context;

    public ConstructorInjector(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> T inject(Class<? extends T> clz) {
        Constructor[] constructors = clz.getDeclaredConstructors();
        Constructor ctor = getInjectedConstructor(constructors);
        try {
            if (ctor != null) {
                Class[] parameterTypes = ctor.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length == 0) return null;
                ArrayList<Object> objects = new ArrayList<>();
                for (Class param : parameterTypes) {
                    context.addComponent(param);
                    objects.add(context.getInstance(param));
                }
                return (T) ctor.newInstance(objects.toArray());
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Constructor getInjectedConstructor(Constructor[] constructors) {
        for (Constructor ctor : constructors) {
            if (!isConstructable(ctor)) continue;
            Class[] parameterTypes = ctor.getParameterTypes();
            if (asList(parameterTypes).stream().allMatch(context::isContextComponent)) {
                return ctor;
            }
        }
        return null;
    }

    private boolean isConstructable(Constructor ctor) {
        return ctor.getAnnotation(Construct.class) != null;
    }

}
