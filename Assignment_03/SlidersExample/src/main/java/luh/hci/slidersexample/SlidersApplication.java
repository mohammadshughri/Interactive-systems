package luh.hci.slidersexample;

import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import luh.hci.lifting.Lifting.*;

import static luh.hci.lifting.Lifting.*;

public class SlidersApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Slider sliderA = new Slider();
        Slider sliderB = new Slider();
        Label textLabel = new Label();

        /*
        sliderA.valueProperty().addListener((o, oldValue, newValue) -> {
            sliderB.setValue(100.0 - newValue.doubleValue());
        });

        sliderB.valueProperty().addListener((o, oldValue, newValue) -> {
            sliderA.setValue(100.0 - newValue.doubleValue());
        });
        */

        bindBidirectional(
                sliderA.valueProperty(),
                sliderB.valueProperty(),
                (a) -> 100. - a.doubleValue(),
                (b) -> 100. - b.doubleValue()
        );

        textLabel.textProperty().bind(sliderA.valueProperty().asString());

        VBox box = new VBox(sliderA, sliderB, textLabel);
        box.setPadding(new Insets(32, 32, 32, 32));
        box.setSpacing(32);

        Scene scene = new Scene(box,600, 400);
        stage.setTitle("Sliders Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}