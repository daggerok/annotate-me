package com.github.daggerok;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        log.info("I'm running!");
        HandlerProcessor.start();
    }
}
