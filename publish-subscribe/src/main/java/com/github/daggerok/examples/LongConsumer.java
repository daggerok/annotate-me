package com.github.daggerok.examples;

import com.github.daggerok.pubsub.Subscribe;
import com.github.daggerok.pubsub.Subscribers;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LongConsumer {
    { Subscribers.register(this); }

    @Subscribe
    public void handlerLong(Long l) {
        log.info("long: {}", l);
    }
}
