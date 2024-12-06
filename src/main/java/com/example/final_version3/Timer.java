package com.example.testgenerator;

import java.time.Instant;
import java.time.Duration;

public class Timer {
    //have fun...stfu


    private Instant startTime;
    private Instant endTime;

    public Timer() {
    }

    public void startTimer() {
        startTime = Instant.now();
    }

    public void endTimer() {
        endTime = Instant.now();
    }


    public String getTimeInMinutesAndSeconds() {
        long totalSeconds = Duration.between(startTime, endTime).toSeconds();
        long mins = totalSeconds / 60;
        long sec = totalSeconds % 60;
        return mins + " minutes and " + sec + " seconds";
    }
}
