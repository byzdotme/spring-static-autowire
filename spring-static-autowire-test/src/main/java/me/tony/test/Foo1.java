package me.tony.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class Foo1 implements IFoo {
    @Override
    public void hello(String name) {
        log.info("Foo1 hello, {}", name);
    }
}
