package net.kimleo.hello;

import net.kimleo.hello.message.*;
import net.kimleo.hello.strategy.DefaultMessageStrategy;
import net.kimleo.hello.strategy.StrategyFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HelloWorldAppTest {

    public static final String HELLO_WORLD_MESSAGE = "hello world!";
    public Message message;


    private StrategyFactory strategyFactory;
    private HelloWorldApp app;
    private MessageResolver printMessageResolver;
    private MessageFactory messageFactory;
    private PrintStream stream;

    @Before
    public void setUp() throws Exception {
        strategyFactory = mock(StrategyFactory.class);
        printMessageResolver = mock(PrintMessageResolver.class);
        messageFactory = mock(MessageFactory.class);
        stream = mock(PrintStream.class);
        DefaultMessageStrategy strategy = new DefaultMessageStrategy(printMessageResolver);
        message = DefaultMessageFactory.getInstance().create(HELLO_WORLD_MESSAGE, stream);
        strategy.setMessage(message);
        when(strategyFactory.createStrategy(any(Message.class), eq(printMessageResolver)))
                .thenReturn(strategy);

        when(messageFactory.create(anyString(), any(PrintStream.class))).thenReturn(message);

        app = new HelloWorldApp(strategyFactory, printMessageResolver, messageFactory);
    }

    @Test
    public void should_integrated_successfully() throws Exception {
        app.say(message);

        verify(strategyFactory).createStrategy(message, printMessageResolver);
        verify(printMessageResolver).resolve(HELLO_WORLD_MESSAGE, stream);
        assertEquals(message.getPayload(), HELLO_WORLD_MESSAGE);
    }

    @Test
    public void should_run_run() throws Exception {
        app.run();

        verify(messageFactory).create(HELLO_WORLD_MESSAGE, System.out);
    }

    @Test
    public void should_run_main() throws Exception {
        HelloWorldApp.main(null);
    }
}