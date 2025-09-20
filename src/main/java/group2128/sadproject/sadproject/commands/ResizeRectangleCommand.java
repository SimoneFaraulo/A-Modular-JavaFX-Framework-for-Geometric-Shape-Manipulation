package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.RectangleShape;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;

/**
 * A command that enables interactive resizing of a {@link RectangleShape}.
 * <p>
 * The resizing operation scales both width and height proportionally
 * based on the mouse drag distance.
 * </p>
 *
 * <p>
 * This class extends {@link ResizeCommand} and applies the Command pattern
 * to encapsulate the resizing logic.
 * </p>
 */
public class ResizeRectangleCommand extends ResizeCommand {

    /**
     * Executes the rectangle resize interaction.
     * <p>
     * When the shape is selected and the mouse is pressed, the initial position and size
     * (center point, width, and height) are stored. During the drag gesture, the new dimensions
     * are computed based on the mouse position while maintaining the original width-to-height ratio.
     * </p>
     * <ul>
     *     <li><b>Mouse Press:</b> Saves the current width, height, and center position of the rectangle. Sets the cursor to resize mode.</li>
     *     <li><b>Mouse Drag:</b> Calculates the distance from the center to the mouse pointer. Adjusts both width and height proportionally and updates the shape accordingly.</li>
     *     <li><b>Mouse Release:</b> Restores the default cursor after resizing ends.</li>
     * </ul>
     */
    @Override
    public void execute() {

        RectangleShape shape = (RectangleShape) super.getShape();

        if (shape == null){
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.SE_RESIZE);

                double[] data = {
                        shape.getAnchorX(),
                        shape.getAnchorY(),
                        shape.getDimensionX(),
                        shape.getDimensionY()
                };
                shape.setUserData(data);
                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if(getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }
                double[] data = (double[]) shape.getUserData();
                double centerX = data[0];
                double centerY = data[1];
                double originalWidth = data[2];
                double originalHeight = data[3];

                Point2D localPoint = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double mouseX = localPoint.getX();
                double mouseY = localPoint.getY();


                double deltaX = Math.max(Math.abs(mouseX - centerX), 1);
                double deltaY = Math.max(Math.abs(mouseY - centerY), 1);

                double ratio = originalHeight/originalWidth;

                if (deltaX * ratio > deltaY) {
                    deltaY = deltaX * ratio;
                } else {
                    deltaX = deltaY / ratio;
                }

                shape.setWidth(deltaX*2);
                shape.setHeight(deltaY*2);

                shape.setX(centerX - deltaX);
                shape.setY(centerY - deltaY);

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
