// bug: update problem, netbeans, da static!
package dynaspot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Block {

    private final FileOutputStream logFile;
    private final int blockNumber;
    private int trial;
    private ArrayList<Circle> targets;

    public Block(int blockNumber, FileOutputStream logFile) {
        this.logFile = logFile;
        this.blockNumber = blockNumber;
    }

    public void initTargets(int n, int width, int height) {
        Random rnd = new Random(blockNumber);

        targets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double cx = rnd.nextDouble() * width;
            double cy = rnd.nextDouble() * height;
            Circle c = new Circle(cx, cy, 3, Color.LIGHTBLUE);
            targets.add(c);
        }
        // first target is the highlighted target (should be selected next)
        targets.get(0).setFill(Color.GREEN);
    }

    public ArrayList<Circle> getTargets() {
        return targets;
    }

    // is called when the user selects the target
    public void select(Circle target) {
        try {
            // Format: block;trial;x;y;time;correct
            logFile.write(String.format("%d;%d;%f;%f;%d;%s\n",
                    blockNumber,
                    trial++,
                    target.getCenterX(),
                    target.getCenterY(),
                    System.currentTimeMillis(),
                    target == targets.get(0) ? "TRUE" : "FALSE"
            ).getBytes());
        } catch (IOException ex) {
            System.err.println("Could not write to log file!");
            // ignore
        }
        targets.remove(target);
        if (!targets.isEmpty()) {
            targets.get(0).setFill(Color.GREEN);
        }
    }

    public boolean isFinished() {
        return targets.isEmpty();
    }

    public int getNumber() {
        return blockNumber;
    }
}
