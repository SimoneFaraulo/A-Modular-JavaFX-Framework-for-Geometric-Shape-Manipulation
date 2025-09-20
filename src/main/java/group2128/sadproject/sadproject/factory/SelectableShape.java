package group2128.sadproject.sadproject.factory;

import javafx.beans.property.BooleanProperty;

/**
 * Represents a shape that can toggle between <em>selected</em> and
 * <em>unselected</em> states.
 * <p>
 * Implementations are expected to expose a {@link BooleanProperty} so the
 * selection status can be observed and bound to UI elements (e.g.&nbsp;for
 * highlighting or enabling context-sensitive commands).
 * All methods are thread-confined to the JavaFX Application Thread unless
 * otherwise specified.
 */
public interface SelectableShape extends Shape {

    /**
     * Returns the JavaFX {@code BooleanProperty} that stores the current
     * selection flag.
     *
     * @return the observable property representing whether the shape is
     *         currently selected
     */
    public BooleanProperty selectedProperty();

    /**
     * Convenience accessor that mirrors {@code selectedProperty().get()}.
     *
     * @return {@code true} if the shape is selected, {@code false} otherwise
     */
    public boolean isSelected();

    /**
     * Sets the selection flag for this shape.
     *
     * @param selected {@code true} to mark the shape as selected;
     *                 {@code false} to clear the selection
     */
    public void setSelected(boolean selected);

    /**
     * Determines whether the given point (x, y) lies within the bounds of this shape.
     *
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return {@code true} if the point is inside the shape's area;
     *         {@code false} otherwise
     */
    public boolean contains(double x, double y);


    /**
     * Initializes mouse event handlers to enable dragging of the shape.
     *
     * <p>The drag behavior is activated only when the shape is currently selected,
     * allowing users to click and drag it using the mouse. During the drag:
     * <ul>
     *   <li>{@code MousePressed} calculates the initial offset and changes the cursor.</li>
     *   <li>{@code MouseDragged} updates the shape's position based on the mouse movement.</li>
     *   <li>{@code MouseReleased} resets the cursor to default.</li>
     * </ul>
     * Events are consumed to prevent propagation to parent nodes.</p>
     */
    public void initDrag();

    /**
     * Returns the {@link BooleanProperty} object representing the interaction state.
     * This can be used for property bindings or listeners.
     *
     * @return the BooleanProperty for interaction
     */
    public BooleanProperty interactionPropertyProperty();

    /**
     * Sets the value of the interaction property.
     *
     * @param interactionProperty true to enable interaction, false to disable it
     */
    public void setInteractionProperty(boolean interactionProperty);

    /**
     * Returns a copy of the given {@link SelectableShape} instance.
     * <p>
     * This method creates and returns a new shape object that is a duplicate of the provided shape,
     * including its visual properties and position. The returned shape is independent of the original,
     * allowing modifications without affecting the original instance.
     *
     * @return a new {@code SelectableShape} instance that is a copy of the given shape
     */
    public SelectableShape getCopy();
}
