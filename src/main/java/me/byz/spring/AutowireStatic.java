package me.byz.spring;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AutowireStatic.List.class)
public @interface AutowireStatic {

    @AliasFor("utilityClass")
    Class<?> value();

    @AliasFor("value")
    Class<?> utilityClass();

    AssignBean[] assignments() default {};

    boolean strict() default false;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AutowireStatic[] value();
    }
}
