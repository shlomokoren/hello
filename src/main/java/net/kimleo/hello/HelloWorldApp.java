package net.kimleo.hello;

import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageFactory;
import net.kimleo.hello.message.MessageResolver;
import net.kimleo.hello.strategy.MessageStrategy;
import net.kimleo.hello.strategy.StrategyFactory;
import net.kimleo.inject.annotation.Application;
import net.kimleo.inject.annotation.Bean;
import net.kimleo.inject.annotation.Config;
import net.kimleo.inject.application.ApplicationBoot;
import net.kimleo.inject.application.Runner;

@Application
@Config
public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        new ApplicationBoot().run(HelloWorldApp.class);
    }

    @Bean
    public Runner runner(MessageFactory messageFactory,
                         MessageResolver messageResolver,
                         StrategyFactory strategyFactory) {
        return (String... args) -> {
            Message messageBody = messageFactory.create("hello world!", System.out);
            MessageStrategy strategy = strategyFactory.createStrategy(messageBody, messageResolver);
            messageBody.send(strategy);
        };
    }
}
