package net.kimleo.hello.strategy;

import net.kimleo.inject.annotation.Component;
import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;

@Component
public interface StrategyFactory {
    MessageStrategy createStrategy(Message message, MessageResolver messageResolver);
}
