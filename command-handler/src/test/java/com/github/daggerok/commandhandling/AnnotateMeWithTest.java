package com.github.daggerok.commandhandling;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface CountWith {
  Class<?> value();
}

class CounterHandler {

  public CounterHandler() {
    System.out.println("calling CounterHandler constructor...");
  }

  private static final AtomicLong counter = new AtomicLong();

  public void handle(Object o) {
    long value = counter.incrementAndGet();
    System.out.println(format("%s counted: %s", value, o));
  }
}

class CounterHandlerTest {

  @Test
  void test_constructor_creation() {
    Constructor<?>[] constructors = CounterHandler.class.getDeclaredConstructors();
    assertThat(constructors).hasSize(1);

    Constructor<?> constructor = Stream.of(constructors).findFirst().orElseThrow(RuntimeException::new);
    assertThat(constructor).isNotNull();
    assertThat(constructor.getParameterCount()).isEqualTo(0);

    Object instance = Try.of(() -> constructor.newInstance(new Object[0]))
                         .getOrElseThrow(RuntimeException::new);
    assertThat(instance).isNotNull();

    System.out.println("instance = " + instance);
  }
}

class MyTestService implements Consumer {

  @Override
  @CountWith(CounterHandler.class)
  public void accept(Object o) {
    System.out.println("o = " + o);
  }
}

class AnnotateMeWithTest {

  private static final Logger log = Logger.getGlobal();
  static final Map<Class<CounterHandler>, CounterHandler> counters = new ConcurrentHashMap<>();

  @Test
  void test_annotate_me_with_annotation_parameter_type_instantiation() {
    MyTestService myTestService = new MyTestService();
    Method[] declaredMethods = Try.of(() -> myTestService.getClass().getDeclaredMethods())
                                  .getOrElseThrow(this::fuck);
    Method firstMethod = declaredMethods[0];
    CountWith[] annotations = firstMethod.getAnnotationsByType(CountWith.class);
    CountWith countWith = annotations[0];
    assertThat(countWith.value()).isEqualTo(CounterHandler.class);

    CounterHandler counterHandler = counters.getOrDefault(
        countWith.value(),
        Try.of(() -> countWith.value().getDeclaredConstructors())
           .map(constructors -> Try.of(() -> constructors[0].newInstance(new Object[0]))
                                   .getOrElseThrow(this::fuck))
           .map(CounterHandler.class::cast)
           .getOrElseThrow(this::fuck)
    );
    assertThat(counterHandler).isNotNull();
  }

  private RuntimeException fuck(Throwable throwable) {
    log.severe(throwable.getLocalizedMessage());
    throw new RuntimeException(throwable);
  }
}
