package me.tony.test;

import lombok.extern.slf4j.Slf4j;
import me.byz.spring.AssignBean;
import me.byz.spring.AutowireStatic;
import me.byz.spring.EnableStaticInjection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@EnableStaticInjection
@AutowireStatic(FooLombok.class)
@AutowireStatic(value = FooUtil1.class, assignments = {@AssignBean(value = "foo", beanType = Foo2.class)})
@AutowireStatic(value = FooUtil2.class, assignments = {@AssignBean(value = "foo", beanType = IFoo.class)})
@AutowireStatic(value = FooUtil3.class, assignments = {@AssignBean("foo")})
@AutowireStatic(BarUtil1.class)
// strict mode will make application failed to start
//@AutowireStatic(value = BarUtil2.class, strict = true)
public class AppMain implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        FooUtil1.foo();
        FooUtil2.foo();
        FooUtil3.foo();
        FooLombok.foo();
        try {
            BarUtil1.bar();
        } catch (NullPointerException e) {
            log.info("expected npe!", e);
        }
    }
}
