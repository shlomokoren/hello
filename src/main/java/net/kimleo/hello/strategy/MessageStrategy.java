package net.kimleo.hello.strategy;

import net.kimleo.inject.annotation.Component;

import java.io.PrintStream;

@Component
public interface MessageStrategy {
    void sendMessage(PrintStream stream);
}
