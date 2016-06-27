package net.kimleo.inject.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class HelloApplication {

    private static final String STARTUP_METHOD = "runner";
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloApplication.class);

    public void run(Class<?> appClass, String... args) throws Exception {
        assert (appClass.getAnnotation(net.kimleo.inject.annotation.Application.class) != null);
        LOGGER.debug("Initializing application class {}", appClass);
        Context context = new ApplicationContext(ClassUtils.getClasses(appClass.getPackage().getName()));

        context.getInstance(ApplicationRunner.class).run(args);
    }

}
