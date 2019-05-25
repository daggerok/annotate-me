package com.github.daggerok.gettingstarted;

import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class MyService {

  private static final String GREETING_TEMPLATE = "Hello, %s!";

  String sayHello(String name) {
    return format(GREETING_TEMPLATE, name);
  }
}

class MyServiceTest {

  @Test
  void test() {
    MyService service = new MyService();
    assertThat(service).isNotNull();

    String hello = service.sayHello("Maksimko");
    assertThat(hello).isEqualTo("Hello, Maksimko!");
  }
}
