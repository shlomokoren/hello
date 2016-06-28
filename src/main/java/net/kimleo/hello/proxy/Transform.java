package net.kimleo.hello.proxy;

import java.util.function.Function;

public interface Transform<T>{
    T apply(T self);
}
