package com.example.demo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class VirtualJoystick {
    private final double joystickCenterX = 100;
    private final double joystickCenterY = 500;
    private final double joystickRadius = 50;
    private double joystickX = joystickCenterX, joystickY = joystickCenterY;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final Pane root;

    public VirtualJoystick(Pane root) {
        this.root = root;
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
    }

    public void enable() {
        drawJoystick();
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleJoystickMovement);
        root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        root.getScene().addEventHandler(KeyEvent.KEY_RELEASED, this::handleKeyRelease);
    }

    public void disable() {
        canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleJoystickMovement);
        root.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        root.getScene().removeEventHandler(KeyEvent.KEY_RELEASED, this::handleKeyRelease);
    }

    private void drawJoystick() {
        gc.clearRect(0, 0, 800, 600);
        gc.strokeOval(joystickCenterX - joystickRadius, joystickCenterY - joystickRadius, joystickRadius * 2, joystickRadius * 2);
        gc.fillOval(joystickX - 10, joystickY - 10, 20, 20);
    }

    private void handleJoystickMovement(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double distance = Math.sqrt(Math.pow(mouseX - joystickCenterX, 2) + Math.pow(mouseY - joystickCenterY, 2));
        if (distance < joystickRadius) {
            joystickX = mouseX;
            joystickY = mouseY;
            drawJoystick();
            ((GameControlSwitch) root.getScene().getWindow().getUserData()).logData(0);
            System.out.println("Joystick moved: (" + joystickX + ", " + joystickY + ")");
        } else {
            ((GameControlSwitch) root.getScene().getWindow().getUserData()).logData(1);
        }
    }

    private void handleKeyPress(KeyEvent event) {
        double moveStep = 5.0;
        if (event.getCode() == KeyCode.UP) {
            joystickY = Math.max(joystickY - moveStep, joystickCenterY - joystickRadius);
        } else if (event.getCode() == KeyCode.DOWN) {
            joystickY = Math.min(joystickY + moveStep, joystickCenterY + joystickRadius);
        } else if (event.getCode() == KeyCode.LEFT) {
            joystickX = Math.max(joystickX - moveStep, joystickCenterX - joystickRadius);
        } else if (event.getCode() == KeyCode.RIGHT) {
            joystickX = Math.min(joystickX + moveStep, joystickCenterX + joystickRadius);
        }
        drawJoystick();
        ((GameControlSwitch) root.getScene().getWindow().getUserData()).logData(0);
        System.out.println("Joystick moved: (" + joystickX + ", " + joystickY + ")");
    }

    private void handleKeyRelease(KeyEvent event) {
        // Optionally handle key release events if needed
    }
}
