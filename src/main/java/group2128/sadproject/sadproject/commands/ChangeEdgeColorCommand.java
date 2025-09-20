package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.paint.Color;

/**
 * A command that changes the edge color of a selected shape on the canvas.
 *
 * <p>This command follows the Command pattern, allowing the edge color of a shape
 * to be changed and subsequently undone using a memento-based restore mechanism.</p>
 *
 * @see Command
 * @see SelectableShape
 */
public class ChangeEdgeColorCommand extends Command {

    private SelectableShape shape;
    private Color selectedColor;

    /**
     * Executes the command to change the edge color of the selected shape.
     *
     * <p>This operation updates the shape's edge color to the value provided via
     * {@link #setSelectedColor(Color)}.</p>
     */
    @Override
    public void execute() {
        shape.setEdgeColor(selectedColor);
    }

    /**
     * Undoes the previously executed edge color change command.
     *
     * <p>If a memento of the drawing canvas is available, this method restores
     * the canvas to its previous state before the edge color was changed.</p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

    /**
     * Sets the shape whose edge color will be changed by this command.
     *
     * @param shape the {@link SelectableShape} to be modified
     */
    public void setSelectedShape(SelectableShape shape) {
        this.shape = shape;
    }

    /**
     * Sets the new edge color to apply to the selected shape.
     *
     * @param selectedColor the {@link Color} to be applied as the shape's edge color
     */
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }
}
