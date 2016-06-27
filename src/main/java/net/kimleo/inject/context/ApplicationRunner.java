package net.kimleo.inject.context;

@FunctionalInterface
public interface ApplicationRunner {
    void run(String... args);
}
