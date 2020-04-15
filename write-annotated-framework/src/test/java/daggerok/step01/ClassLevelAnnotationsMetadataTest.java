package daggerok.step01;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
  String param1() default "";
  String[] param2() default {};
}

@MyAnnotation
class MyClass1 { }

@MyAnnotation(param1 = "ololo")
class MyClass2 { }

@MyAnnotation(param1 = "ololo", param2 = { "ololo", "trololo" })
class MyClass3 { }

class ClassLevelAnnotationsMetadataTest {

  @Test void MyClass1_should_have_class_level_annotation() throws IllegalAccessException, InstantiationException {

    MyClass1 instance = MyClass1.class.newInstance();
    Class<? extends MyClass1> type = instance.getClass();
    Annotation[] annotations = type.getAnnotations();

    assertThat(annotations).hasSize(1);
  }

  @Test void MyClass1_should_have_MyAnnotation() throws IllegalAccessException, InstantiationException {

    MyClass1 instance = MyClass1.class.newInstance();
    Class<? extends MyClass1> type = instance.getClass();
    Annotation[] annotations = type.getAnnotations();

    assertThat(annotations).hasSize(1);
    assertThat(annotations[0]).isInstanceOf(MyAnnotation.class);
  }

  @Test void MyClass2_should_have_MyAnnotation_with_param1()
      throws IllegalAccessException, InstantiationException {

    MyClass2 instance = MyClass2.class.newInstance();
    Class<? extends MyClass2> type = instance.getClass();
    Annotation[] annotations = type.getAnnotations();

    assertThat(annotations).hasSize(1);

    Annotation maybeMyAnnotation = annotations[0];
    assertThat(maybeMyAnnotation).isInstanceOf(MyAnnotation.class);

    MyAnnotation myAnnotation = (MyAnnotation) maybeMyAnnotation;
    assertThat(myAnnotation.param1()).isEqualTo("ololo");
  }

  @Test void MyClass3_should_have_MyAnnotation_with_expected_param1_and_param2()
      throws IllegalAccessException, InstantiationException {

    MyClass3 instance = MyClass3.class.newInstance();
    Class<? extends MyClass3> type = instance.getClass();
    Annotation[] annotations = type.getAnnotations();

    assertThat(annotations).hasSize(1);

    Annotation maybeMyAnnotation = annotations[0];
    assertThat(maybeMyAnnotation).isInstanceOf(MyAnnotation.class);

    MyAnnotation myAnnotation = MyAnnotation.class.cast(maybeMyAnnotation);
    assertThat(myAnnotation.param1()).isEqualTo("ololo");
    assertThat(myAnnotation.param2()).isNotEmpty()
                                     .contains("trololo", "ololo");
  }
}
