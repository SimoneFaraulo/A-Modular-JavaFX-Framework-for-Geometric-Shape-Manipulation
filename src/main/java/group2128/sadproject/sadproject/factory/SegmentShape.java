package group2128.sadproject.sadproject.factory;

import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import org.json.JSONObject;

/**
 * Represents a line segment shape, defined by a start and end point,
 * and extends the {@link Line} class for rendering purposes.
 * Implements the {@link SelectableShape} interface to support setting and retrieving
 * edge color and basic shape properties.
 */
public class SegmentShape extends Line implements SelectableShape {

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
     * The default stroke width (edge thickness) for the segment, in pixels.
     */
    private static final double DEFAULT_STROKE_WIDTH = 3.0;

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
     * The horizontal distance between the shape's starting X coordinate and its end point.
     * This is used to maintain the relative position of the end point during dragging.
     */
    private double distanceX;

    /**
     * The vertical distance between the shape's starting Y coordinate and its end point.
     * This is used to maintain the relative position of the end point during dragging.
     */
    private double distanceY;

    /**
     * A boolean property used to notify whether the block has undergone a generic interaction,
     * such as being drawn or modified. This property can be observed to trigger updates or
     * state changes in response to user actions.
     */
    private final BooleanProperty interactionProperty = new SimpleBooleanProperty();

    /**
     * Constructs a SegmentShape with the specified edge color.
     *
     * @param edgeColor the color to set for the edge of the segment
     */
    public SegmentShape(Color fillColor, Color edgeColor){
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
    }

    /**
     * Constructs a {@code SegmentShape} (a stylized line) with the specified fill and edge colors,
     * as well as the start and end coordinates.
     * <p>
     * This constructor initializes the segment with default stroke width and sets up
     * visual effects for selection and drag behavior.
     * </p>
     *
     * @param fillColor the fill color of the segment (may be unused depending on rendering)
     * @param edgeColor the stroke (edge) color of the segment
     * @param startX    the X coordinate of the segment's starting point
     * @param startY    the Y coordinate of the segment's starting point
     * @param endX      the X coordinate of the segment's ending point
     * @param endY      the Y coordinate of the segment's ending point
     */
    public SegmentShape(Color fillColor, Color edgeColor, double startX, double startY, double endX, double endY){
        super(startX,startY,endX,endY);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);

        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
    }

    /**
     * Constructs a {@code SegmentShape} with full customization, including scale and rotation.
     * <p>
     * This constructor allows setting the segment’s start and end points, visual properties
     * (fill and edge color), scale factors, and rotation angle. It also applies default stroke width,
     * initializes selection effects, and enables drag behavior.
     * </p>
     *
     * @param fillColor the fill color of the segment (may be unused depending on rendering)
     * @param edgeColor the stroke (edge) color of the segment
     * @param startX    the X coordinate of the segment's starting point
     * @param startY    the Y coordinate of the segment's starting point
     * @param endX      the X coordinate of the segment's ending point
     * @param endY      the Y coordinate of the segment's ending point
     * @param scaleX    the horizontal scale factor
     * @param scaleY    the vertical scale factor
     * @param angle     the rotation angle of the segment, in degrees
     */
    public SegmentShape(Color fillColor, Color edgeColor, double startX, double startY, double endX, double endY, double scaleX, double scaleY, double angle){
        super(startX,startY,endX,endY);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        setScaleX(scaleX);
        setScaleY(scaleY);
        setRotate(angle);
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });

        initDrag();
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
     * Initializes mouse event handlers to enable dragging of the shape.
     *
     * <p>The drag behavior is activated only when the shape is currently selected,
     * allowing users to click and drag it using the mouse. The shape's position
     * and its endpoint (e.g., for lines or complex shapes) are both updated to reflect
     * the drag movement. Specifically:
     *
     * <ul>
     *   <li>{@code MousePressed} calculates the initial offsets and distances, and changes the cursor.</li>
     *   <li>{@code MouseDragged} updates both the shape's position and its end point based on the mouse movement.</li>
     *   <li>{@code MouseReleased} resets the cursor to its default state.</li>
     * </ul>
     *
     * Events are consumed to prevent propagation to parent nodes.
     * </p>
     */
    @Override
    public void initDrag() {

        this.setOnMousePressed(event -> {
            if (isSelected()) {
                setDimensionX(event.getX());
                setDimensionY(event.getY());
                dragOffsetX = event.getSceneX() - getAnchorX();
                dragOffsetY = event.getSceneY() - getAnchorY();
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
                setAnchorX(event.getSceneX() - dragOffsetX);
                setAnchorY(event.getSceneY() - dragOffsetY);
                setEndPointX((event.getSceneX() - this.distanceX) - dragOffsetX);
                setEndPointY((event.getSceneY() - this.distanceY) - dragOffsetY);
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
     * Returns a copy of the given {@link SelectableShape} instance.
     * <p>
     * This method creates and returns a new shape object that is a duplicate of the provided shape,
     * including its visual properties and position. The returned shape is independent of the original,
     * allowing modifications without affecting the original instance.
     *
     * @return a new {@code SelectableShape} instance that is a copy of the given shape
     */
    @Override
    public SegmentShape getCopy() {
        Color edge = new Color(getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(), getEdgeColor().getOpacity());
        return new SegmentShape(null, edge, getAnchorX(), getAnchorY(),getEndPointX(), getEndPointY(), getScaleX(), getScaleY(), getRotation());
    }

    /**
     * Sets the color of the segment's edge.
     *
     * @param edgeColor the color to set as the edge
     */
    @Override
    public void setEdgeColor(Color edgeColor) {
        super.setStroke(edgeColor);
    }

    /**
     * Returns the color of the segment's edge.
     *
     * @return the edge color
     */
    @Override
    public Color getEdgeColor() {
        return (Color) getStroke();
    }

    /**
     * Sets the fill color of the shape.
     *
     * @param fillColor the color to be used to fill the shape
     */
    @Override
    public void setFillColor(Color fillColor) {
        super.setFill(fillColor);
    }

    /**
     * Returns the current fill color of the shape.
     *
     * @return the fill color
     */
    @Override
    public Color getFillColor() {
        return (Color) getFill();
    }

    /**
     * Sets the x-coordinate of the starting point.
     *
     * @param startPointX the x-coordinate of the start point
     */
    @Override
    public void setAnchorX(double startPointX) {
        super.setStartX(startPointX);
    }

    /**
     * Returns the x-coordinate of the starting point.
     *
     * @return the start point x-coordinate
     */
    @Override
    public double getAnchorX() {
        return super.getStartX();
    }

    /**
     * Sets the y-coordinate of the starting point.
     *
     * @param startPointY the y-coordinate of the start point
     */
    @Override
    public void setAnchorY(double startPointY) {
        super.setStartY(startPointY);
    }

    /**
     * Returns the y-coordinate of the starting point.
     *
     * @return the start point y-coordinate
     */
    @Override
    public double getAnchorY() {
        return super.getStartY();
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
        this.distanceX = getAnchorX() - getEndPointX();
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
        return this.distanceX;
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
        this.distanceY = getAnchorY() - getEndPointY();
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
        return this.distanceY;
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
        obj.put("type", "segment");
        obj.put("startX", this.getAnchorX());
        obj.put("startY", this.getAnchorY());
        obj.put("endX", this.getEndPointX());
        obj.put("endY", this.getEndPointY());
        obj.put("stroke", Shape.getColorString(this.getEdgeColor()));
        obj.put("strokeWidth", this.getStrokeWidth());
        obj.put("flipHorizontal",this.getScaleX());
        obj.put("flipVertical",this.getScaleY());
        obj.put("rotation", this.getRotation());
    }

    /**
     * Sets the x-coordinate of the ending point.
     *
     * @param endPointX the x-coordinate of the end point
     */
    public void setEndPointX(double endPointX) {
        super.setEndX(endPointX);
    }

    /**
     * Returns the x-coordinate of the ending point.
     *
     * @return the end point x-coordinate
     */
    public double getEndPointX() {
        return super.getEndX();
    }

    /**
     * Sets the y-coordinate of the ending point.
     *
     * @param endPointY the y-coordinate of the end point
     */
    public void setEndPointY(double endPointY) {
        super.setEndY(endPointY);
    }

    /**
     * Returns the y-coordinate of the ending point.
     *
     * @return the end point y-coordinate
     */
    public double getEndPointY() {
        return super.getEndY();
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
        params.setFillColor(null);
        params.setEdgeColor((Color) Paint.valueOf(obj.optString("stroke", "BLACK")));
        params.setRotationValueProperty(obj.getDouble("rotation"));
        params.setScaleX(obj.getDouble("flipHorizontal"));
        params.setScaleY(obj.getDouble("flipVertical"));
    }

}