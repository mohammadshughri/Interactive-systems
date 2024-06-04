package com.example.demo;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class UserSurvey {
    public void showSurvey() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("User Satisfaction Survey");
        alert.setHeaderText("Please rate your experience");
        alert.setContentText("Which control did you prefer?");

        ButtonType buttonTypeJoystick = new ButtonType("Joystick");
        ButtonType buttonTypeSwipe = new ButtonType("Swipe");

        alert.getButtonTypes().setAll(buttonTypeJoystick, buttonTypeSwipe);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeJoystick) {
                // Log user preference for joystick
            } else if (response == buttonTypeSwipe) {
                // Log user preference for swipe
            }
        });
    }
}

