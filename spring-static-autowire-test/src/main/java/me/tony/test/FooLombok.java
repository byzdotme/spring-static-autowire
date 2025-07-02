package me.tony.test;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FooLombok {

    private IFoo foo;

    public static void foo() {
        foo.hello(FooLombok.class.getName());
    }
}
