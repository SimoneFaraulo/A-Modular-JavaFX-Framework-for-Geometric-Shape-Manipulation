package group2128.sadproject.sadproject.commands;

/**
 * A placeholder command representing a generic user interaction such as drawing or modifying a shape.
 * <p>
 * This command does not perform any specific action when executed. It is mainly used
 * to mark a change in the drawing state for the purpose of supporting undo functionality.
 * </p>
 *
 * <p>
 * For example, this command can be pushed to the {@code CommandHistory} stack to allow
 * the system to revert to a previous canvas state even if no explicit command action was performed.
 * </p>
 */
public class InteractionCommand extends Command {

    /**
     * Executes the command.
     * <p>
     * This implementation does nothing, as {@code InteractionCommand} is used only as a marker
     * for state changes that should be undoable (e.g., drawing or modifying shapes).
     * </p>
     */
    @Override
    public void execute() {
        // Do nothing — used only for Undo support
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

}
