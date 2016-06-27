package net.kimleo.hello.message;

import net.kimleo.hello.text.TokenList;
import net.kimleo.inject.annotation.Component;
import net.kimleo.inject.annotation.Construct;

import java.io.PrintStream;

@Component
public class PrintMessageResolver implements MessageResolver<PrintStream> {

    private final MessageParser<TokenList> parser;
    private final ConcreteTokenVisitor visitor;

    @Construct
    public PrintMessageResolver(MessageParser<TokenList> parser, ConcreteTokenVisitor visitor) {
        this.parser = parser;
        this.visitor = visitor;
    }

    @Override
    public void resolve(String message, PrintStream stream) {
        parser.parse(message).accept(visitor.withStream(stream));
    }
}
