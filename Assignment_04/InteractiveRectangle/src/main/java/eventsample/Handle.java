package eventsample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;

public class Handle extends Rectangle { 

    protected Procedure positionUpdater;
    
    protected ImageView iv;
    protected Canvas cv;

    public Handle(Rectangle rect, RectangleWithHandles handles, double initialCursorRotation) {
        super(-10, -10, 20, 20);
        setFill(Color.BLUE);
        setOpacity(0.5);
        // setEffect(new GaussianBlur(3));
    

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            rect.requestFocus();
            handles.toFront();
            handles.pressedX = e.getSceneX();
            handles.pressedY = e.getSceneY();
            handles.pressedTranslateX = getTranslateX();
            handles.pressedTranslateY = getTranslateY();
            e.consume(); // prevent further processing
            System.err.println("Handle");
            handles.setHandlesVisible(false);
        });

        
        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (!e.isStillSincePress()) {
            handles.updatePositions();
            handles.setHandlesVisible(true);
            }
        });

        try {
            var inputstream = new FileInputStream("cursor.png");
            var image = new Image(inputstream);
            
            cv = new Canvas();
            iv = new ImageView(image);        
            var imageRotate = new Rotate();
            
            imageRotate.setAngle(initialCursorRotation);
            imageRotate.setPivotX(iv.getImage().getWidth()/2);
            imageRotate.setPivotY(iv.getImage().getHeight()/2);
            
            var scale = new Scale();
            scale.setX(0.06);
            scale.setY(0.06);
            iv.getTransforms().add(scale);
            iv.getTransforms().add(imageRotate);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image rotatedImage = iv.snapshot(params, null);
            
            var gc = cv.getGraphicsContext2D();
            gc.drawImage(rotatedImage, 0, 0);
            this.setCursor(new ImageCursor(rotatedImage, rotatedImage.getWidth() / 2, rotatedImage.getHeight() /2));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RectangleWithHandles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void updateRotation(double rotation){
     
            var imageRotate = new Rotate();
            
            imageRotate.setAngle(rotation);
            imageRotate.setPivotX(iv.getImage().getWidth()/2);
            imageRotate.setPivotY(iv.getImage().getHeight()/2);
            
            iv.getTransforms().add(imageRotate);
            var params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image rotatedImage = iv.snapshot(params, null);
            
            var gc = cv.getGraphicsContext2D();
            gc.drawImage(rotatedImage, 0, 0);
            this.setCursor(new ImageCursor(rotatedImage, rotatedImage.getWidth() / 2, rotatedImage.getHeight() /2));
            
    }

}
