package sinelayout;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SineLayout extends Application {

	@Override
	public void start(Stage stage) {
		SinePane sinePane = new SinePane();
		sinePane.setPadding(new Insets(10, 10, 10, 10));
		sinePane.setSpacing(10);

		/**
		 * @todo a) explain what happens when the button is pressed
		 *       When the "Add" button is pressed, a new button labeled "Button" is
		 *       created.
		 *       This button is then added to the sinePane object, which is an instance
		 *       of the SinePane class.
		 *       The sinePane is a custom layout that extends the VBox class, so adding
		 *       the button to it will cause
		 *       the button to be displayed vertically in the layout.
		 */
		Button add = new Button("Add");
		add.setOnAction((ActionEvent event) -> {
			Button b = new Button("Button");
			sinePane.getChildren().add(b);
		});

		Button remove = new Button("Remove");
		remove.setOnAction((ActionEvent event) -> {
			ObservableList<Node> children = sinePane.getChildren();
			if (children.size() > 0) {
				children.remove(0);
			}
		});

		Button incSpacing = new Button("+ Spacing");
		incSpacing.setOnAction((ActionEvent event) -> {
			sinePane.setSpacing(Math.min(100, sinePane.getSpacing() + 2));
		});

		Button decSpacing = new Button("- Spacing");
		decSpacing.setOnAction((ActionEvent event) -> {
			sinePane.setSpacing(Math.max(0, sinePane.getSpacing() - 2));
		});

		BorderPane root = new BorderPane();
		ScrollPane sp = new ScrollPane(sinePane);
		sp.setFitToWidth(true);
		sp.setFitToHeight(true);
		root.setCenter(sp);
		HBox buttons = new HBox(5);
		buttons.setPadding(new Insets(5));
		buttons.getChildren().addAll(add, remove, incSpacing, decSpacing);
		root.setBottom(buttons);

		Scene scene = new Scene(root, 300, 500);

		stage.setTitle("Sine Pane");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
