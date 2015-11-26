package net.kimleo.hello.strategy;

import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;

import java.io.PrintStream;

public class DefaultMessageStrategy implements MessageStrategy {

    private final MessageResolver<PrintStream> messageResolver;
    private final Message message;

    public DefaultMessageStrategy(MessageResolver messageResolver, Message message) {
        this.messageResolver = messageResolver;
        this.message = message;
    }

    public void sendMessage(PrintStream stream) {
        String message = this.message.getPayload();
        messageResolver.resolve(message, stream);
    }
}
