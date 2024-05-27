package calculator;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jdk.jfr.Event;
import lifting.EventStream;

import static lifting.Lifting.link;
import static lifting.Lifting.map;

/**
 *
 * @author michaelrohs
 */
public class Calculator extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField inputA = new TextField("1");
        TextField inputB = new TextField("2");
        Label outputC = new Label();

        VBox box = new VBox(10, inputA, inputB, outputC);
        box.setPadding(new Insets(10));

        /* Listeners */
        /*
        inputA.textProperty().addListener((o, oldValue, newValue) -> {
            double result = Double.valueOf(newValue) + Double.valueOf(inputB.textProperty().getValue());
            outputC.textProperty().setValue(String.valueOf(result));
        });

        inputB.textProperty().addListener((o, oldValue, newValue) -> {
            double result = Double.valueOf(newValue) + Double.valueOf(inputA.textProperty().getValue());
            outputC.textProperty().setValue(String.valueOf(result));
        });
        */

        /* Bindings */
        DoubleBinding a = Bindings.createDoubleBinding(() -> Double.valueOf(inputA.textProperty().getValue()), inputA.textProperty());
        DoubleBinding b = Bindings.createDoubleBinding(() -> Double.valueOf(inputB.textProperty().getValue()), inputB.textProperty());
        DoubleBinding c = a.add(b);
        outputC.textProperty().bind(c.asString());
        /*
         */

        /* EventStreams */
        /*
        EventStream<Double> d_a = map(inputA.textProperty(), Double::parseDouble);
        EventStream<Double> d_b = map(inputB.textProperty(), Double::parseDouble);
        EventStream<Double> d_c = map(d_a, d_b, Double::sum);
        //EventStream<Double> d_c = map(d_a, d_b, (a, b) -> a * b);
        link(d_c, String::valueOf, outputC.textProperty());
         */

        StackPane root = new StackPane();
        root.getChildren().add(box);

        Scene scene = new Scene(root, 200, 150);

        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
