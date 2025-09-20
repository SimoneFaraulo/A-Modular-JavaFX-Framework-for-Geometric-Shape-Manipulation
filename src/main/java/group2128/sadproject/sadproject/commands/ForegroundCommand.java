package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.Node;

/**
 * A command that brings the selected shape to the front of the drawing canvas.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to move the selected shape to the front using JavaFX's
 * {@code toFront()} method.
 * </p>
 */
public class ForegroundCommand extends Command {

    private SelectableShape selectedShape;

    /**
     * Executes the command to bring the selected shape to the front of the canvas.
     * <p>
     * This operation changes the rendering order of the node, placing it above all other nodes
     * in the same parent container.
     * </p>
     */
    @Override
    public void execute() {
        ((Node) selectedShape).toFront();
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

    /**
     * Sets the shape that this command will operate on.
     *
     * @param selectedShape the shape to be brought to the front
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }
}
