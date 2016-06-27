package net.kimleo.hello.message;

import net.kimleo.inject.annotation.Component;

import java.io.PrintStream;

@Component
public interface MessageFactory {
    Message create(String message, final PrintStream stream);
}
