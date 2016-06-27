package net.kimleo.hello.text;

import net.kimleo.inject.annotation.Component;

@FunctionalInterface
@Component
public interface TokenVisitor {
    void visit(String token);
}
