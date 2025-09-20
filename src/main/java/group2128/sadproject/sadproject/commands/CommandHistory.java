package group2128.sadproject.sadproject.commands;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A class that manages the history of executed commands using a stack structure.
 * This is an extension of the Command pattern, providing undo support.
 *
 * The internal stack is implemented using an ObservableList to allow UI components
 * to bind to its state (e.g., enable/disable the Undo button based on stack emptiness).
 */
public class CommandHistory {

    /**
     * Internal observable list acting as a stack (LIFO).
     * The head of the list (index 0) represents the top of the stack.
     */
    private final ObservableList<Command> internalStack = FXCollections.observableArrayList();

    /**
     * A JavaFX property wrapper around the internal stack,
     * useful for UI bindings.
     */
    private final SimpleListProperty<Command> stack = new SimpleListProperty<>(internalStack);

    /**
     * Pushes a command onto the top of the stack.
     *
     * @param command the Command to be added
     */
    public void push(Command command) {
        stack.add(0, command);
    }

    /**
     * Pops the most recently added command from the top of the stack.
     * Returns {@code null} if the stack is empty.
     *
     * @return the last Command pushed, or {@code null} if stack is empty
     */
    public Command pop() {
        if (!stack.isEmpty()) {
            return stack.remove(0);
        }
        return null;
    }

    /**
     * Checks whether the stack is currently empty.
     *
     * @return true if there are no commands in the stack, false otherwise
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns a binding that reflects whether the stack is empty.
     * Useful for enabling/disabling UI controls based on stack content.
     *
     * @return a BooleanBinding that is true when the stack is empty
     */
    public BooleanBinding emptyBinding() {
        return Bindings.isEmpty(stack);
    }

    /**
     * Returns the last command added to the command stack without removing it.
     * <p>
     * This method is useful for inspecting the most recent command
     * </p>
     *
     * @return the last {@link Command} in the stack
     * @throws IndexOutOfBoundsException if the stack is empty
     */
    public Command getLast() {
        return stack.get(stack.size() - 1);
    }

}
