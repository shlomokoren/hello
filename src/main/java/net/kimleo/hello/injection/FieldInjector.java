package net.kimleo.hello.injection;

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
                if (context.isContextComponent(field.getDeclaringClass())) {
                    setField(instance, field);
                }
            }

            return instance;
        } catch (Exception ignored) {
        }
        return null;
    }

    private <T> void setField(T instance, Field field) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(instance, context.getInstance(field.getType()));
        field.setAccessible(accessible);
    }
}
