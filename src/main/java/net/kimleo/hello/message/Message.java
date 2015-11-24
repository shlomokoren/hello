package net.kimleo.hello.message;

import net.kimleo.hello.strategy.MessageStrategy;

public interface Message {
    String getPayload();

    void send(MessageStrategy ms);
}
