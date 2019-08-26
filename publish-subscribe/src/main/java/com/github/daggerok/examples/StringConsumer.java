package com.github.daggerok.examples;

import com.github.daggerok.pubsub.Subscribe;
import com.github.daggerok.pubsub.Subscribers;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StringConsumer {
    { Subscribers.register(this); }

    @Subscribe
    public void handlerString(String s) {
        log.info("string: {}", s);
    }
}
