package com.github.daggerok;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlerProcessor {

    @SneakyThrows
    public static void start() {
        Optional<Method> maybeMethod = Arrays.stream(Consumer.class.getDeclaredMethods())
                                             .filter(m -> Objects.nonNull(m.getDeclaredAnnotation(OnMessage.class)))
                                             .findFirst();
        maybeMethod.ifPresent(
                method -> Try.run(
                        () -> method.invoke(Consumer.class.newInstance(), "Hello from handler!")));
    }
}
