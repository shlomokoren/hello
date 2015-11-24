package net.kimleo.hello;

import net.kimleo.hello.annotation.Application;
import net.kimleo.hello.context.ApplicationRunner;
import net.kimleo.hello.message.Message;
import net.kimleo.hello.message.MessageFactory;
import net.kimleo.hello.message.MessageResolver;
import net.kimleo.hello.strategy.MessageStrategy;
import net.kimleo.hello.strategy.StrategyFactory;

@Application
public class HelloWorldApp {

    private final StrategyFactory strategyFactory;
    private final MessageResolver messageResolver;
    private final MessageFactory messageFactory;

    public HelloWorldApp(StrategyFactory strategyFactory,
                         MessageResolver messageResolver,
                         MessageFactory messageFactory) {
        this.strategyFactory = strategyFactory;
        this.messageResolver = messageResolver;
        this.messageFactory = messageFactory;
    }

    public static void main(String[] args) throws Exception {
        new ApplicationRunner().run(HelloWorldApp.class);
    }

    public void run() {
        this.say(messageFactory.create("hello world!", System.out));
    }

    public void say(Message messageBody) {
        MessageStrategy strategy = strategyFactory.createStrategy(messageBody, messageResolver);
        messageBody.send(strategy);
    }
}
