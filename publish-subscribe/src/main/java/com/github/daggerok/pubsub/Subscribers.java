package com.github.daggerok.pubsub;

import io.vavr.Predicates;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscribers {

    private static final Collection<Object> subscribers = new CopyOnWriteArrayList<>();

    /**
     * Register subscribers contains @{@link Subscribe} annotated methods
     *
     * @param objects subscribers varargs
     */
    public static void register(Object... objects) {
        subscribers.addAll(Arrays.asList(objects));
    }

    /**
     * Post message to annotated methods of subscribed objects
     *
     * @param message Message object to be consumed by subscribers
     * @return Completable future
     */
    public static CompletableFuture<Void> post(Object message) {
        return CompletableFuture.runAsync(() -> Subscribers.supply(message));
    }

    /**
     * Supply message flow:
     * <p>
     * 1. iterates over all subscribers
     * 2. map subscribers with all annotated methods
     * 3. filter all non-empty entries (object -> methods)
     * 4. for each pair:
     * 4.1. filter all methods by it param types containing assignable from message type
     * 4.2. for each method invoke on given subscriber object for message parameter
     *
     * @param message Message to be sent
     */
    private static void supply(Object message) {
        subscribers.parallelStream()
                   .map(subscriber -> singletonMap(subscriber, findAll(subscriber)))
                   .map(Map::entrySet)
                   .filter(Predicates.not(Set::isEmpty))
                   .forEach(entries -> {
                       Map.Entry<Object, Collection<Method>> pair = entries.iterator().next();
                       Object subscriber = pair.getKey();
                       Collection<Method> methods = pair.getValue();
                       methods.parallelStream()
                              .filter(method -> Arrays.stream(method.getParameterTypes())
                                                      .allMatch(aClass -> aClass.isAssignableFrom(message.getClass())))
                              .forEach(method -> Try.run(() -> method.invoke(subscriber, message)));
                   });
    }

    /**
     * Search for annotated methods
     *
     * @param subscriber Subscribed object with annotated methods
     * @return Collection of annotated methods with @{@link Subscribe} annotation
     */
    private static Collection<Method> findAll(Object subscriber) {
        return Arrays.stream(subscriber.getClass().getDeclaredMethods())
                     .filter(m -> Objects.nonNull(m.getDeclaredAnnotation(Subscribe.class)))
                     .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    // private static Optional<Method> find(Object subscriber) {
    //     return Arrays.stream(subscriber.getClass().getDeclaredMethods())
    //                  .filter(m -> Objects.nonNull(m.getDeclaredAnnotation(Subscribe.class)))
    //                  .findFirst();
    // }
}
