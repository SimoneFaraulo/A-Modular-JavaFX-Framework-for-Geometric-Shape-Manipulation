package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.Node;

/**
 * A command that sends the selected shape to the back of the drawing canvas.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to move the selected shape to the back using JavaFX's
 * {@code toBack()} method.
 * </p>
 */
public class BackgroundCommand extends Command {

    private SelectableShape selectedShape;

    /**
     * Executes the command to send the selected shape to the back of the canvas.
     * <p>
     * This operation changes the rendering order of the node, placing it behind all other nodes
     * in the same parent container.
     * </p>
     */
    @Override
    public void execute() {
        ((Node) selectedShape).toBack();
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

    /**
     * Sets the shape that this command will operate on.
     *
     * @param selectedShape the shape to be sent to the back
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }



}
