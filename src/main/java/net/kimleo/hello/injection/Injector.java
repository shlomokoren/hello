package net.kimleo.hello.injection;

public interface Injector {
    <T> T inject(Class<? extends T> clz);
}
