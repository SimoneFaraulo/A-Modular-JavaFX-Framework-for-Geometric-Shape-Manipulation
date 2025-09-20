package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.scene.text.Text;

/**
 * A command that changes the font size of the selected text shape.
 * <p>
 * This class extends the abstract {@link Command} class and overrides the
 * {@link #execute()} method to update the font size of a {@link Text} node.
 * It also supports undo functionality by restoring the previous font size
 * using the drawing canvas memento.
 * </p>
 */
public class FontSizeCommand extends Command {

    private SelectableShape selectedShape;
    private double newFontSize;

    /**
     * Executes the command to set the new font size on the selected text shape.
     * <p>
     * If the selected shape is an instance of {@link Text}, its font size is updated.
     * The previous state of the canvas should be saved externally for undo support.
     * </p>
     */
    @Override
    public void execute() {
        if (selectedShape instanceof Text) {
            Text textNode = (Text) selectedShape;
            textNode.setFont(javafx.scene.text.Font.font(newFontSize));
        }
    }

    /**
     * Undoes the font size change by restoring the previous state of the drawing canvas.
     * <p>
     * This method relies on a memento of the canvas being available to restore.
     * </p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

    /**
     * Sets the shape on which the font size change will be applied.
     *
     * @param selectedShape the selected shape, expected to be a {@link Text} node
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }

    /**
     * Sets the new font size to apply to the selected shape.
     *
     * @param newFontSize the new font size in points
     */
    public void setFontSize(double newFontSize) {
        this.newFontSize = newFontSize;
    }
}
