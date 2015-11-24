package net.kimleo.hello.message;

import net.kimleo.hello.text.TokenList;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PrintMessageResolverTest {

    public static final String HELLO_WORLD_MESSAGE = "hello world!";
    public static final String HELLO = "hello";
    public static final String WORLD = "world!";
    private MessageResolver resolver;
    private MessageParser parser;
    private ConcreteTokenVisitor visitor;
    private TokenList tokens;

    @Before
    public void setUp() throws Exception {
        visitor = mock(ConcreteTokenVisitor.class);
        parser = mock(MessageParser.class);
        tokens = mock(TokenList.class);
        when(visitor.withStream(any())).thenReturn(visitor);
        when(parser.parse(anyString())).thenReturn(tokens);
        resolver = new PrintMessageResolver(parser, visitor);
    }

    @Test
    public void should_resolve_message() throws Exception {
        resolver.resolve(HELLO_WORLD_MESSAGE, System.out);

        verify(parser).parse(HELLO_WORLD_MESSAGE);
        verify(tokens).accept(visitor);
    }
}