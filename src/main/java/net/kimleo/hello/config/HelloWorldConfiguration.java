package net.kimleo.hello.config;

import net.kimleo.hello.validate.Validator;
import net.kimleo.inject.annotation.Bean;
import net.kimleo.inject.annotation.Config;

@Config
public class HelloWorldConfiguration {
    @Bean
    public Validator<String> tokenValidator() {
        return (token) -> token != null && !token.isEmpty();
    }
}
