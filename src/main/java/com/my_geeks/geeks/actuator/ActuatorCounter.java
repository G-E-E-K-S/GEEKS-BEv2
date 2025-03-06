package com.my_geeks.geeks.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ActuatorCounter {

    private final Counter homecomingSendCounter;
    private final Counter roomateAcceptCounter;
    private final Counter scheduleCreateCounter;

    public ActuatorCounter(MeterRegistry meterRegistry) {
        this.homecomingSendCounter = meterRegistry.counter("homecoming.send.counter");
        this.roomateAcceptCounter = meterRegistry.counter("roommate.accept.counter");
        this.scheduleCreateCounter = meterRegistry.counter("schedule.create.counter");
    }

    public void homecomingSendIncrement() {
        homecomingSendCounter.increment();
    }

    public void roommateAcceptIncrement() {
        roomateAcceptCounter.increment();
    }

    public void scheduleCreateIncrement() {
        scheduleCreateCounter.increment();
    }
}
