package net.kimleo.inject.context;

import net.kimleo.inject.annotation.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ApplicationRunner {

    public static final String STARTUP_METHOD = "run";
    public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunner.class);
    Context context = new ApplicationContext();

    public void run(Class<?> appClass) throws Exception {
        assert (appClass.getAnnotation(Application.class) != null);
        LOGGER.debug("Initializing application class {}", appClass);

        context.addComponents(ClassUtils.getClasses(appClass.getPackage().getName()));

        Constructor ctor = appClass.getDeclaredConstructors()[0];
        ArrayList<Object> objects = new ArrayList<>();
        for (Class aClass : ctor.getParameterTypes()) {
            objects.add(context.getInstance(aClass));
        }

        appClass.getDeclaredMethod(STARTUP_METHOD).invoke(ctor.newInstance(objects.toArray()));
    }

}
