package net.kimleo.hello.strategy;

import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultStrategyFactoryTest {

    public static final String HELLO_WORLD_MESSAGE = "hello world!";
    private StrategyFactory factory;
    private Message message;
    private MessageResolver messageResolver;

    @Before
    public void setUp() throws Exception {
        factory = DefaultStrategyFactory.getInstance();
        message = mock(Message.class);
        messageResolver = mock(MessageResolver.class);
        when(message.getPayload()).thenReturn(HELLO_WORLD_MESSAGE);
    }

    @Test
    public void should_get_same_instance() throws Exception {
        assertThat(factory, is(DefaultStrategyFactory.getInstance()));
    }

    @Test
    public void should_create_message_strategy() throws Exception {
        MessageStrategy strategy = factory.createStrategy(message, messageResolver);

        assertNotNull(strategy);
    }

}