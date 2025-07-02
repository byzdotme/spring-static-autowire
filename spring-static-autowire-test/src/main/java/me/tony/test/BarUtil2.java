package me.tony.test;

public class BarUtil2 {

    private static IBar bar;

    public static void bar() {
        bar.hello(BarUtil2.class.getName());
    }
}
