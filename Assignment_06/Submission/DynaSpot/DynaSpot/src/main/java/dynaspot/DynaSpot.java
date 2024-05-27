/*
 DynaSpot is described in:
 Olivier Chapuis, Jean-Baptiste Labrune, and Emmanuel Pietriga. 2009. DynaSpot: 
 Speed-dependent area cursor. In Proceedings of the SIGCHI Conference on Human 
 Factors in Compu-ting Systems (CHI '09). ACM, New York, NY, USA, 1391-1400. 
 http://dx.doi.org/10.1145/1518701.1518911
 */
package dynaspot;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

public class DynaSpot extends Group {
    private Point2D lastPosition = new Point2D(0, 0);
    private long lastTimeMoved = 0;
    private double speed = 0;

    private static final double SPOT_WIDTH = 32; // pixels
    private static final double LAG = 0.400; // sec

    private static enum SpotState {
        OFF, GROWING, ON, SHRINKING
    };

    private SpotState spotState = SpotState.OFF;
    private static final double WIDTH_TO_SPEED = 50.0;
    private static final double SPEED_TO_WIDTH = 1.0 / WIDTH_TO_SPEED;
    private static final double MIN_SPEED = 800;
    private static final double MAX_SPEED = SPOT_WIDTH * WIDTH_TO_SPEED;

    private final Circle circle = new Circle();

    public DynaSpot() {
        circle.setRadius(10.0);
        circle.setFill(Color.gray(0.7, 0.7)); // semi-transparent gray
        getChildren().add(circle);

        Path cross = new Path();
        cross.getElements().addAll(
                new MoveTo(-7, 0),
                new HLineTo(7),
                new MoveTo(0, -7),
                new VLineTo(7));
        getChildren().add(cross);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                onTimerTick(now);
            }
        }.start();
    }

    private void onTimerTick(long now) {
        double secondsTillLastMove = 0.001 * (System.currentTimeMillis() - lastTimeMoved);
        double newSpeed = lastPosition.distance(getTranslateX(), getTranslateY()) / secondsTillLastMove;

        if (newSpeed > MIN_SPEED && spotState == SpotState.OFF) {
            spotState = SpotState.GROWING;
        } else if (newSpeed < MIN_SPEED && spotState == SpotState.ON) {
            spotState = SpotState.SHRINKING;
        }

        if (spotState == SpotState.GROWING) {
            circle.setRadius(Math.min(circle.getRadius() + WIDTH_TO_SPEED * secondsTillLastMove, SPOT_WIDTH));
            if (circle.getRadius() >= SPOT_WIDTH) {
                spotState = SpotState.ON;
            }
        } else if (spotState == SpotState.SHRINKING) {
            circle.setRadius(Math.max(circle.getRadius() - WIDTH_TO_SPEED * secondsTillLastMove, 0));
            if (circle.getRadius() <= 0) {
                spotState = SpotState.OFF;
            }
        }

        lastPosition = new Point2D(getTranslateX(), getTranslateY());
        lastTimeMoved = System.currentTimeMillis();
    }

    public void moveTo(Point2D pos) {
        setTranslateX(pos.getX());
        setTranslateY(pos.getY());
    }


    // returns the circle that the DynaSpot hits, or null
    public Circle hitTest(Iterable<Circle> targets) {
        Circle closest = null;
        double closestDistance = Double.POSITIVE_INFINITY;
        double spotRadius = circle.getRadius();
        Point2D center = new Point2D(getTranslateX(), getTranslateY());
        for (Circle target : targets) {
            Point2D targetCenter = new Point2D(target.getCenterX(), target.getCenterY());
            double d = center.distance(targetCenter);
            if (d < target.getRadius() + spotRadius) {
                if (d < closestDistance) {
                    closest = target;
                    closestDistance = d;
                }
            }
        }
        return closest;
    }

}
