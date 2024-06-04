package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameControlSwitch extends Application {
    private boolean useJoystick = true;
    private Pane root;
    private VirtualJoystick joystick;
    private SwipeGesture swipeGesture;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        joystick = new VirtualJoystick(root);
        swipeGesture = new SwipeGesture(root);

        Button switchButton = new Button("Switch Control");
        switchButton.setOnAction(event -> switchControl());
        switchButton.setLayoutX(700);
        switchButton.setLayoutY(20);

        root.getChildren().add(switchButton);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Control Switch Example");
        primaryStage.show();

        enableJoystickControls();
        root.requestFocus();  // Ensure the root pane has focus for keyboard events
    }

    private void switchControl() {
        useJoystick = !useJoystick;
        if (useJoystick) {
            enableJoystickControls();
        } else {
            enableSwipeControls();
        }
        root.requestFocus();  // Ensure focus is set after switching controls
    }

    private void enableJoystickControls() {
        swipeGesture.disable();
        joystick.enable();
    }

    private void enableSwipeControls() {
        joystick.disable();
        swipeGesture.enable();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
