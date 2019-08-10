package com.github.daggerok.annotation.method;

import io.vavr.control.Try;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Command {
    boolean enabled() default true;
}

class Aggregate {

    void justMethod() {
        // this one should not be processed because of no annotation
    }

    void justMethod(String cmd) {
        // this one also should not be processed because of no annotation
    }

    @Command(enabled = false)
    void disabledHandler(String cmd) {
        System.out.printf("Aggregate.handle(%s)\n", cmd);
    }

    @Command
    void handle(String cmd) {
        System.out.printf("Aggregate.handle(%s)\n", cmd);
    }

    @Command
    void handler() {
        System.out.println("Aggregate.handler()");
    }
}

@Value
@Builder
class Holder<A extends Annotation> {
    private final Method method;
    private final A annotation;
}

@Value(staticConstructor = "of")
class Processor<A extends Annotation> {
    private final Holder<A> holder;

    public Object applyTo(Object instance) throws InvocationTargetException, IllegalAccessException /* <- invoke */,
            ClassNotFoundException /* <- Class.forName */ {

        Method method = holder.getMethod();
        System.out.println(format("before invocation %s()", method.getName()));
        return method.getParameterCount() > 0
                ? method.invoke(instance, Class.forName(method.getParameterTypes()[0].getName()).cast("Hola and ololo-trololo!"))
                : method.invoke(instance);
    }
}

class CustomMethodAnnotationTest {

    @Test
    void test() throws IllegalAccessException, InstantiationException /* <- newInstance */ {
        // given:
        Aggregate instance = Aggregate.class.newInstance();
        // and:
        Method[] declaredMethods = instance.getClass().getDeclaredMethods();
        // and:
        Predicate<Method> commandMethodsSelector =
                method -> Objects.nonNull(method.getDeclaredAnnotation(Command.class));
        // then:
        Arrays.stream(declaredMethods)
              .filter(commandMethodsSelector)
              .map(method -> Holder.<Command>builder()
                      .method(method)
                      .annotation(method.getDeclaredAnnotation(Command.class))
                      .build())
              .map(Processor::of)
              .map(commandProcessor ->
                           Try.of(() -> commandProcessor.applyTo(instance))
                              .getOrElseGet(e -> format("oops: %s", e.getLocalizedMessage())))
              .forEach(System.out::println);
    }
}
