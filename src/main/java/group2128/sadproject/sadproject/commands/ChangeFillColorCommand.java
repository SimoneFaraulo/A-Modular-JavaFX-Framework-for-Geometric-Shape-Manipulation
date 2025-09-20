package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.paint.Color;

/**
 * A command that changes the fill color of a selected shape on the canvas.
 *
 * <p>This command follows the Command pattern, allowing the fill color of a shape
 * to be changed and subsequently undone using a memento-based restore mechanism.</p>
 *
 * @see Command
 * @see SelectableShape
 */
public class ChangeFillColorCommand extends Command {

    private SelectableShape shape;
    private Color selectedColor;

    /**
     * Executes the command to change the fill color of the selected shape.
     *
     * <p>This operation updates the shape's fill color to the value provided via
     * {@link #setSelectedColor(Color)}.</p>
     */
    @Override
    public void execute() {
        shape.setFillColor(this.selectedColor);
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
     * Sets the shape whose fill color will be changed by this command.
     *
     * @param shape the {@link SelectableShape} to be modified
     */
    public void setSelectedShape(SelectableShape shape) {
        this.shape = shape;
    }

    /**
     * Sets the new fill color to apply to the selected shape.
     *
     * @param selectedColor the {@link Color} to be applied as the shape's fill color
     */
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }
}
