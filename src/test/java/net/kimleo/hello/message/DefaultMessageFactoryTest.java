package net.kimleo.hello.message;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DefaultMessageFactoryTest {

    private MessageFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = DefaultMessageFactory.getInstance();
    }

    @Test
    public void should_get_same_instance() throws Exception {
        assertEquals(factory, DefaultMessageFactory.getInstance());
    }

    @Test
    public void should_create_message() throws Exception {
        assertThat(factory.create("hello world", System.out), is(Message.class));
    }
}