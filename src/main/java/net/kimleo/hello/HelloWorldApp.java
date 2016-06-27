package net.kimleo.hello;

import net.kimleo.inject.annotation.Application;
import net.kimleo.inject.annotation.Bean;
import net.kimleo.inject.annotation.Config;
import net.kimleo.inject.context.ApplicationRunner;
import net.kimleo.inject.context.HelloApplication;
import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageFactory;
import net.kimleo.hello.message.MessageResolver;
import net.kimleo.hello.strategy.MessageStrategy;
import net.kimleo.hello.strategy.StrategyFactory;

@Application
@Config
public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        new HelloApplication().run(HelloWorldApp.class);
    }

    @Bean
    public ApplicationRunner runner(MessageFactory messageFactory,
                                    MessageResolver messageResolver,
                                    StrategyFactory strategyFactory) {
        return (String... args) -> {
            Message messageBody = messageFactory.create("hello world!", System.out);
            MessageStrategy strategy = strategyFactory.createStrategy(messageBody, messageResolver);
            messageBody.send(strategy);
        };
    }
}
