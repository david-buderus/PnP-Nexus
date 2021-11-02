package de.pnp.manager.ui.part;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue;
    private final double zoomIntensity;
    private Node target;
    private Node zoomNode;
    private ChangeListener<Node> listener;

    public ZoomableScrollPane() {
        this(0.002);
    }

    public ZoomableScrollPane(double zoomIntensity) {
        super();
        this.listener = (ob, o, n) -> {
            this.contentProperty().removeListener(listener);
            this.target = n;
            this.scaleValue = 1;
            this.zoomNode = new Group(target);
            this.setContent(createCenteringBox(zoomNode));
            this.updateScale();
            this.contentProperty().addListener(listener);
        };
        this.zoomIntensity = zoomIntensity;
        this.scaleValue = 1;

        this.contentProperty().addListener(listener);
        this.setPannable(true);
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }

    private Node createCenteringBox(Node node) {
        VBox box = new VBox(node);
        box.setAlignment(Pos.CENTER);
        box.setOnScroll(event -> {
            if (event.isControlDown()) {
                onScroll(event.getDeltaY(), new Point2D(event.getX(), event.getY()));
                event.consume();
            }
        });
        return box;
    }

    private void updateScale() {
        this.target.setScaleX(this.scaleValue);
        this.target.setScaleY(this.scaleValue);
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * this.zoomIntensity);

        Bounds innerBounds = this.zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        this.scaleValue *= zoomFactor;
        //if (scaleValue < 1){
        //    scaleValue = 1;
        //}

        this.updateScale();
        this.layout();

        Point2D posInZoomTarget = this.target.parentToLocal(this.zoomNode.parentToLocal(mousePoint));
        Point2D adjustment = this.target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        Bounds updatedInnerBounds = this.zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}
