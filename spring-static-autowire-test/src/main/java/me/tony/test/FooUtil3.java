package me.tony.test;

public class FooUtil3 {

    private static Foo2 foo;

    public static void foo() {
        foo.hello(FooUtil3.class.getName());
    }
}
