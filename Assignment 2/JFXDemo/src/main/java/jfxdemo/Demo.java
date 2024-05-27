package jfxdemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Demo extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FlowPane flow = new FlowPane();

        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(5);
        flow.setHgap(5);

        CustomPane custom = new CustomPane();

        for (int i = 0; i < 100; i++) {
            Color c = new Color((double)i / 100, 0, 0, 1);
            Rectangle r = new Rectangle(50, 50, c);
            flow.getChildren().add(r);
        }

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setContent(flow);

        Scene scene = new Scene(scroll, 800, 600);
        stage.setTitle("JavaFX Demo");
        stage.setScene(scene);
        stage.show();
    }
}
