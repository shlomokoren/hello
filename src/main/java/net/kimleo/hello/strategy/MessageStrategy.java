package net.kimleo.hello.strategy;

import net.kimleo.hello.annotation.Component;

import java.io.PrintStream;

@Component
public interface MessageStrategy {
    void sendMessage(PrintStream stream);
}
