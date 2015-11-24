package net.kimleo.hello.strategy;

import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class DefaultMessageStrategyTest {

    private static final String HELLO_WORLD_MESSAGE = "hello world";
    private Message message;
    private MessageResolver messageResolver;
    private DefaultMessageStrategy strategy;
    private PrintStream stream;

    @Before
    public void setUp() throws Exception {
        message = mock(Message.class);
        messageResolver = mock(MessageResolver.class);
        when(message.getPayload()).thenReturn(HELLO_WORLD_MESSAGE);
        strategy = new DefaultMessageStrategy(messageResolver);
        strategy.setMessage(message);
        stream = mock(PrintStream.class);
    }

    @Test
    public void should_use_message_payload() throws Exception {
        strategy.sendMessage(stream);

        verify(message).getPayload();
    }

    @Test
    public void should_print_message_to_stream() throws Exception {
        strategy.sendMessage(System.out);

        verify(messageResolver).resolve(HELLO_WORLD_MESSAGE, System.out);
    }


}