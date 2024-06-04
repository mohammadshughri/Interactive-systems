package com.example.demo;

import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Pane;

public class SwipeGesture {
    private final Pane root;

    public SwipeGesture(Pane root) {
        this.root = root;
    }

    public void enable() {
        root.addEventHandler(SwipeEvent.SWIPE_UP, event -> moveCharacter("UP"));
        root.addEventHandler(SwipeEvent.SWIPE_DOWN, event -> moveCharacter("DOWN"));
        root.addEventHandler(SwipeEvent.SWIPE_LEFT, event -> moveCharacter("LEFT"));
        root.addEventHandler(SwipeEvent.SWIPE_RIGHT, event -> moveCharacter("RIGHT"));
    }

    public void disable() {
        root.removeEventHandler(SwipeEvent.SWIPE_UP, event -> moveCharacter("UP"));
        root.removeEventHandler(SwipeEvent.SWIPE_DOWN, event -> moveCharacter("DOWN"));
        root.removeEventHandler(SwipeEvent.SWIPE_LEFT, event -> moveCharacter("LEFT"));
        root.removeEventHandler(SwipeEvent.SWIPE_RIGHT, event -> moveCharacter("RIGHT"));
    }

    private void moveCharacter(String direction) {
        // Implement character movement based on swipe direction
        ((GameControlSwitch) root.getScene().getWindow().getUserData()).logData(0);
        System.out.println("Swipe direction: " + direction);
    }
}
