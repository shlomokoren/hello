package net.kimleo.hello.context;

import net.kimleo.hello.annotation.Application;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ApplicationRunner {

    public static final String STARTUP_METHOD = "run";
    Context context = new ApplicationContext();

    public void run(Class<?> appClass) throws Exception {
        assert (appClass.getAnnotation(Application.class) != null);

        context.addComponents(ClassUtils.getClasses(appClass.getPackage().getName()));

        Constructor ctor = appClass.getDeclaredConstructors()[0];
        ArrayList<Object> objects = new ArrayList<>();
        for (Class aClass : ctor.getParameterTypes()) {
            objects.add(context.getInstance(aClass));
        }

        appClass.getDeclaredMethod(STARTUP_METHOD).invoke(ctor.newInstance(objects.toArray()));
    }

}
