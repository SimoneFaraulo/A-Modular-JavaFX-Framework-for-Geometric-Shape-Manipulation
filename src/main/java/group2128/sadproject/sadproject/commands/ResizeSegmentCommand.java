package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SegmentShape;
import javafx.scene.Cursor;

/**
 * A command that allows the resizing (or endpoint repositioning) of a {@link SegmentShape}.
 * <p>
 * This class enables interactive dragging of the segment’s endpoint,
 * effectively allowing the user to resize and rotate the segment.
 * </p>
 *
 * <p>
 * Extends {@link ResizeCommand} and applies the Command pattern to encapsulate
 * endpoint manipulation logic.
 * </p>
 */
public class ResizeSegmentCommand extends ResizeCommand {

    /**
     * Executes the resize operation for a {@link SegmentShape} by enabling endpoint dragging.
     * <p>
     * When the segment is selected:
     * </p>
     * <ul>
     *     <li><b>Mouse Press:</b> Stores the initial start point of the segment and changes the cursor to a resize style.</li>
     *     <li><b>Mouse Drag:</b> Continuously updates the endpoint of the segment as the user drags the mouse, effectively resizing the segment.</li>
     *     <li><b>Mouse Release:</b> Restores the default cursor once the resize operation ends.</li>
     * </ul>
     * <p>
     * This behavior allows the user to interactively control the length and orientation of the segment.
     * </p>
     */
    @Override
    public void execute() {
        SegmentShape shape = (SegmentShape) super.getShape();

        if (shape == null){
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.SE_RESIZE);

                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if(getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }
                double localX = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY()).getX();
                double localY = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY()).getY();

                shape.setEndPointX(localX);
                shape.setEndPointY(localY);

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
