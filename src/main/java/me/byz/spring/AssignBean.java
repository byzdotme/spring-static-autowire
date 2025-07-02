package me.byz.spring;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface AssignBean {

    String value();

    Class<?> beanType() default void.class;

    /**
     * Optional Spring bean name. When specified, the processor will fetch the bean by name first and
     * then validate its type. This can be used to disambiguate situations where multiple beans share
     * the same type.
     */
    String beanName() default "";
}
