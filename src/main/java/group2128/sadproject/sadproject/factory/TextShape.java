package group2128.sadproject.sadproject.factory;

import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONObject;

/**
 * Represents a selectable and draggable text shape that can be added to a drawing canvas.
 * <p>
 * This class extends {@link Text} and implements the {@link SelectableShape} interface to
 * support operations such as selection highlighting, movement, and duplication.
 * </p>
 */
public class TextShape extends Text implements SelectableShape {

    /**
     * Flag to prevent interaction logic from being triggered multiple times during a single gesture.
     */
    private boolean shouldTriggerInteraction = true;

    /** Default font used when no specific font is set. */
    private static final Font DEFAULT_FONT = Font.font(Font.getDefault().getFamily());

    /** X-axis offset used to calculate drag positioning. */
    private double dragOffsetX;

    /** Y-axis offset used to calculate drag positioning. */
    private double dragOffsetY;

    /** Property that tracks whether this shape is currently selected. */
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty();

    /** Property that indicates whether this shape has been interacted with (e.g., dragged). */
    private final BooleanProperty interactionProperty = new SimpleBooleanProperty();

    /**
     * Constructs a new {@code TextShape} at the specified position with initial styling and transformation.
     *
     * @param x         the initial X coordinate of the shape
     * @param y         the initial Y coordinate of the shape
     * @param scaleX    the initial horizontal scale factor
     * @param scaleY    the initial vertical scale factor
     * @param fontSize  the font size of the text
     * @param fillColor the color used to fill the text
     * @param edgeColor the color used to outline (stroke) the text
     * @param angle    the rotation angle applied to the shape
     */
    public TextShape(double x, double y, double scaleX, double scaleY, double fontSize, Color fillColor, Color edgeColor, double angle) {
        super();

        setAnchorX(x);
        setAnchorY(y);
        setScaleX(scaleX);
        setScaleY(scaleY);
        setFont(new Font(DEFAULT_FONT.getFamily(), fontSize));
        setStroke(edgeColor);
        setFill(fillColor);
        setRotate(angle);

        selectedProperty().addListener((observable, oldValue, newValue) ->
                setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE) : null));

        initDrag();
    }

    /**
     * Constructs a new {@code TextShape} using the specified fill and edge colors.
     *
     * @param fillColor the color used to fill the text
     * @param edgeColor the color used to outline (stroke) the text
     */
    public TextShape(Color fillColor, Color edgeColor) {
        super();
        setFill(fillColor);
        setEdgeColor(edgeColor);

        selectedProperty().addListener((observable, oldValue, newValue) ->
                setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE) : null));

        initDrag();
    }

    /**
     * Returns the observable selection state of the shape.
     *
     * @return the selection property
     */
    @Override
    public BooleanProperty selectedProperty() {
        return this.selectedProperty;
    }

    /**
     * Checks whether the shape is currently selected.
     *
     * @return {@code true} if selected; {@code false} otherwise
     */
    @Override
    public boolean isSelected() {
        return this.selectedProperty.get();
    }

    /**
     * Sets the selection status of the shape.
     *
     * @param selected {@code true} to mark the shape as selected; {@code false} to deselect it
     */
    @Override
    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    /**
     * Checks whether the specified point lies within the bounds of this shape.
     *
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return {@code true} if the point is within the shape; {@code false} otherwise
     */
    @Override
    public boolean contains(double x, double y) {
        return this.getBoundsInParent().contains(x, y);
    }

    /**
     * Initializes the drag behavior for the shape.
     * <p>
     * Enables dragging only when the shape is selected. Mouse events are handled to update
     * the position and interaction state.
     * </p>
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
                if (shouldTriggerInteraction) {
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
     * Returns the interaction state property for this shape.
     *
     * @return the interaction property
     */
    @Override
    public BooleanProperty interactionPropertyProperty() {
        return this.interactionProperty;
    }

    /**
     * Sets the interaction state value.
     *
     * @param interactionProperty {@code true} if interacted with; {@code false} otherwise
     */
    @Override
    public void setInteractionProperty(boolean interactionProperty) {
        this.interactionProperty.set(interactionProperty);
    }

    /**
     * Creates and returns a copy of this {@code TextShape}, duplicating its visual properties.
     *
     * @return a new instance of {@code SelectableShape} that is a copy of this shape
     */
    @Override
    public SelectableShape getCopy() {
        Color edge = new Color(getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(), getEdgeColor().getOpacity());
        Color fill = new Color(getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue(), getFillColor().getOpacity());
        TextShape t = new TextShape(getAnchorX(), getAnchorY(), getDimensionX(), getDimensionY(), getFontSize(), fill, edge, getRotation());
        String freshText = new String(getText());
        t.setText(freshText);
        return t;
    }

    /**
     * Sets the color used to stroke (outline) the text.
     *
     * @param edgeColor the new edge color
     */
    @Override
    public void setEdgeColor(Color edgeColor) {
        super.setStroke(edgeColor);
    }

    /**
     * Returns the current stroke (edge) color of the text.
     *
     * @return the edge color
     */
    @Override
    public Color getEdgeColor() {
        return (Color) getStroke();
    }

    /**
     * Sets the fill color of the text.
     *
     * @param fillColor the new fill color
     */
    @Override
    public void setFillColor(Color fillColor) {
        super.setFill(fillColor);
    }

    /**
     * Returns the current fill color of the text.
     *
     * @return the fill color
     */
    @Override
    public Color getFillColor() {
        return (Color) getFill();
    }

    /**
     * Sets the X coordinate of the text's anchor point.
     *
     * @param x the X coordinate to set
     */
    @Override
    public void setAnchorX(double x) {
        super.setX(x);
    }

    /**
     * Returns the X coordinate of the text's anchor point.
     *
     * @return the X coordinate
     */
    @Override
    public double getAnchorX() {
        return super.getX();
    }

    /**
     * Sets the Y coordinate of the text's anchor point.
     *
     * @param y the Y coordinate to set
     */
    @Override
    public void setAnchorY(double y) {
        super.setY(y);
    }

    /**
     * Returns the Y coordinate of the text's anchor point.
     *
     * @return the Y coordinate
     */
    @Override
    public double getAnchorY() {
        return super.getY();
    }

    /**
     * Sets the horizontal dimension of the shape.
     * <p>
     * For text shapes, this represents the horizontal scale factor.
     * </p>
     *
     * @param x the new horizontal dimension (scale X)
     */
    @Override
    public void setDimensionX(double x) {
        super.setScaleX(x);
    }

    /**
     * Returns the current horizontal dimension of the shape.
     * <p>
     * For text shapes, this is the horizontal scale factor.
     * </p>
     *
     * @return the horizontal scale
     */
    @Override
    public double getDimensionX() {
        return getScaleX();
    }

    /**
     * Sets the vertical dimension of the shape.
     * <p>
     * For text shapes, this is the vertical scale factor.
     * </p>
     *
     * @param y the new vertical dimension (scale Y)
     */
    @Override
    public void setDimensionY(double y) {
        super.setScaleY(y);
    }

    /**
     * Returns the current vertical dimension of the shape.
     * <p>
     * For text shapes, this is the vertical scale factor.
     * </p>
     *
     * @return the vertical scale
     */
    @Override
    public double getDimensionY() {
        return super.getScaleY();
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
        obj.put("type", "text");
        obj.put("x", this.getAnchorX());
        obj.put("y", this.getAnchorY());
        obj.put("fontSize", this.getFontSize());
        obj.put("flipHorizontal", this.getDimensionX());
        obj.put("flipVertical", this.getDimensionY());
        obj.put("fill", Shape.getColorString(this.getFillColor()));
        obj.put("stroke", Shape.getColorString(this.getEdgeColor()));
        obj.put("text", this.getText());
        obj.put("strokeWidth", this.getStrokeWidth());
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
     * Returns the current font size of the text.
     *
     * @return the font size in points
     */
    public double getFontSize() {
        return this.getFont().getSize();
    }

    /**
     * Sets the font size of the text using the default font family.
     * <p>
     * This method preserves the font family defined by {@code DEFAULT_FONT} and updates only the size.
     * It is typically used to resize the text content without altering its typeface.
     * </p>
     *
     * @param fontSize the new font size, in points
     */
    public void setFontSize(double fontSize) {
        this.setFont(new Font(DEFAULT_FONT.getFamily(), fontSize));
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
        params.setFontSize(obj.getDouble("fontSize"));
        params.setScaleX(obj.getDouble("flipHorizontal"));
        params.setScaleY(obj.getDouble("flipVertical"));
        params.setRotationValueProperty(obj.getDouble("rotation"));
        params.setText((String) obj.opt("text"));
    }
}
