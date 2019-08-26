package com.github.daggerok;

import com.google.common.eventbus.Subscribe;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class Consumer {
    { Bus.register(this); }

    @Subscribe
    public void on(String message) {
        log.info("received: {}", message);
    }
}
