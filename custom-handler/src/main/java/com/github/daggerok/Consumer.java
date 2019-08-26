package com.github.daggerok;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Consumer {

    @OnMessage
    public void handle(String message) {
        log.info("received: {}", message);
    }
}
