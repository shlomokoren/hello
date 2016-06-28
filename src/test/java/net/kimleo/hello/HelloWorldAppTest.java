package net.kimleo.hello;

import net.kimleo.hello.message.*;
import net.kimleo.hello.strategy.DefaultMessageStrategy;
import net.kimleo.hello.strategy.StrategyFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class HelloWorldAppTest {

    public static final String HELLO_WORLD_MESSAGE = "hello world!";
    public Message message;


    private StrategyFactory strategyFactory;
    private HelloWorldApp app;
    private MessageResolver printMessageResolver;
    private MessageFactory messageFactory;
    private DefaultMessageStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategyFactory = mock(StrategyFactory.class);
        printMessageResolver = mock(PrintMessageResolver.class);
        messageFactory = mock(MessageFactory.class);
        message = mock(Message.class);
        strategy = new DefaultMessageStrategy(printMessageResolver, message);
        when(strategyFactory.createStrategy(any(Message.class), eq(printMessageResolver)))
                .thenReturn(strategy);

        when(messageFactory.create(anyString(), any(PrintStream.class))).thenReturn(message);

        app = new HelloWorldApp();
    }

    @Test
    public void should_run_run() throws Exception {
        app.runner(messageFactory, printMessageResolver, strategyFactory).run();

        verify(messageFactory).create(anyString(), any(PrintStream.class));
        verify(strategyFactory).createStrategy(message, printMessageResolver);
        verify(message).send(strategy);
    }

    @Test
    public void should_run_main() throws Exception {
        HelloWorldApp.main(null);
    }
}