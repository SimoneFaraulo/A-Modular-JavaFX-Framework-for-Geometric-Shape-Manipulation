package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;

/**
 * Command to create a copy of a selected shape.
 * <p>
 * This command stores a reference to the shape to be copied and,
 * upon execution, generates a new copy of that shape.
 */
public class CopyCommand extends Command {

    /** The shape that will be copied and the result of the copy operation. */
    private SelectableShape copiedShape;

    /**
     * Executes the copy operation.
     * <p>
     * Creates a new copy of the selected shape by invoking its {@code getCopy} method.
     * The copied shape is stored internally and can be retrieved using {@link #getCopiedShape()}.
     */
    @Override
    public void execute() {
        this.copiedShape = copiedShape.getCopy();
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
     * Sets the shape to be copied.
     *
     * @param copiedShape the shape currently selected and to be copied
     */
    public void setSelectedShape(SelectableShape copiedShape) {
        this.copiedShape = copiedShape;
    }

    /**
     * Returns the copied shape after the command has been executed.
     *
     * @return the copied shape instance
     */
    public SelectableShape getCopiedShape() {
        return copiedShape;
    }
}
