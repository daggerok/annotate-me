package com.github.daggerok;

import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Bus {

    private static final EventBus bus = new EventBus("default");

    public static EventBus register(Object o) {
        bus.register(o);
        return bus;
    }

    public static EventBus fire(Object message) {
        bus.post(message);
        return bus;
    }
}
