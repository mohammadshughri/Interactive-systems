package eventsample;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RotateHandle extends Handle { 

    
    
    protected Point2D startRotation;
    protected boolean pressBegin = false;
    
    public RotateHandle(Rectangle rect, RectangleWithHandles handles, double initialCursorRotation) {
        super(rect, handles, initialCursorRotation);
        this.setCursor(Cursor.CLOSED_HAND);
        setFill(Color.YELLOW);
        setOpacity(0.5);
        // setEffect(new GaussianBlur(3));
    

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            rect.requestFocus();
            handles.toFront();
            handles.pressedX = e.getSceneX();
            handles.pressedY = e.getSceneY();
            
            if(pressBegin == false){
                startRotation = new Point2D(e.getSceneX(), e.getSceneY());
                pressBegin = true;
            }
            
            handles.pressedTranslateX = getTranslateX();
            handles.pressedTranslateY = getTranslateY();
            e.consume(); // prevent further processing
            System.err.println("Handle");
            handles.setHandlesVisible(false);
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            pressBegin = false;
            handles.updatePositions();
            handles.setHandlesVisible(true);
        });

    }
    
    public void updateRotation(double rotation){
    
    }

}
