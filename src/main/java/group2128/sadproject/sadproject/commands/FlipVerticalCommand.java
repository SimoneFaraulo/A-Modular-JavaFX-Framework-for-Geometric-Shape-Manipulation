package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.Node;

/**
 * A command that performs a vertical flip transformation on the selected shape.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to reflect the shape vertically by applying a negative
 * scaling factor along the Y-axis.
 * </p>
 */
public class FlipVerticalCommand extends Command {

    private SelectableShape selectedShape;

    /**
     * Executes the command to flip the selected shape vertically.
     * <p>
     * This operation multiplies the shape's current Y-scale by -1,
     * resulting in a mirror effect across the horizontal axis.
     * </p>
     */
    @Override
    public void execute() {
        ((Node) selectedShape).setScaleY(((Node) selectedShape).getScaleY() * (-1));
    }

    /**
     * Undoes the previously executed vertical flip transformation.
     * <p>
     * If a memento of the drawing canvas is available, this method restores
     * the canvas to its previous state before the transformation was applied.
     * </p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

    /**
     * Sets the shape that this command will operate on.
     *
     * @param selectedShape the shape to be flipped vertically
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }
}
