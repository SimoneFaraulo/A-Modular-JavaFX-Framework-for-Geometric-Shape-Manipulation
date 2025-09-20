package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.TextShape;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;

/**
 * A command that enables interactive resizing of a {@link TextShape}.
 * <p>
 * The resizing operation scales the text uniformly by adjusting its scaleX and scaleY
 * based on the distance dragged from the center.
 * </p>
 */
public class ResizeTextCommand extends ResizeCommand {

    @Override
    public void execute() {
        TextShape shape = (TextShape) getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.SE_RESIZE);

                Point2D center = new Point2D(
                        shape.getBoundsInParent().getMinX() + shape.getBoundsInParent().getWidth() / 2,
                        shape.getBoundsInParent().getMinY() + shape.getBoundsInParent().getHeight() / 2
                );

                double initialScale = shape.getScaleX();
                Point2D base = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());

                Object[] data = new Object[]{ center, base, initialScale };
                shape.setUserData(data);
                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                Object[] data = (Object[]) shape.getUserData();
                Point2D center = (Point2D) data[0];
                Point2D base = (Point2D) data[1];
                double initialScale = (double) data[2];

                Point2D current = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double baseDistance = base.distance(center);
                double currentDistance = current.distance(center);

                double scale = currentDistance / baseDistance;
                double newScale = initialScale * scale;

                shape.setScaleX(newScale);
                shape.setScaleY(newScale);

                shape.setWrappingWidth(shape.getWrappingWidth()/newScale);

                event.consume();
            }
        });

        shape.setOnMouseReleased(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.DEFAULT);
                setShouldTriggerInteraction(true);
            }
        });
    }

    /**
     * Undoes the previously executed fill color change command.
     *
     * <p>If a memento of the drawing canvas is available, this method restores
     * the canvas to its previous state before the fill color was changed.</p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

}
