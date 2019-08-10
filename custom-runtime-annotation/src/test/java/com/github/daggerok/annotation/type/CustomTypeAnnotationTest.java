package com.github.daggerok.annotation.type;

import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface CustomTypeAnnotation {
    boolean enabled() default true;
}

class Aggregate1 {}

@CustomTypeAnnotation(enabled = false)
class Aggregate2 {}

@CustomTypeAnnotation
class Aggregate3 {}

class CustomTypeAnnotationTest {

    @Test
    void Class1_should_not_be_processed_because_it_has_not_be_annotated() {
        // when:
        CustomTypeAnnotation annotation = Aggregate1.class.getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNull();
    }

    @Test
    void Class2_should_not_be_processed_because_it_has_been_disabled_via_annotation_property() {
        // when:
        CustomTypeAnnotation annotation = Aggregate2.class.getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNotNull();
        // and:
        assertThat(annotation.enabled()).isFalse();
    }

    @Test
    void Class3_should_be_processed() {
        // when:
        CustomTypeAnnotation annotation = Aggregate3.class.getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNotNull();
        // and:
        assertThat(annotation.enabled()).isTrue();
    }

    @Test
    void instance_of_Class1_should_not_be_processed_because_it_has_not_be_annotated() {
        // given:
        Aggregate1 instance = new Aggregate1();
        // when:
        CustomTypeAnnotation annotation = instance.getClass().getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNull();
    }

    @Test
    void instance_of_Class2_should_not_be_processed_because_it_has_been_disabled_via_annotation_property()
            throws IllegalAccessException, InstantiationException {

        // given:
        Aggregate2 instance = Aggregate2.class.newInstance();
        // when:
        CustomTypeAnnotation annotation = instance.getClass().getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNotNull();
        // and:
        assertThat(annotation.enabled()).isFalse();
    }

    @Test
    void instance_of_Class3_should_be_processed() throws NoSuchMethodException, /* <- getDeclaredConstructor */
            IllegalAccessException, InvocationTargetException, InstantiationException /* <- newInstance */ {

        // given:
        Aggregate3 instance = Aggregate3.class.getDeclaredConstructor().newInstance(new Object[0]);
        // when:
        CustomTypeAnnotation annotation = instance.getClass().getDeclaredAnnotation(CustomTypeAnnotation.class);
        // then:
        assertThat(annotation).isNotNull();
        // and:
        assertThat(annotation.enabled()).isTrue();
    }
}
