package net.kimleo.hello.injection;

import net.kimleo.hello.annotation.Inject;
import net.kimleo.hello.context.Context;

import java.lang.reflect.Field;

public class FieldInjector implements Injector {

    private final Context context;

    public FieldInjector(Context context) {

        this.context = context;
    }

    @Override
    public <T> T inject(Class<? extends T> clz) {
        try {
            T instance = clz.newInstance();

            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (isInjectable(field) && context.isContextComponent(field.getDeclaringClass())) {
                    setField(instance, field);
                }
            }

            return instance;
        } catch (Exception ignored) {
        }
        return null;
    }

    private boolean isInjectable(Field field) {
        return field.getAnnotation(Inject.class) != null;
    }

    private <T> void setField(T instance, Field field) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        if (isSpecifiedTypeValid(field)) {
            field.set(instance, context.getInstance(getSpecifiedType(field)));
        } else {
            field.set(instance, context.getInstance(field.getType()));
        }
        field.setAccessible(accessible);
    }

    private boolean isSpecifiedTypeValid(Field field) {
        return isSpecifiedType(field) && field.getType().isAssignableFrom(getSpecifiedType(field));
    }

    private boolean isSpecifiedType(Field field) {
        return isInjectable(field) && getSpecifiedType(field) != Object.class;
    }

    private Class<?> getSpecifiedType(Field field) {
        return field.getAnnotation(Inject.class).value();
    }
}
