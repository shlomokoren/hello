package net.kimleo.hello.message;

import net.kimleo.hello.strategy.MessageStrategy;
import net.kimleo.inject.annotation.Component;
import net.kimleo.inject.annotation.Factory;

import java.io.PrintStream;

@Factory(product = Message.class)
@Component
public class DefaultMessageFactory implements MessageFactory {

    private static MessageFactory instance;

    private DefaultMessageFactory() {
    }

    public static MessageFactory getInstance() {
        if (instance == null)
            instance = new DefaultMessageFactory();
        return instance;
    }

    @Override
    public Message create(String message, PrintStream stream) {
        return new Message() {
            private final String payload = message;

            @Override
            public String getPayload() {
                return payload;
            }

            @Override
            public void send(MessageStrategy ms) {
                ms.sendMessage(stream);
            }
        };
    }
}
