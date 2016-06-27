package net.kimleo.inject.injection;

import net.kimleo.inject.annotation.Component;
import net.kimleo.inject.annotation.Inject;
import net.kimleo.inject.annotation.Qualified;
import net.kimleo.inject.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class FieldInjector implements Injector {

    public static final Logger LOGGER = LoggerFactory.getLogger(FieldInjector.class);
    private final Context context;

    public FieldInjector(Context context) {
        this.context = context;
    }

    @Override
    public <T> T inject(Class<? extends T> clz) {
        try {
            Component an = clz.getAnnotation(Component.class);
            T instance = clz.newInstance();

            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (isInjectable(field) && context.isContextComponent(field.getDeclaringClass())) {
                    setField(instance, field);
                }
            }
            if (an != null && !an.qualifier().isEmpty()) {
                context.addQualifiedInstance(clz, an.qualifier(), instance);
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
        Class<?> finalType = null;
        if (isSpecifiedTypeValid(field)) {
            finalType = getSpecifiedType(field);
        } else {
            finalType = field.getType();
        }

        if (field.getAnnotation(Qualified.class) != null) {
            Qualified qualified = field.getAnnotation(Qualified.class);
            LOGGER.debug("Qualified field {} found with qualifier {}", field, qualified.value());
            field.set(instance, context.getQualifiedInstance(finalType, qualified.value()));
        }
        else {
            field.set(instance,  context.getInstance(finalType));
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
