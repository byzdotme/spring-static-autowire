package me.byz.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticAutowireProcessor implements BeanFactoryAware, SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(StaticAutowireProcessor.class);

    @Nullable
    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ListableBeanFactory) {
            this.beanFactory = (ListableBeanFactory) beanFactory;
        } else {
            log.warn("[StaticAutowireProcessor]bean factory is not ListableBeanFactory");
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (beanFactory == null) {
            return;
        }
        Set<Object> annotatedBeans = new HashSet<>();
        annotatedBeans.addAll(beanFactory.getBeansWithAnnotation(AutowireStatic.class).values());
        annotatedBeans.addAll(beanFactory.getBeansWithAnnotation(AutowireStatic.List.class).values());
        Set<? extends Class<?>> annotatedClasses = annotatedBeans.stream().map(Object::getClass).collect(Collectors.toSet());
        if (annotatedClasses.isEmpty()) {
            log.warn("[StaticAutowireProcessor]: No beans annotated with @AutowireStatic");
        }
        List<MergedAnnotation<AutowireStatic>> autowireStatics = annotatedClasses.stream()
                .flatMap(it -> MergedAnnotations.from(it, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).stream(AutowireStatic.class))
                .collect(Collectors.toList());
        log.debug("[StaticAutowireProcessor]{} AutowireStatic found", autowireStatics.size());
        autowireStatics.forEach(this::processAutowireStatic);
    }

    private void processAutowireStatic(MergedAnnotation<AutowireStatic> annotation) {
        boolean strict = annotation.getBoolean("strict");
        Class<?> utilityClass = annotation.getClass(MergedAnnotation.VALUE);
        MergedAnnotation<AssignBean>[] assignments = annotation.getAnnotationArray("assignments", AssignBean.class);
        if (assignments.length == 0) {
            processAllStaticProperties(utilityClass, strict);
        } else {
            processDeclaredStaticProperties(utilityClass, assignments, strict);
        }
    }

    private void processDeclaredStaticProperties(Class<?> utilityClass, MergedAnnotation<AssignBean>[] assignments, boolean strict) {
        Arrays.stream(assignments).forEach(assignment -> {
            String propertyName = assignment.getString(MergedAnnotation.VALUE);
            String beanName = assignment.getString("beanName");
            try {
                Field field = utilityClass.getDeclaredField(propertyName);
                if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    processStrict(strict, propertyName + " of utility class " + utilityClass.getName() + " is not static or final");
                    return;
                }
                Class<?> beanType = assignment.getClass("beanType");
                if (beanType == void.class) {
                    beanType = field.getType();
                }
                processField(field, beanType, beanName, strict);
            } catch (NoSuchFieldException e) {
                processStrict(strict, "Utility class " + utilityClass.getName() + " has no private field named " + propertyName, e);
            }
        });
    }

    private void processAllStaticProperties(Class<?> utilityClass, boolean strict) {
        Arrays.stream(utilityClass.getDeclaredFields())
                .filter(it -> Modifier.isStatic(it.getModifiers()) && !Modifier.isFinal(it.getModifiers()))
                .forEach(field -> processField(field, field.getType(), "", strict));
    }

    private void processField(Field field, Class<?> beanType, String beanName, boolean strict) {
        try {
            Object bean;
            if (beanName != null && !beanName.isEmpty()) {
                try {
                    bean = Objects.requireNonNull(beanFactory).getBean(beanName, beanType);
                } catch (BeansException e) {
                    // fall back to by-type lookup if by-name failed
                    bean = Objects.requireNonNull(beanFactory).getBean(beanType);
                }
            } else {
                bean = Objects.requireNonNull(beanFactory).getBean(beanType);
            }
            ReflectionUtils.makeAccessible(field);
            field.set(null, bean);
        } catch (BeansException e) {
            processStrict(strict, "Bean of type " + beanType.getName() + " failed to found", e);
        } catch (IllegalAccessException e) {
            processStrict(strict, "Failed to access " + field.getName(), e);
        } catch (IllegalArgumentException e) {
            processStrict(strict, "Failed to set " + field.getName(), e);
        }
    }

    private void processStrict(boolean strict, String errorMessage) {
        if (strict) {
            throw new IllegalStateException(errorMessage);
        } else {
            log.warn(errorMessage);
        }
    }

    private void processStrict(boolean strict, String errorMessage, Exception cause) {
        if (strict) {
            throw new IllegalStateException(errorMessage, cause);
        } else {
            log.warn(errorMessage, cause);
        }
    }
}
