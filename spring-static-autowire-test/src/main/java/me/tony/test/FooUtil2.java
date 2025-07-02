package me.tony.test;

public class FooUtil2 {

    private static IFoo foo;

    public static void foo() {
        foo.hello(FooUtil2.class.getName());
    }
}
