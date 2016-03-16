package net.kimleo.hello.proxy;

import net.kimleo.hello.aspect.Before;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class AspectInjectorTest implements Transform<Object> {

    @Before(runner =  AspectInjectorTest.class)
    public void transform(Object v)
    {
        assertNotNull(v);
    }

    @Override
    public Object apply(Object self) {
        System.out.println(self);
        return self;
    }

    @Test
    public void testBefore() throws Exception {
        Object o = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{Transform.class},
                new AspectInjector(AspectInjectorTest.class));
        o.toString();
    }
}