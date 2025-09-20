package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.Node;
/**
 * A command that performs a horizontal flip transformation on the selected shape.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to reflect the shape horizontally by applying a negative
 * scaling factor along the X-axis.
 * </p>
 */
public class FlipHorizontalCommand extends Command {

    private SelectableShape selectedShape;

    /**
     * Executes the command to flip the selected shape horizontally.
     * <p>
     * This operation multiplies the shape's current X-scale by -1,
     * resulting in a mirror effect across the vertical axis.
     * </p>
     */
    @Override
    public void execute() {
        ((Node) selectedShape).setScaleX(((Node) selectedShape).getScaleX() * (-1));
    }

    /**
     * Undoes the previously executed horizontal flip transformation.
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
     * @param selectedShape the shape to be flipped horizontally
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }
}
