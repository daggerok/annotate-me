package com.github.daggerok;

import com.github.daggerok.examples.LongConsumer;
import com.github.daggerok.examples.ObjectConsumer;
import com.github.daggerok.examples.StringConsumer;
import com.github.daggerok.pubsub.Subscribers;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Main {
    public static void main(String[] args) {

        log.info("Registered subscribers, run in async and wait for completion...");

        new ObjectConsumer();
        new StringConsumer();
        new LongConsumer();

        Try.run(() -> CompletableFuture.supplyAsync(() -> Subscribers.post("Hello!"))
                                       .thenComposeAsync(first -> Subscribers.post(12))
                                       .thenComposeAsync(second -> Subscribers.post(System.currentTimeMillis()))
                                       .get(1, TimeUnit.SECONDS));
        log.info("I'm done.");
    }
}
