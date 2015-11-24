package net.kimleo.hello.strategy;

import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.annotation.Factory;
import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageResolver;

@Factory(product = MessageStrategy.class)
@Component
public class DefaultStrategyFactory implements StrategyFactory {

    private static StrategyFactory instance;

    private DefaultStrategyFactory() {
    }

    public static StrategyFactory getInstance() {
        if (instance == null)
            instance = new DefaultStrategyFactory();
        return instance;
    }

    public MessageStrategy createStrategy(final Message message, MessageResolver messageResolver) {
        DefaultMessageStrategy strategy = new DefaultMessageStrategy(messageResolver);
        strategy.setMessage(message);
        return strategy;
    }

}
