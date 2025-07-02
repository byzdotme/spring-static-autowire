package me.tony.test;

public class FooUtil1 {

    private static IFoo foo;

    public static void foo() {
        foo.hello(FooUtil1.class.getName());
    }
}
