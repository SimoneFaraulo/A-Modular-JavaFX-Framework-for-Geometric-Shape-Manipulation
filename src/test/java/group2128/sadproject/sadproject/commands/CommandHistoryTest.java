package group2128.sadproject.sadproject.commands;

import javafx.beans.binding.BooleanBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link CommandHistory}.
 * <p>
 * These tests verify correct push, pop, peek, and binding behaviors of the CommandHistory stack.
 * Uses a simple {@link DummyCommand} to test functionality.
 * </p>
 */
public class CommandHistoryTest {

    private CommandHistory history;

    private Command commandA;
    private Command commandB;
    private Command commandC;

    /**
     * Initializes a new CommandHistory and dummy commands before each test.
     */
    @BeforeEach
    public void setUp() {
        history = new CommandHistory();
        commandA = new DummyCommand("A");
        commandB = new DummyCommand("B");
        commandC = new DummyCommand("C");
    }

    /**
     * Verifies that push and pop operations work as expected.
     * Also checks that popping from an empty stack returns null.
     */
    @Test
    public void testPushAndPop() {
        assertTrue(history.isEmpty());

        history.push(commandA);
        history.push(commandB);

        assertFalse(history.isEmpty());

        Command popped = history.pop();
        assertEquals(commandB, popped);

        popped = history.pop();
        assertEquals(commandA, popped);

        assertTrue(history.isEmpty());

        assertNull(history.pop());
    }

    /**
     * Verifies that the emptyBinding reflects the current state of the stack.
     */
    @Test
    public void testEmptyBinding() {
        BooleanBinding binding = history.emptyBinding();

        assertTrue(binding.get());

        history.push(commandC);
        assertFalse(binding.get());

        history.pop();
        assertTrue(binding.get());
    }

    /**
     * Verifies that getLast returns the second-to-last command on the stack.
     */
    @Test
    public void testGetLastCommand() {
        history.push(commandA);
        history.push(commandB);

        Command last = history.getLast();
        assertEquals(commandA, last);
    }

    /**
     * Verifies that calling getLast on an empty stack throws an exception.
     */
    @Test
    public void testGetLastCommandOnEmptyThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> history.getLast());
    }

    /**
     * Simple dummy implementation of {@link Command} for testing purposes.
     * Each instance is identified by a string ID.
     */
    private static class DummyCommand extends Command {
        private final String id;

        /**
         * Constructs a new DummyCommand with the given identifier.
         * @param id identifier for the dummy command
         */
        public DummyCommand(String id) {
            this.id = id;
        }

        /**
         * Empty implementation of execute method.
         */
        @Override
        public void execute() {
            // No-op
        }

        /**
         * Undoes the previously executed fill color change command.
         *
         * <p>If a memento of the drawing canvas is available, this method restores
         * the canvas to its previous state before the fill color was changed.</p>
         */
        @Override
        public void undo() {
            //No op
        }


        /**
         * Returns a string representation of the command.
         */
        @Override
        public String toString() {
            return "DummyCommand{" + "id='" + id + '\'' + '}';
        }

        /**
         * Two DummyCommands are equal if they have the same ID.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DummyCommand that = (DummyCommand) obj;
            return id.equals(that.id);
        }

        /**
         * Hash code based on ID.
         */
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
