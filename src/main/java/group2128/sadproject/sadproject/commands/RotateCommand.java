package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.Node;

/**
 * A command that performs a rotation on the selected shape.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to rotate the shape by a specified angle in degrees.
 * </p>
 */
public class RotateCommand extends Command {

    private SelectableShape selectedShape;
    private double angle;

    /**
     * Executes the command to rotate the selected shape.
     * <p>
     * This operation adds the specified angle to the shape's current rotation,
     * effectively rotating it around its center.
     * </p>
     */
    @Override
    public void execute() {
        Node node = (Node) selectedShape;
        node.setRotate(angle);
    }

    /**
     * Undoes the previously executed rotation transformation.
     * <p>
     * Restores the shape to the rotation angle it had before this command was executed.
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
     * @param selectedShape the shape to be rotated
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }

    /**
     * Sets the rotation angle to apply during execution.
     *
     * @param angle the angle in degrees to rotate the shape
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }
}
