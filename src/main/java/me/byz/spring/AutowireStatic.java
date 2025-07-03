package me.byz.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AutowireStatic.List.class)
public @interface AutowireStatic {

    /**
     * your utility class
     *
     * @return utility class
     */
    Class<?> value();

    /**
     * assignments strategy. empty mean all nonfinal static fields should be injected
     *
     * @return assignments strategy
     */
    AssignBean[] assignments() default {};

    /**
     * when strict, fail injection for any reason will cause spring application start fail
     *
     * @return strict mode or not
     */
    boolean strict() default false;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AutowireStatic[] value();
    }
}
