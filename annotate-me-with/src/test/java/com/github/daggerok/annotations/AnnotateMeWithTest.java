package com.github.daggerok.annotations;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

//@Inherited
//@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotateMeWith {
  String value() default "Hello, %s!";
}

class MyTestService {
  private static final String GREETING_TEMPLATE = "Hey, %s!";

  @AnnotateMeWith(value = "Hola, %s!")
  String sayHello(String name) {
    return format(GREETING_TEMPLATE, name);
  }
}

class AnnotateMeWithTest {

  @Test
  void test_annotate_me_with_annotation() {
    MyTestService myTestService = new MyTestService();
    Method[] declaredMethods = Try.of(() -> myTestService.getClass().getDeclaredMethods())
                                  .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    assertThat(declaredMethods).hasSize(1);

    Method firstMethod = Arrays.stream(declaredMethods).findFirst().orElseThrow(RuntimeException::new);
    assertThat(firstMethod).isNotNull();
    assertThat(firstMethod.getName()).isEqualTo("sayHello");

    AnnotateMeWith[] annotations = firstMethod.getAnnotationsByType(AnnotateMeWith.class);
    assertThat(annotations).hasSize(1);

    AnnotateMeWith annotateMeWith = Stream.of(annotations).findFirst().orElseThrow(RuntimeException::new);
    assertThat(annotateMeWith.value()).isEqualTo("Hola, %s!");
  }
}
