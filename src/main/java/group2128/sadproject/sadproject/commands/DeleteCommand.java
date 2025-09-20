package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;

/**
 * A concrete command that handles the deletion of a selected shape
 * from the drawing canvas.
 * <p>
 * This class extends the abstract {@link Command} class and implements
 * the logic to remove a given {@link SelectableShape} from the canvas.
 * </p>
 */
public class DeleteCommand extends Command {

    /**
     * The shape selected for deletion.
     */
    private SelectableShape selectedShape;

    /**
     * Executes the delete operation by removing the selected shape
     * from the drawing canvas.
     */
    @Override
    public void execute() {
        getDrawingCanvas().getChildren().remove(selectedShape);
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
     * Sets the shape that should be deleted.
     *
     * @param selectedShape the {@link SelectableShape} to be removed
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }
}
