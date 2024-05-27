package eventsample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class InteractiveRectangle extends Application {

	@Override
	public void start(Stage stage) {

		Pane root = new Pane();
		Rectangle rect1 = new Rectangle(100, 100, Color.LIGHTGREEN);
		Rectangle rect2 = new Rectangle(100, 100, Color.RED);
		Rectangle rect3 = new Rectangle(100, 100, Color.LIGHTBLUE);
		Rectangle rect4 = new Rectangle(100, 100, Color.PINK);

		root.getChildren().addAll(new RectangleWithHandles(rect1));
		root.getChildren().addAll(new RectangleWithHandles(rect2));
		root.getChildren().addAll(new RectangleWithHandles(rect3));
		root.getChildren().addAll(new RectangleWithHandles(rect4));
		
		root.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getTarget() == root) {
				root.requestFocus();
			}
		});
		
		Scene scene = new Scene(root, 800, 400);
		stage.setTitle("InteractRect");
		stage.setScene(scene);
		stage.show();		
	}

	public static void main(String[] args) {
		launch(args);
	}

        
}
