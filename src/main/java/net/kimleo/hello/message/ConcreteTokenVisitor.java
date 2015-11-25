package net.kimleo.hello.message;

import net.kimleo.hello.annotation.Component;
import net.kimleo.hello.text.TokenVisitor;
import net.kimleo.hello.validate.Validator;

import java.io.PrintStream;

@Component
public class ConcreteTokenVisitor implements TokenVisitor {

    private Validator<String> validator;

    @Override
    public void visit(String token) {
        validator.validate(token);
    }

    public TokenVisitor withStream(PrintStream stream) {
        return token -> {
            visit(token);
            if (validator.validate(token))
                stream.println(token);
        };
    }
}
