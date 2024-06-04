package com.example.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class MetricsLogger {
    private Instant startTime;
    private int collisions = 0;

    public void start() {
        startTime = Instant.now();
    }

    public void logCollision() {
        collisions++;
    }

    public void stopAndSave(String controlType) {
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        try (FileWriter writer = new FileWriter("metrics_" + controlType + ".txt", true)) {
            writer.write("Time: " + duration.toMillis() + " ms\n");
            writer.write("Collisions: " + collisions + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
