package me.tony.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Foo2 implements IFoo {
    @Override
    public void hello(String name) {
        log.info("Foo2 hello, {}", name);
    }
}
