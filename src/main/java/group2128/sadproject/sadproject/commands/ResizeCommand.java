package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.SelectableShape;

/**
 * An abstract command that provides common behavior for resizing shapes.
 * <p>
 * This class serves as a base for concrete resize command implementations
 * that modify the size or dimensions of {@link SelectableShape} instances.
 * </p>
 *
 * <p>
 * Subclasses must implement the {@link Command#execute()} method to define
 * how the shape should be resized.
 * </p>
 */
public abstract class ResizeCommand extends Command {

    /**
     * The shape that will be resized by this command.
     */
    private SelectableShape shape;

    /**
     * A flag used to ensure that the interaction trigger logic is executed only once per interaction session.
     * <p>
     * This variable prevents redundant state changes or repeated calls to interaction-related logic
     * (e.g., setting interactionProperty to true) during user gestures such as dragging or resizing.
     * It is typically reset externally when a new interaction cycle is expected.
     * </p>
     */
    private boolean shouldTriggerInteraction = true;

    /**
     * Sets the shape to be resized by this command.
     *
     * @param shape the {@link SelectableShape} to associate with this command
     */
    public void setShape(SelectableShape shape) {
        this.shape = shape;
    }

    /**
     * Returns the shape associated with this resize command.
     *
     * @return the {@link SelectableShape} that will be resized
     */
    public SelectableShape getShape() {
        return this.shape;
    }

    /**
     * Sets the interaction trigger flag for this resize command.
     * <p>
     * This flag is used to control whether interaction-related logic
     * (such as enabling interaction mode on a shape) should be triggered
     * during the execution of this command. It is typically set to {@code false}
     * after the interaction has been handled to avoid redundant actions.
     * </p>
     *
     * @param shouldTriggerInteraction {@code true} if the interaction logic should be triggered;
     *                                  {@code false} otherwise
     */
    public void setShouldTriggerInteraction(boolean shouldTriggerInteraction){
        this.shouldTriggerInteraction = shouldTriggerInteraction;
    }

    /**
     * Returns whether the interaction logic should be triggered for this command.
     *
     * @return {@code true} if interaction logic should be triggered; {@code false} otherwise
     */
    public boolean getShouldTriggerInteraction(){
        return shouldTriggerInteraction;
    }
}
