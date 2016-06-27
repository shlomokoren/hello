package net.kimleo.hello.text;

import net.kimleo.inject.annotation.Component;
import net.kimleo.inject.annotation.Construct;

import static java.util.Arrays.asList;

@Component(qualifier = "wordTokenizer")
public class WordsTokenizer implements Tokenizer {

    private final TokenListFactory tokenListFactory;

    @Construct
    public WordsTokenizer(TokenListFactory tokenListFactory) {
        this.tokenListFactory = tokenListFactory;
    }

    @Override
    public TokenList tokenize(String text) {
        String[] strings = text.split("\\s");
        return tokenListFactory.create(asList(strings));
    }

}
