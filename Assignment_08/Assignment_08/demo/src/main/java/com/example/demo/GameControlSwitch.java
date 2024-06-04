package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameControlSwitch extends Application {
    private boolean useJoystick = true;
    private Pane root;
    private VirtualJoystick joystick;
    private SwipeGesture swipeGesture;
    private TextField participantIdField;
    private int participantID = 1; // Example participant ID
    private int block = 1; // Example block
    private int trial = 1; // Example trial
    private long startTime;
    private PrintWriter writer;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        joystick = new VirtualJoystick(root);
        swipeGesture = new SwipeGesture(root);

        Button switchButton = new Button("Switch Control");
        switchButton.setOnAction(event -> switchControl());
        switchButton.setLayoutX(700);
        switchButton.setLayoutY(20);

        Button startButton = new Button("Start Trial");
        startButton.setOnAction(event -> startTrial());
        startButton.setLayoutX(700);
        startButton.setLayoutY(60);

        participantIdField = new TextField();
        participantIdField.setPromptText("Participant ID");
        participantIdField.setLayoutX(700);
        participantIdField.setLayoutY(100);

        Button newParticipantButton = new Button("New Participant");
        newParticipantButton.setOnAction(event -> startNewParticipant());
        newParticipantButton.setLayoutX(700);
        newParticipantButton.setLayoutY(140);

        root.getChildren().addAll(switchButton, startButton, participantIdField, newParticipantButton);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Experiment Prototype");

        // Set the UserData for the primary stage
        primaryStage.setUserData(this);

        primaryStage.show();

        enableJoystickControls();
        root.requestFocus();  // Ensure the root pane has focus for keyboard events

        // Initialize the writer in overwrite mode
        initializeWriter(true);
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

    private void startTrial() {
        trial++;
        startTime = System.currentTimeMillis();
    }

    private void startNewParticipant() {
        String participantIdText = participantIdField.getText();
        if (!participantIdText.isEmpty()) {
            participantID = Integer.parseInt(participantIdText);
            block = 1;
            trial = 0; // Reset trial counter
            System.out.println("New participant: " + participantID);
            // Initialize writer in append mode for new participants
            initializeWriter(false);
        }
    }

    private void initializeWriter(boolean overwrite) {
        try {
            if (writer != null) {
                writer.close();
            }
            writer = new PrintWriter(new FileWriter("MohammadSukriJoystick.csv", !overwrite));
            if (overwrite) {
                writer.println("Participant,Block,Trial,Technique,Time,Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logData(int error) {
        if (writer == null) {
            System.err.println("Writer is not initialized!");
            return;
        }
        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        writer.println(participantID + "," + block + "," + trial + "," + (useJoystick ? 0 : 1) + "," + timeTaken + "," + error);
        writer.flush();
    }

    @Override
    public void stop() throws Exception {
        if (writer != null) {
            writer.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}