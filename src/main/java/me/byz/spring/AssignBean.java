package me.byz.spring;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssignBean {

    @AliasFor("propertyName")
    String value();

    @AliasFor("value")
    String propertyName();

    Class<?> beanType() default void.class;
}
