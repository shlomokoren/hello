package net.kimleo.hello.validate;

import net.kimleo.inject.annotation.Component;

@Component
public class TokenValidator implements Validator<String> {
    @Override
    public boolean validate(String s) {
        return s != null && !s.isEmpty();
    }
}
