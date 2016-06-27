package net.kimleo.hello.message;

import net.kimleo.inject.annotation.Component;

@Component
public interface MessageResolver<T> {
    void resolve(String message, T target);
}
