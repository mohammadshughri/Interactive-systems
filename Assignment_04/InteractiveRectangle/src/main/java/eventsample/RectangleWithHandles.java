package eventsample;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class RectangleWithHandles extends Group {

    protected enum MODE {
        OFF, SCALE, ROTATE
    }

    protected MODE mode;

    // the rectangle that can be modified using these handles
    protected Rectangle rect;

    // the handles around the rectangle
    protected List<Handle> handles = new ArrayList<>();
    // the handles for rotation around the rectangle
    protected List<RotateHandle> rotateHandles = new ArrayList<>();

    // initial translation state when pressed
    protected double pressedTranslateX, pressedTranslateY;

    // mouse position when pressed
    protected double pressedX, pressedY;

    public RectangleWithHandles(Rectangle rect) {
        super();

        mode = MODE.OFF;
        this.rect = rect;
        getChildren().add(rect);

        createHandles();
        getChildren().addAll(handles);
        getChildren().addAll(rotateHandles);

        updatePositions();
        setHandlesVisible(false); // handles should only be visible if this rect has focus
        setRotateHandlesVisible(false);

        // behavior for dragging the rectangle
        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            boolean isAltPressed = e.isAltDown(); // Check if Alt is pressed
            System.out.println("alt pressed: " + isAltPressed);
            if (isAltPressed && e.getButton() == MouseButton.PRIMARY) { // Check if left click with Alt
                toBack(); // Move this rectangle to the bottom of the stack
                e.consume(); // Prevent further processing
            } else {
                pressedX = e.getSceneX();
                pressedY = e.getSceneY();
                pressedTranslateX = getTranslateX();
                pressedTranslateY = getTranslateY();
                rect.requestFocus();
                toFront(); // Bring this rectangle to the front
                setHandlesVisible(false); // Handles don't need to be shown during dragging
            }
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            setTranslateX(pressedTranslateX + e.getSceneX() - pressedX);
            setTranslateY(pressedTranslateY + e.getSceneY() - pressedY);
            mode = MODE.OFF;
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            setHandlesVisible(false);
            if (mode == MODE.OFF) {
                mode = MODE.SCALE;
                setHandlesVisible(true);
                setRotateHandlesVisible(false);
                System.out.println("Mode is drag");
            } else if (mode == MODE.SCALE) {
                mode = MODE.ROTATE;
                setHandlesVisible(false);
                setRotateHandlesVisible(true);
                System.out.println("Mode is rotate");
            } else {
                mode = MODE.OFF;
                setHandlesVisible(false);
                setRotateHandlesVisible(false);
                System.out.println("Mode is off");
            }
            // rect.setEffect(null); // hide drop shadow after dragging
        });

        rect.setCursor(Cursor.OPEN_HAND);
        rect.setFocusTraversable(true);

        rect.focusedProperty().addListener((v, o, n) -> {
            // System.out.println("rect focused: " + n);
            rect.setStroke(Color.GRAY);
            rect.setStrokeWidth(n ? 1 : 0);
            setHandlesVisible(n);
        });

    }

    public final void setHandlesVisible(boolean v) {
        for (Handle h : handles) {
            h.setVisible(v);
        }
    }

    public final void setRotateHandlesVisible(boolean v) {
        for (RotateHandle h : rotateHandles) {
            h.setVisible(v);
        }
    }

    public final void updatePositions() {
        for (Handle h : handles) {
            h.positionUpdater.call();
        }
        for (RotateHandle h : rotateHandles) {
            h.positionUpdater.call();
        }
    }

    public final void updateRotations(double rotation) {
        for (Handle h : handles) {
            h.updateRotation(rotation);
        }
        for (RotateHandle h : rotateHandles) {
            h.updateRotation(rotation);
        }
    }

    private void addRotationHandles() {
        RotateHandle leftTopRotate = new RotateHandle(rect, this, 0);
        EventHandler<MouseEvent> rotateOuter = e -> {
            var rotHandle = (RotateHandle) e.getSource();
            Point2D a = rotHandle.startRotation;
            rotHandle.startRotation = new Point2D(e.getSceneX(), e.getSceneY());

            Point2D centerOfRect = localToScene(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            Point2D b = new Point2D(e.getSceneX(), e.getSceneY());

            a = a.subtract(centerOfRect);
            b = b.subtract(centerOfRect);

            double angle = Math.atan2(b.getY(), b.getX()) - Math.atan2(a.getY(), a.getX());
            angle = angle * 180.0 / Math.PI;

            Rotate rotateTransform = new Rotate();
            rotateTransform.setAngle(angle);
            rotateTransform.setPivotX(rect.getX() + rect.getWidth() / 2);
            rotateTransform.setPivotY(rect.getY() + rect.getHeight() / 2);

            this.getTransforms().add(rotateTransform);

            updateRotations(angle);

            e.consume(); // prevent further processing
        };
        leftTopRotate.addEventHandler(MouseEvent.MOUSE_DRAGGED, rotateOuter);

        leftTopRotate.positionUpdater = () -> {
            leftTopRotate.setTranslateX(rect.getX());
            leftTopRotate.setTranslateY(rect.getY());
        };

        rotateHandles.add(leftTopRotate);

        RotateHandle rightTopRotate = new RotateHandle(rect, this, 0);

        rightTopRotate.addEventHandler(MouseEvent.MOUSE_DRAGGED, rotateOuter);

        rightTopRotate.positionUpdater = () -> {
            rightTopRotate.setTranslateX(rect.getX() + rect.getWidth());
            rightTopRotate.setTranslateY(rect.getY());
        };

        rotateHandles.add(rightTopRotate);

        RotateHandle leftBottomRotate = new RotateHandle(rect, this, 0);

        leftBottomRotate.addEventHandler(MouseEvent.MOUSE_DRAGGED, rotateOuter);

        leftBottomRotate.positionUpdater = () -> {
            leftBottomRotate.setTranslateX(rect.getX());
            leftBottomRotate.setTranslateY(rect.getY() + rect.getHeight());
        };

        rotateHandles.add(leftBottomRotate);

        RotateHandle rightBottomRotate = new RotateHandle(rect, this, 0);

        rightBottomRotate.addEventHandler(MouseEvent.MOUSE_DRAGGED, rotateOuter);

        rightBottomRotate.positionUpdater = () -> {
            rightBottomRotate.setTranslateX(rect.getX() + rect.getWidth());
            rightBottomRotate.setTranslateY(rect.getY() + rect.getHeight());
        };

        rotateHandles.add(rightBottomRotate);
    }

    private void createHandles() {

        addRotationHandles();

        Handle left = new Handle(rect, this, 90);
        EventHandler<MouseEvent> leftDragged = e -> {
            Point2D s = rect.sceneToLocal(e.getSceneX(), e.getSceneY());
            double newWidth = rect.getWidth() + rect.getX() - s.getX();
            rect.setX(s.getX());
            rect.setWidth(newWidth);
            e.consume(); // prevent further processing
        };
        left.addEventHandler(MouseEvent.MOUSE_DRAGGED, leftDragged);
//        left.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> e.consume());
        left.positionUpdater = () -> {
            left.setTranslateX(rect.getX());
            left.setTranslateY(rect.getY() + rect.getHeight() / 2);
        };
        handles.add(left);


        Handle top = new Handle(rect, this, 0);
        EventHandler<MouseEvent> topDragged = e -> {
            Point2D s = rect.sceneToLocal(e.getSceneX(), e.getSceneY());
            double newHeight = rect.getHeight() + rect.getY() - s.getY();
            rect.setY(s.getY());
            rect.setHeight(newHeight);
            e.consume(); // prevent further processing
        };
        top.addEventHandler(MouseEvent.MOUSE_DRAGGED, topDragged);
//        top.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> e.consume());
        top.positionUpdater = () -> {
            top.setTranslateX(rect.getX() + rect.getWidth() / 2);
            top.setTranslateY(rect.getY());
        };
        handles.add(top);

        // add right
        Handle right = new Handle(rect, this, -90);
        EventHandler<MouseEvent> rightDragged = e -> {
            Point2D s = rect.sceneToLocal(e.getSceneX(), e.getSceneY());
            double newWidth = s.getX() - rect.getX();
            rect.setWidth(newWidth);
            e.consume(); // prevent further processing
        };
        right.addEventHandler(MouseEvent.MOUSE_DRAGGED, rightDragged);
//        right.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> e.consume());
        right.positionUpdater = () -> {
            right.setTranslateX(rect.getX() + rect.getWidth());
            right.setTranslateY(rect.getY() + rect.getHeight() / 2);
        };
        handles.add(right);

        // add bottom
        Handle bottom = new Handle(rect, this, 180);
        EventHandler<MouseEvent> bottomDragged = e -> {
            Point2D s = rect.sceneToLocal(e.getSceneX(), e.getSceneY());
            double newHeight = s.getY() - rect.getY();
            rect.setHeight(newHeight);
            e.consume(); // prevent further processing
        };
        bottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, bottomDragged);
        bottom.positionUpdater = () -> {
            bottom.setTranslateX(rect.getX() + rect.getWidth() / 2);
            bottom.setTranslateY(rect.getY() + rect.getHeight());
        };
        handles.add(bottom);

        Handle leftTop = new Handle(rect, this, -45);
        leftTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, leftDragged);
        leftTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, topDragged);
        leftTop.positionUpdater = () -> {
            leftTop.setTranslateX(rect.getX());
            leftTop.setTranslateY(rect.getY());
        };
        handles.add(leftTop);

        // add right-top
        Handle rightTop = new Handle(rect, this, 45);
        rightTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, rightDragged);
        rightTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, topDragged);
        rightTop.positionUpdater = () -> {
            rightTop.setTranslateX(rect.getX() + rect.getWidth());
            rightTop.setTranslateY(rect.getY());
        };
        handles.add(rightTop);

        // add left-bottom
        Handle leftBottom = new Handle(rect, this, -135);
        leftBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, leftDragged);
        leftBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, bottomDragged);
        leftBottom.positionUpdater = () -> {
            leftBottom.setTranslateX(rect.getX());
            leftBottom.setTranslateY(rect.getY() + rect.getHeight());
        };
        handles.add(leftBottom);

        // add right-bottom
        Handle rightBottom = new Handle(rect, this, 135);
        rightBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, rightDragged);
        rightBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, bottomDragged);
        rightBottom.positionUpdater = () -> {
            rightBottom.setTranslateX(rect.getX() + rect.getWidth());
            rightBottom.setTranslateY(rect.getY() + rect.getHeight());
        };
        handles.add(rightBottom);
    }

}
