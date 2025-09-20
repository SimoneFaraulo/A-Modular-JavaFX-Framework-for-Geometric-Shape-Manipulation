package group2128.sadproject.sadproject.memento;

import javafx.scene.layout.AnchorPane;

/**
 * The {@code Memento} interface represents a snapshot of the state of an object
 * that can be restored later. It is used to implement undo/redo functionality
 * within the application.
 */
public interface Memento {

    /**
     * Restores the state of the object to the point when this memento was created.
     */
    void restore();

    /**
     * Returns a new {@link AnchorPane} that will serve as the container for restoring the saved nodes.
     *
     * @return an empty {@code AnchorPane} ready to be populated via {@link #restore()}
     */
    AnchorPane getDrawingCanvas();
}
