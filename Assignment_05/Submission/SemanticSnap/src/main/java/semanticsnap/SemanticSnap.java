package semanticsnap;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

abstract class Target {
    public Point2D center;
    public Circle shape;

    public Target(double x, double y) {
        center = new Point2D(x, y);
        shape = new Circle(30);
        shape.setTranslateX(x);
        shape.setTranslateY(y);
    }

    abstract public Point2D snap(Circle cursor);
}

class AttractingTarget extends Target {
    public AttractingTarget(double x, double y) {
        super(x, y);
        shape.setFill(Color.CYAN);
    }

    @Override
    public Point2D snap(Circle cursor) {
        Point2D cursorCenter = new Point2D(cursor.getTranslateX(), cursor.getTranslateY());
        double cursorRadius = cursor.getRadius();
        double targetRadius = shape.getRadius();
        double distance = cursorCenter.distance(center);
        if (distance < (cursorRadius + targetRadius)) {
            // Cursor is within the target, so snap to target center
            return center;
        } else {
            // Cursor is outside the target, so it stays where it is
            return cursorCenter;
        }
    }
}

class RepellingTarget extends Target {
    public RepellingTarget(double x, double y) {
        super(x, y);
        shape.setFill(Color.RED);
    }

    @Override
    public Point2D snap(Circle cursor) {
        Point2D cursorCenter = new Point2D(cursor.getTranslateX(), cursor.getTranslateY());
        double cursorRadius = cursor.getRadius();
        double targetRadius = shape.getRadius();
        double distance = cursorCenter.distance(center);
        double minimumAllowedDistance = cursorRadius + targetRadius;
        if (distance < minimumAllowedDistance) {
            // If they overlap, move the cursor outside the repelling target
            Point2D direction = cursorCenter.subtract(center).normalize(); // Direction from target to cursor
            Point2D offset = direction.multiply(minimumAllowedDistance - distance); // Move by the overlap distance

            // New position to keep the cursor outside the repelling target
            return cursorCenter.add(offset);
        } else {
            // If there's no overlap, keep the cursor as is
            return cursorCenter;
        }
    }
}

public class SemanticSnap extends Application {

    Circle cursor = new Circle(20, Color.BLUE);
    Target[] targets = {
            new AttractingTarget(100, 100),
            new RepellingTarget(400, 100),
            new AttractingTarget(400, 400),
            new RepellingTarget(100, 400),
    };

    private Target closestTarget(Point2D p) {
        double dMin = Double.POSITIVE_INFINITY;
        Target result = targets[0];
        for (Target t : targets) {
            double d = p.distance(t.center);
            if (d < dMin) {
                dMin = d;
                result = t;
            }
        }
        return result;
    }

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        for (Target t : targets) {
            root.getChildren().add(t.shape);
        }
        root.getChildren().add(cursor);

        // Create a label to display the snapping status
        Label statusLabel = new Label();
        statusLabel.setTranslateX(10); // Set the position of the label
        statusLabel.setTranslateY(10);

        // Create a boolean property for snapping status
        SimpleBooleanProperty isSnapping = new SimpleBooleanProperty(true);

        // Bind the label text to the snapping status
        statusLabel.textProperty().bind(Bindings.when(isSnapping)
                .then("Snapping: ON")
                .otherwise("Snapping: OFF"));

        root.getChildren().add(statusLabel);

        root.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            Point2D p = new Point2D(e.getSceneX(), e.getSceneY());
            cursor.setTranslateX(p.getX());
            cursor.setTranslateY(p.getY());
            if (isSnapping.get()) {
                Target t = closestTarget(p);
                Point2D q = t.snap(cursor);
                cursor.setTranslateX(q.getX());
                cursor.setTranslateY(q.getY());
            }
        });

        Scene scene = new Scene(root, 500, 500);

        // Add a key listener to the scene
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S) {
                isSnapping.set(!isSnapping.get()); // Toggle the snapping status
            }
        });

        stage.setTitle("Attract-Repel");
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