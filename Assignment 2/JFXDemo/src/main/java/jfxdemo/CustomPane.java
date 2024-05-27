package jfxdemo;

import javafx.scene.Node;

import java.util.List;
import java.util.Random;

public class CustomPane extends javafx.scene.layout.Pane {
    @Override
    protected void layoutChildren() {
        List<Node> nodes = getManagedChildren();
        Random rand = new Random();
        for(Node node : nodes) {
            node.autosize();
            node.setLayoutX(rand.nextDouble() * getWidth());
            node.setLayoutY(rand.nextDouble() * getHeight());
        }
    }
}
