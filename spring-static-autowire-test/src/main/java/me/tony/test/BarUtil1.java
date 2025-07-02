package me.tony.test;

public class BarUtil1 {

    private static IBar bar;

    public static void bar() {
        bar.hello(BarUtil1.class.getName());
    }
}
