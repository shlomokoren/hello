package net.kimleo.hello.strategy;

import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;

import java.io.PrintStream;

@Component
public class DefaultMessageStrategy implements MessageStrategy {

    private final MessageResolver messageResolver;
    private Message message;

    public DefaultMessageStrategy(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    public void sendMessage(PrintStream stream) {
        String message = this.message.getPayload();
        messageResolver.resolve(message, stream);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
