package com.github.daggerok;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        log.info("starting...");
        new Consumer();
        Bus.fire("hello");
        Bus.fire("world!");
        log.info("done.");
    }
}
