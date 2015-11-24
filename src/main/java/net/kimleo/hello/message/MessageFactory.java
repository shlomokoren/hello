package net.kimleo.hello.message;

import net.kimleo.hello.annotation.Component;

import java.io.PrintStream;

@Component
public interface MessageFactory {
    Message create(String message, final PrintStream stream);
}
