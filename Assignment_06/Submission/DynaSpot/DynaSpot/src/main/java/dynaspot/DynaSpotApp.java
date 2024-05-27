package dynaspot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DynaSpotApp extends Application {

    public final int SCENE_WIDTH = 300;
    public final int SCENE_HEIGHT = 300;
    Pane targetsPane = new Pane();
    Circle selectedTarget = null;
    private static FileOutputStream logFile = null;
    private Block block = null;
    private final DynaSpot cursor = new DynaSpot();

    private void createLogFile() {
        File logFileName = new File("log.txt");
        int logNumber = 1;
        while (logFileName.exists()) {
            logFileName = new File(String.format("log-%d.txt", logNumber));
            logNumber++;
        }
        try {
            logFileName.createNewFile();
            logFile = new FileOutputStream(logFileName);
            logFile.write("block;trial;x;y;time;correct\n".getBytes());
        } catch (IOException ex) {
            System.err.println("Could not create log file.");
            System.exit(1);
        }
    }

    private void startNextBlock() {
        if (block == null) {
            block = new Block(0, logFile);
        } else {
            block = new Block(block.getNumber() + 1, logFile);
        }
        block.initTargets(30, SCENE_WIDTH, SCENE_HEIGHT);
        targetsPane.getChildren().addAll(block.getTargets());
    }

    private void updateSelectedTarget() {
        Circle t = cursor.hitTest(block.getTargets());
        if (selectedTarget != null && selectedTarget != t) {
            selectedTarget.setStrokeWidth(0.0);
        }
        if (t != null) {
            t.setStroke(Color.RED);
            t.setStrokeWidth(2.0);
        }
        selectedTarget = t;
    }

    @Override
    public void start(Stage stage) {
        createLogFile();

        Pane root = new Pane();
        root.setCursor(Cursor.NONE);

        root.getChildren().addAll(targetsPane, cursor);
        startNextBlock();

        // called at 60 Hz update rate (if mouse pointer moves)
        root.setOnMouseMoved(e -> {
            Point2D newPosition = new Point2D(e.getSceneX(), e.getSceneY());
            cursor.moveTo(newPosition);
            updateSelectedTarget();
        });

        new AnimationTimer() {
            // called at 60 Hz update rate
            @Override
            public void handle(long now) {
                updateSelectedTarget();
            }
        }.start();

        root.setOnMouseClicked((MouseEvent e) -> {
            if (selectedTarget != null) {
                block.select(selectedTarget);
                targetsPane.getChildren().remove(selectedTarget);
                selectedTarget = null;
                if (block.isFinished()) {
                    startNextBlock();
                }
            }
        });

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setTitle("DynaSpot");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
