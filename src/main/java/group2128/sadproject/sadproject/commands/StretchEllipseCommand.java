package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.EllipseShape;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;

public class StretchEllipseCommand extends StretchCommand{

    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    @Override
    public void execute() {
        EllipseShape shape = (EllipseShape) super.getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {

                shape.setUserData(new double[]{
                        shape.getAnchorX(),
                        shape.getAnchorY(),
                });

                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                double[] data = (double[]) shape.getUserData();
                double anchorX = data[0];
                double anchorY = data[1];

                Point2D localPoint = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double mouseX = localPoint.getX();
                double mouseY = localPoint.getY();

                double newDimensionX = Math.abs(mouseX - anchorX);
                double newDimensionY = Math.abs(mouseY - anchorY);

                shape.setDimensionX(newDimensionX);
                shape.setDimensionY(newDimensionY);

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
     * Undoes the previously executed command.
     * <p>
     * Subclasses must implement this method to restore the previous state of the canvas,
     * typically using the stored memento.
     * </p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }
}
