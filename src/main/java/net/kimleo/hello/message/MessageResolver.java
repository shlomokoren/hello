package net.kimleo.hello.message;

import net.kimleo.hello.annotation.Component;

@Component
public interface MessageResolver<T> {
    void resolve(String message, T target);
}
