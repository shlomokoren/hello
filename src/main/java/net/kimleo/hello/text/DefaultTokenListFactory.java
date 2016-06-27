package net.kimleo.hello.text;

import net.kimleo.inject.annotation.Component;
import net.kimleo.inject.annotation.Factory;

import java.util.Iterator;
import java.util.List;

@Factory(product = TokenList.class)
@Component
public class DefaultTokenListFactory implements TokenListFactory {

    private static TokenListFactory instance;

    private DefaultTokenListFactory() {
    }

    public static TokenListFactory getInstance() {
        if (instance == null) {
            instance = new DefaultTokenListFactory();
        }
        return instance;
    }

    @Override
    public TokenList create(final List<String> tokens) {
        return new TokenList() {

            @Override
            public void accept(TokenVisitor visitor) {
                tokens.forEach(visitor::visit);
            }

            @Override
            public Iterator<String> iterator() {
                return tokens.iterator();
            }
        };
    }
}
