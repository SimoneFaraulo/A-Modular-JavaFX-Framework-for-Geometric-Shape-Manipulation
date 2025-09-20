package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.EllipseShape;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;

/**
 * A command that enables interactive resizing of an {@link EllipseShape}
 * while maintaining its aspect ratio.
 * <p>
 * The resizing is performed by dragging the mouse, with the scaling
 * proportional to the user's drag distance.
 * </p>
 *
 * <p>
 * This class implements the {@link ResizeCommand} abstract base and
 * applies the Command pattern for shape manipulation.
 * </p>
 */
public class ResizeEllipseCommand extends ResizeCommand {

    /**
     * Executes the ellipse resize interaction.
     * <p>
     * When the shape is selected and the mouse is pressed on it, the initial state
     * (radii and center coordinates) is saved. During the drag gesture, the radii
     * are recalculated based on the distance from the center point, ensuring that the
     * ellipse keeps its original aspect ratio.
     * </p>
     * <ul>
     *     <li><b>Mouse Press:</b> Stores the current radii and center of the ellipse, and sets the cursor to resize mode.</li>
     *     <li><b>Mouse Drag:</b> Computes new radii based on mouse position and applies aspect ratio-preserving resizing.</li>
     *     <li><b>Mouse Release:</b> Restores the default cursor.</li>
     * </ul>
     */
    @Override
    public void execute() {

        EllipseShape shape = (EllipseShape) super.getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.SE_RESIZE);

                double[] data = {
                        shape.getDimensionX(),
                        shape.getDimensionY(),
                        shape.getAnchorX(),
                        shape.getAnchorY()
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
                double originalDimensionX = data[0];
                double originalDimensionY = data[1];
                double centerX = data[2];
                double centerY = data[3];

                Point2D localPoint = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double mouseX = localPoint.getX();
                double mouseY = localPoint.getY();


                double deltaX = Math.max(Math.abs(mouseX - centerX), 1);
                double deltaY = Math.max(Math.abs(mouseY - centerY), 1);

                double ratio = originalDimensionY / originalDimensionX;

                if (deltaX * ratio > deltaY) {
                    deltaY = deltaX * ratio;
                } else {
                    deltaX = deltaY / ratio;
                }

                shape.setDimensionX(deltaX);
                shape.setDimensionY(deltaY);

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
