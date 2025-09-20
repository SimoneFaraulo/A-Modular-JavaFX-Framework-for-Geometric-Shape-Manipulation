package group2128.sadproject.sadproject.factory;

import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import org.json.JSONObject;

/**
 * A custom implementation of an ellipse shape that extends {@link Ellipse} and implements
 * the {@link SelectableShape} interface.
 *
 * <p>This class enhances the standard JavaFX ellipse by adding support for selection state,
 * interaction tracking, dragging behavior, and standardized color and dimension management.
 * It is intended for use in interactive graphical applications that rely on a shape factory pattern.</p>
 */
public class EllipseShape extends Ellipse implements SelectableShape {

    /**
     * The default horizontal radius of the ellipse.
     */
    private static final double DEFAULT_RADIUS_X = 50.0;

    /**
     * The default vertical radius of the ellipse.
     */
    private static final double DEFAULT_RADIUS_Y = 60.0;

    /**
     * The default stroke width used for the ellipse's border.
     */
    private static final double DEFAULT_STROKE_WIDTH = 3.0;

    /**
     * Property indicating whether the ellipse is currently selected.
     * This property can be observed to update the visual representation accordingly.
     */
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty();

    /**
     * Property indicating whether the ellipse is currently in an interactive state
     * (e.g., being dragged or resized).
     */
    private final BooleanProperty interactionProperty = new SimpleBooleanProperty();

    /**
     * The horizontal offset between the mouse click position and the ellipse's center
     * when initiating a drag gesture.
     */
    private double dragOffsetX;

    /**
     * The vertical offset between the mouse click position and the ellipse's center
     * when initiating a drag gesture.
     */
    private double dragOffsetY;

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
     * Constructs an ellipse shape with default dimensions and specified fill and edge colors.
     *
     * @param fillColor the fill color of the ellipse
     * @param edgeColor the edge (stroke) color of the ellipse
     */
    public EllipseShape(Color fillColor, Color edgeColor) {
        super(DEFAULT_RADIUS_X, DEFAULT_RADIUS_Y);
        initialize(fillColor, edgeColor);
    }

    /**
     * Constructs an ellipse shape with specified radii and colors.
     *
     * @param fillColor the fill color of the ellipse
     * @param edgeColor the edge (stroke) color of the ellipse
     * @param radiusX   the horizontal radius of the ellipse
     * @param radiusY   the vertical radius of the ellipse
     */
    public EllipseShape(Color fillColor, Color edgeColor, double radiusX, double radiusY) {
        super(radiusX, radiusY);
        initialize(fillColor, edgeColor);
    }

    /**
     * Constructs a fully customized {@code EllipseShape} with the specified visual and geometric properties.
     * <p>
     * This constructor allows full configuration of the ellipse, including its center position, radii,
     * scale factors, rotation angle, fill color, and stroke color. The shape is initialized and made
     * ready for rendering and interaction within the application.
     * </p>
     *
     * @param fillColor the fill color of the ellipse
     * @param edgeColor the stroke (edge) color of the ellipse
     * @param centerX   the X coordinate of the ellipse's center
     * @param centerY   the Y coordinate of the ellipse's center
     * @param radiusX   the horizontal radius of the ellipse
     * @param radiusY   the vertical radius of the ellipse
     * @param scaleX    the horizontal scaling factor applied to the ellipse
     * @param scaleY    the vertical scaling factor applied to the ellipse
     * @param angle     the rotation angle of the ellipse, in degrees
     */
    public EllipseShape(Color fillColor, Color edgeColor, double centerX, double centerY, double radiusX, double radiusY, double scaleX , double scaleY, double angle) {
        super(centerX, centerY, radiusX, radiusY);
        super.setScaleX(scaleX);
        super.setScaleY(scaleY);
        setRotation(angle);
        initialize(fillColor, edgeColor);
    }

    /**
     * Constructs a fully customized {@code EllipseShape} with the specified visual and geometric properties.
     * <p>
     * This constructor allows full configuration of the ellipse, including its center position, radii,
     * scale factors, rotation angle, fill color, and stroke color. The shape is initialized and made
     * ready for rendering and interaction within the application.
     * </p>
     *
     * @param fillColor the fill color of the ellipse
     * @param edgeColor the stroke (edge) color of the ellipse
     * @param centerX   the X coordinate of the ellipse's center
     * @param centerY   the Y coordinate of the ellipse's center
     * @param radiusX   the horizontal radius of the ellipse
     * @param radiusY   the vertical radius of the ellipse
     */
    public EllipseShape(Color fillColor, Color edgeColor, double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY, radiusX, radiusY);
        initialize(fillColor, edgeColor);
    }


    /**
     * Initializes common properties such as stroke width, colors, selection visual effects, and drag behavior.
     *
     * @param fillColor the fill color
     * @param edgeColor the stroke color
     */
    private void initialize(Color fillColor, Color edgeColor) {
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        setStrokeWidth(DEFAULT_STROKE_WIDTH);
        selectedProperty.addListener((obs, oldVal, newVal) ->
                setEffect(newVal ? new DropShadow(10, Color.DEEPSKYBLUE) : null));
        initDrag();
    }

    /**
     * Initializes mouse event handlers to enable selection-based dragging of the shape.
     */
    @Override
    public void initDrag() {
        setOnMousePressed(event -> {
            if (isSelected()) {
                dragOffsetX = event.getSceneX() - getAnchorX();
                dragOffsetY = event.getSceneY() - getAnchorY();
                event.consume();
            }
        });

        setOnMouseDragged(event -> {
            if (isSelected() && event.getButton() == MouseButton.PRIMARY) {
                if (shouldTriggerInteraction) {
                    interactionProperty.set(true);
                    shouldTriggerInteraction = false;
                }
                setCursor(Cursor.CLOSED_HAND);
                setAnchorX(event.getSceneX() - dragOffsetX);
                setAnchorY(event.getSceneY() - dragOffsetY);
                event.consume();
            }
        });

        setOnMouseReleased(event -> {
            if (isSelected()) {
                setCursor(Cursor.DEFAULT);
                shouldTriggerInteraction = true;
            }
        });
    }

    /**
     * Returns a new instance of {@code EllipseShape} that duplicates this shape's visual properties and position.
     *
     * @return a new {@code EllipseShape} instance
     */
    @Override
    public SelectableShape getCopy() {
        Color edge = new Color(getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(), getEdgeColor().getOpacity());
        Color fill = new Color(getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue(), getFillColor().getOpacity());
        return new EllipseShape(fill, edge, getAnchorX(), getAnchorY(), getDimensionX(), getDimensionY(), getScaleX(), getScaleY(), getRotation());
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
     * Sets the edge (stroke) color of the ellipse.
     *
     * @param edgeColor the new stroke color
     */
    @Override
    public void setEdgeColor(Color edgeColor) {
        super.setStroke(edgeColor);
    }

    /**
     * Returns the current edge (stroke) color of the ellipse.
     *
     * @return the edge color
     */
    @Override
    public Color getEdgeColor() {
        return (Color) getStroke();
    }

    /**
     * Sets the X coordinate of the ellipse's center.
     *
     * @param x the new X coordinate
     */
    @Override
    public void setAnchorX(double x) {
        super.setCenterX(x);
    }

    /**
     * Returns the current X coordinate of the ellipse's center.
     *
     * @return the X coordinate
     */
    @Override
    public double getAnchorX() {
        return super.getCenterX();
    }

    /**
     * Sets the Y coordinate of the ellipse's center.
     *
     * @param y the new Y coordinate
     */
    @Override
    public void setAnchorY(double y) {
        super.setCenterY(y);
    }

    /**
     * Returns the current Y coordinate of the ellipse's center.
     *
     * @return the Y coordinate
     */
    @Override
    public double getAnchorY() {
        return super.getCenterY();
    }

    /**
     * Sets the horizontal dimension (width, radius, or extent depending on shape type) of the shape.
     * The implementation should define what this value means contextually.
     */
    @Override
    public void setDimensionX(double x) {
        super.setRadiusX(x);
    }

    /**
     * Returns the current horizontal dimension (width, radius, or extent) of the shape.
     *
     * @return the X dimension
     */
    @Override
    public double getDimensionX() {
        return super.getRadiusX();
    }

    /**
     * Sets the vertical dimension (height, radius, or extent depending on shape type) of the shape.
     * The implementation should define what this value means contextually.
     */
    @Override
    public void setDimensionY(double y) {
        super.setRadiusY(y);
    }

    /**
     * Returns the current vertical dimension (height, radius, or extent) of the shape.
     *
     * @return the Y dimension
     */
    @Override
    public double getDimensionY() {
        return super.getRadiusY();
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
        obj.put("type", "ellipse");
        obj.put("x", this.getAnchorX());
        obj.put("y", this.getAnchorY());
        obj.put("radiusX", this.getDimensionX());
        obj.put("radiusY", this.getDimensionY());
        obj.put("fill", Shape.getColorString(this.getFillColor()));
        obj.put("stroke", Shape.getColorString(this.getEdgeColor()));
        obj.put("strokeWidth", this.getStrokeWidth());
        obj.put("flipHorizontal",this.getScaleX());
        obj.put("flipVertical",this.getScaleY());
        obj.put("rotation", this.getRotation());
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
     * Sets the fill color of the ellipse.
     *
     * @param fillColor the {@link Color} to fill the ellipse
     */
    @Override
    public void setFillColor(Color fillColor) {
        super.setFill(fillColor);
    }

    /**
     * Returns the current fill color of the ellipse.
     *
     * @return the {@link Color} used to fill the ellipse
     */
    @Override
    public Color getFillColor() {
        return (Color) getFill();
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
        return this.selectedProperty().get();
    }

    /**
     * Sets the selection flag for this shape.
     *
     * @param selected {@code true} to mark the shape as selected;
     *                 {@code false} to clear the selection
     */
    @Override
    public void setSelected(boolean selected) {
        this.selectedProperty().set(selected);
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
        params.setFillColor((Color) Paint.valueOf(obj.optString("fill", "BLACK")));
        params.setEdgeColor((Color) Paint.valueOf(obj.optString("stroke", "BLACK")));
        params.setWidthValueProperty(obj.getDouble("radiusX"));
        params.setHeightValueProperty(obj.getDouble("radiusY"));
        params.setRotationValueProperty(obj.getDouble("rotation"));
        params.setScaleX(obj.getDouble("flipHorizontal"));
        params.setScaleY(obj.getDouble("flipVertical"));
    }

}
