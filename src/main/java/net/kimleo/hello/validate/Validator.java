package net.kimleo.hello.validate;


import net.kimleo.inject.annotation.Component;

@Component
public interface Validator<T> {
    boolean validate(T t);
}
