package group2128.sadproject.sadproject.factory;

import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.json.JSONObject;

/**
 * A custom rectangular shape implementation that extends JavaFX's {@link Rectangle}
 * and implements the {@link SelectableShape} interface.
 * This shape supports custom edge and fill colors.
 */
public class RectangleShape extends Rectangle implements SelectableShape {

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
     * The default width of the rectangle shape, in pixels.
     * Used when no specific width is provided during instantiation.
     */
    private static final double DEFAULT_WIDTH = 60.0;

    /**
     * The default height of the rectangle shape, in pixels.
     * Used when no specific height is provided during instantiation.
     */
    private static final double DEFAULT_HEIGHT = 40.0;

    /**
     * The default stroke width (edge thickness) for the rectangle, in pixels.
     */
    private static final double DEFAULT_STROKE_WIDTH = 3.0;

    /**
     * The horizontal offset between the mouse cursor and the shape's X coordinate
     * at the beginning of the drag. Used to maintain consistent dragging behavior.
     */
    private double dragOffsetX;

    /**
     * The vertical offset between the mouse cursor and the shape's Y coordinate
     * at the beginning of the drag. Used to maintain consistent dragging behavior.
     */
    private double dragOffsetY;

    /**
     * A JavaFX {@code BooleanProperty} representing the selection state of this shape.
     * <p>
     * This property can be observed or bound to enable reactive UI behavior,
     * such as highlighting the shape when selected.
     *
     * <p>Defaults to {@code false} (unselected).
     */
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty();

    /**
     * A boolean property used to notify whether the block has undergone a generic interaction,
     * such as being drawn or modified. This property can be observed to trigger updates or
     * state changes in response to user actions.
     */
    private final BooleanProperty interactionProperty = new SimpleBooleanProperty();

    /**
     * Constructs a {@code RectangleShape} with the specified top-left corner coordinates.
     * Default width and height.
     *
     * @param fillColor the fill Color of the shape
     * @param edgeColor the edge Color of the shape
     */
    public RectangleShape(Color fillColor, Color edgeColor) {
        super(DEFAULT_WIDTH,  DEFAULT_HEIGHT);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();

    }

    /**
     * Constructs a {@code RectangleShape} with the specified fill and edge colors,
     * and the given width and height. The rectangle is centered at (0,0) by default.
     *
     * @param fillColor the fill color of the rectangle
     * @param edgeColor the edge (stroke) color of the rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     */
    public RectangleShape(Color fillColor, Color edgeColor, double width, double height) {
        super(width,  height);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
    }

    /**
     * Constructs a {@code RectangleShape} with the specified fill and edge colors,
     * top-left corner coordinates (x, y), width, and height.
     *
     * @param fillColor the fill color of the rectangle
     * @param edgeColor the edge (stroke) color of the rectangle
     * @param x         the X coordinate of the top-left corner
     * @param y         the Y coordinate of the top-left corner
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     */
    public RectangleShape(Color fillColor, Color edgeColor, double x, double y, double width, double height){
        super(width,height);
        setAnchorX(x);
        setAnchorY(y);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
    }


    /**
     * Constructs a {@code RectangleShape} with the specified fill and edge colors,
     * top-left corner (x, y), dimensions, scale factors, and rotation angle.
     *
     * @param fillColor the fill color of the rectangle
     * @param edgeColor the edge (stroke) color of the rectangle
     * @param x         the X coordinate of the top-left corner
     * @param y         the Y coordinate of the top-left corner
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @param scaleX    the horizontal scale factor
     * @param scaleY    the vertical scale factor
     * @param angle     the rotation angle in degrees
     */
    public RectangleShape(Color fillColor, Color edgeColor, double x, double y, double width, double height, double scaleX, double scaleY, double angle){
        super(width,height);
        setAnchorX(x);
        setAnchorY(y);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        setScaleX(scaleX);
        setScaleY(scaleY);
        setRotation(angle);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
    }


    /**
     * Returns a copy of the given {@link SelectableShape} instance.
     * <p>
     * This method creates and returns a new shape object that is a duplicate of the provided shape,
     * including its visual properties and position. The returned shape is independent of the original,
     * allowing modifications without affecting the original instance.
     *
     * @return a new {@code SelectableShape} instance that is a copy of the given shape
     */
    @Override
    public SelectableShape getCopy() {
        Color edge = new Color(getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(), getEdgeColor().getOpacity());
        Color fill = new Color(getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue(), getFillColor().getOpacity());
        RectangleShape rectangle = new RectangleShape(fill, edge,getAnchorX(),getAnchorY(),getDimensionX(),getDimensionY());
        rectangle.setScaleX(getScaleX());
        rectangle.setScaleY(getScaleY());
        rectangle.setRotation(getRotation());
        return rectangle;
    }

    /**
     * Returns the {@link BooleanProperty} object representing the interaction state.
     * This can be used for property bindings or listeners.
     *
     * @return the BooleanProperty for interaction
     */
    @Override
    public BooleanProperty interactionPropertyProperty() {
        return interactionProperty;
    }

    /**
     * Sets the value of the interaction property.
     *
     * @param interactionProperty true to enable interaction, false to disable it
     */
    @Override
    public void setInteractionProperty(boolean interactionProperty) {
        this.interactionProperty.set(interactionProperty);
    }

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
    @Override
    public void initDrag() {
        this.setOnMousePressed(event -> {
            if (isSelected()) {
                dragOffsetX = event.getSceneX() - getX();
                dragOffsetY = event.getSceneY() - getY();

                event.consume();
            }
        });

        this.setOnMouseDragged(event -> {
            if (isSelected() && event.getButton() == MouseButton.PRIMARY) {
                if(shouldTriggerInteraction){
                    interactionProperty.set(true);
                    shouldTriggerInteraction = false;
                }
                setCursor(Cursor.CLOSED_HAND);
                setX(event.getSceneX() - dragOffsetX);
                setY(event.getSceneY() - dragOffsetY);
                event.consume();
            }
        });

        this.setOnMouseReleased(event -> {
            if (isSelected()) {
                setCursor(Cursor.DEFAULT);
                shouldTriggerInteraction = true;
            }
        });
    }

    /**
     * Sets the edge color (stroke) of the rectangle.
     *
     * @param edge the {@link Color} to use for the edge
     */
    @Override
    public void setEdgeColor(Color edge) {
        super.setStroke(edge);
    }

    /**
     * Returns the current edge color (stroke) of the rectangle.
     *
     * @return the {@link Color} used for the edge
     */
    @Override
    public Color getEdgeColor() {
        return (Color) getStroke();
    }

    /**
     * Sets the fill color of the rectangle.
     *
     * @param fillColor the {@link Color} to fill the rectangle with
     */
    @Override
    public void setFillColor(Color fillColor) {
        super.setFill(fillColor);
    }

    /**
     * Returns the current fill color of the rectangle.
     *
     * @return the {@link Color} used to fill the rectangle
     */
    @Override
    public Color getFillColor() {
        return (Color) getFill();
    }

    /**
     * Sets the X coordinate of the rectangle's center point.
     *
     * @param x the new X coordinate of the center
     */
    @Override
    public void setAnchorX(double x) {
        super.setX(x - getDimensionX() / 2);
    }

    /**
     * Sets the Y coordinate of the rectangle's center point.
     *
     * @param y the new Y coordinate of the center
     */
    @Override
    public void setAnchorY(double y) {
        super.setY(y - getDimensionY() / 2);
    }

    /**
     * Returns the X coordinate of the rectangle's center point.
     *
     * @return the X coordinate of the center
     */
    @Override
    public double getAnchorX() {
        return getX()+ getDimensionX() / 2;
    }

    /**
     * Returns the Y coordinate of the rectangle's center point.
     *
     * @return the Y coordinate of the center
     */
    @Override
    public double getAnchorY() {
        return getY()+ getDimensionY() / 2;
    }

    /**
     * Sets the horizontal dimension of the shape.
     * <p>
     * The meaning of this value depends on the specific shape implementation.
     * For example, it may represent the width of a rectangle, the horizontal radius of an ellipse,
     * or the length of a line.
     *
     * @param x the value to set for the horizontal dimension
     */
    @Override
    public void setDimensionX(double x) {
        super.setWidth(x);
    }

    /**
     * Returns the horizontal dimension of the shape.
     * <p>
     * This value may correspond to the width, horizontal radius, or other context-specific
     * horizontal measurement of the shape.
     *
     * @return the horizontal dimension
     */
    @Override
    public double getDimensionX() {
        return super.getWidth();
    }

    /**
     * Sets the vertical dimension of the shape.
     * <p>
     * The meaning of this value depends on the specific shape implementation.
     * For example, it may represent the height of a rectangle, the vertical radius of an ellipse,
     * or the vertical length of a line.
     *
     * @param y the value to set for the vertical dimension
     */
    @Override
    public void setDimensionY(double y) {
        super.setHeight(y);
    }

    /**
     * Returns the vertical dimension of the shape.
     * <p>
     * This value may correspond to the height, vertical radius, or other context-specific
     * vertical measurement of the shape.
     *
     * @return the vertical dimension
     */
    @Override
    public double getDimensionY() {
        return super.getHeight();
    }

    /**
     * Sets the rotation angle of the shape.
     *
     * @param angle the angle to set for the rotation
     */
    @Override
    public void setRotation(double angle) {
        super.setRotate(angle);
    }

    /**
     * Returns the rotation angle of the shape.
     *
     * @return the rotation angle in degrees.
     */
    @Override
    public double getRotation() {
        return super.getRotate();
    }


    /**
     * Returns the JavaFX {@code BooleanProperty} that stores the current
     * selection flag.
     *
     * @return the observable property representing whether the shape is
     * currently selected
     */
    @Override
    public BooleanProperty selectedProperty() {
        return this.selectedProperty;
    }

    /**
     * Convenience accessor that mirrors {@code selectedProperty().get()}.
     *
     * @return {@code true} if the shape is selected, {@code false} otherwise
     */
    @Override
    public boolean isSelected() {
        return this.selectedProperty.get();
    }

    /**
     * Sets the selection flag for this shape.
     *
     * @param selected {@code true} to mark the shape as selected;
     *                 {@code false} to clear the selection
     */
    @Override
    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    /**
     * Determines whether the given point (x, y) lies within the bounds of this shape.
     *
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return {@code true} if the point is inside the shape's area;
     *         {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y) {
        return this.getBoundsInParent().contains(x,y);
    }

    /**
     * Serializes the properties of this shape into the provided {@link JSONObject}.
     * <p>
     * This method is typically used to persist the state of the shape, including its position,
     * dimensions, colors, rotation, and any other relevant attributes, into a JSON format
     * suitable for saving to disk or transmitting over a network.
     * </p>
     *
     * @param obj the {@code JSONObject} to populate with this shape's data
     */
    @Override
    public void saveJson(JSONObject obj) {
        obj.put("type", "rectangle");
        obj.put("x", this.getAnchorX());
        obj.put("y", this.getAnchorY());
        obj.put("width", this.getDimensionX());
        obj.put("height", this.getDimensionY());
        obj.put("fill", Shape.getColorString(this.getFillColor()));
        obj.put("stroke", Shape.getColorString(this.getEdgeColor()));
        obj.put("strokeWidth", this.getStrokeWidth());
        obj.put("flipHorizontal",this.getScaleX());
        obj.put("flipVertical",this.getScaleY());
        obj.put("rotation", this.getRotation());
    }

    /**
     * Populates the given {@link DrawingParams} object with shape attributes extracted from
     * the specified {@link JSONObject}.
     * <p>
     * This method is typically used to deserialize a shape's visual properties—such as fill color,
     * stroke color, width, and height—from a JSON object that was previously created via serialization.
     * It updates the provided {@code DrawingParams} instance in-place with the extracted values.
     * </p>
     * <p>
     * If any expected property is missing from the JSON, a default fallback is applied
     * (e.g., {@code "BLACK"} for color values).
     * </p>
     *
     * @param params the {@code DrawingParams} instance to populate
     * @param obj the {@code JSONObject} containing the serialized shape data
     */
    public static void loadJson(DrawingParams params, JSONObject obj) {
        params.setFillColor((Color) Paint.valueOf(obj.optString("fill", "BLACK")));
        params.setEdgeColor((Color) Paint.valueOf(obj.optString("stroke", "BLACK")));
        params.setWidthValueProperty(obj.getDouble("width"));
        params.setHeightValueProperty(obj.getDouble("height"));
        params.setRotationValueProperty(obj.getDouble("rotation"));
        params.setScaleX(obj.getDouble("flipHorizontal"));
        params.setScaleY(obj.getDouble("flipVertical"));
    }
}
