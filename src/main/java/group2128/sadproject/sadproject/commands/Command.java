package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.memento.DrawingCanvasMemento;
import javafx.scene.layout.AnchorPane;

/**
 * Represents an abstract command that can be executed within the drawing application.
 * <p>
 * Concrete subclasses should implement the {@code execute()} method to define specific behavior.
 * This class is intended to be used as part of the Command design pattern.
 * </p>
 */
public abstract class Command {

    /**
     * The canvas on which the drawing operations are performed.
     */
    private AnchorPane drawingCanvas;

    /**
     * The memento object that stores a snapshot of the canvas state for undo functionality.
     */
    private DrawingCanvasMemento drawingCanvasMemento;

    /**
     * Returns the current {@link DrawingCanvasMemento} associated with this command.
     *
     * <p>The memento holds the saved state of the drawing canvas and can be used
     * to restore the canvas to a previous state, typically during an undo operation.</p>
     *
     * @return the {@code DrawingCanvasMemento} containing the saved canvas state
     */
    public DrawingCanvasMemento getDrawingCanvasMemento() {
        return drawingCanvasMemento;
    }

    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    public abstract void execute();

    /**
     * Undoes the previously executed command.
     * <p>
     * Subclasses must implement this method to restore the previous state of the canvas,
     * typically using the stored memento.
     * </p>
     */
    public abstract void undo();

    /**
     * Returns the drawing canvas associated with this command.
     *
     * @return the {@link AnchorPane} representing the drawing canvas
     */
    public AnchorPane getDrawingCanvas() {
        return drawingCanvas;
    }

    /**
     * Sets the drawing canvas for this command.
     *
     * @param drawingCanvas the {@link AnchorPane} to be associated with this command
     */
    public void setDrawingCanvas(AnchorPane drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
    }

    /**
     * Stores a backup of the current canvas state using a memento.
     * <p>
     * This method is typically used before executing a command that modifies the canvas,
     * allowing the state to be restored later if needed.
     * </p>
     */
    public void saveBackup() {
        drawingCanvasMemento = new DrawingCanvasMemento(drawingCanvas);
    }

}

