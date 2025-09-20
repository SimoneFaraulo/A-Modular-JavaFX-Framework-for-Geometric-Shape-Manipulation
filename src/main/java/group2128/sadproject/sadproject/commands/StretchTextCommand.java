package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.TextShape;
import javafx.scene.Cursor;

public class StretchTextCommand extends StretchCommand{

    /**
     * Stores the initial X-coordinate of the mouse during the drag operation.
     * Used to calculate horizontal scaling of the text shape.
     */
    private double dragOffsetX;

    /**
     * Stores the initial Y-coordinate of the mouse during the drag operation.
     * Used to calculate vertical scaling of the text shape.
     */
    private double dragOffsetY;

    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    @Override
    public void execute() {
        TextShape shape = (TextShape) super.getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {

                dragOffsetX = event.getSceneX();
                dragOffsetY = event.getSceneY();

                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                double deltaX = event.getSceneX() - dragOffsetX;
                double deltaY = event.getSceneY() - dragOffsetY;

                double scaleX = Math.max(0.1, shape.getScaleX() + deltaX/100);
                double scaleY = Math.max(0.1, shape.getScaleY() + deltaY/100);

                shape.setDimensionX(scaleX);
                shape.setDimensionY(scaleY);

                dragOffsetX = event.getSceneX();
                dragOffsetY = event.getSceneY();
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
