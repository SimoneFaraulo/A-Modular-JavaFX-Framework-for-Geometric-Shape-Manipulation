package group2128.sadproject.sadproject.factory;

import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import org.json.JSONObject;
import java.util.List;


/**
 * A custom polygon shape that extends {@link Polygon} and implements the {@link SelectableShape} interface.
 * <p>
 * This class allows defining a polygonal shape with support for selection, interaction, and anchor points.
 * It integrates JavaFX properties for selection and interaction state management and provides methods to
 * manipulate the shape’s geometry and visual appearance.
 * </p>
 *
 * <p>Features include:
 * <ul>
 *     <li>Adding and setting points using {@link Point2D}.</li>
 *     <li>Support for selection and interaction properties.</li>
 *     <li>Customizable fill and stroke (edge) colors.</li>
 *     <li>Anchor point tracking for X and Y coordinates.</li>
 * </ul>
 * </p>
 */
public class PolygonShape extends Polygon implements SelectableShape{


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
     * A JavaFX {@link BooleanProperty} representing whether the shape is currently selected.
     * This property can be observed or bound to enable selection-based behaviors in the UI.
     */
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty();

    /**
     * A JavaFX {@link BooleanProperty} representing whether the shape is currently interactive.
     * This property can be used to control or respond to user interaction states.
     */
    private final BooleanProperty interactionProperty = new SimpleBooleanProperty();

    /**
     * The default stroke width (edge thickness) for the polygon, in pixels.
     */
    private static final double DEFAULT_STROKE_WIDTH = 3.0;

    private double dragStartX;

    private double dragStartY;


    /**
     * Constructs a new {@code PolygonShape} instance with the specified fill and edge colors,
     * and initial anchor point coordinates.
     *
     * @param fillColor  the fill color of the shape
     * @param edgeColor the edge color of the shape
     */
    public PolygonShape(Color fillColor,Color edgeColor){
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        initDrag();

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });
    }

    /**
     * Constructs a new {@code PolygonShape} instance with the specified fill and edge colors,
     * and initial anchor point coordinates.
     *
     * @param fillColor  the fill color of the shape
     * @param edgeColor the edge color of the shape
     * @param points a list of points to define the polygon's vertices'
     */
    public PolygonShape(Color fillColor, Color edgeColor, List<Double> points){
        this(fillColor,edgeColor);
        setPoints(points);
        initDrag();
    }

    public PolygonShape(Color fillColor, Color edgeColor, double scaleX, double scaleY, double angle){
        super.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        setFillColor(fillColor);
        setEdgeColor(edgeColor);
        setScaleX(scaleX);
        setScaleY(scaleY);
        setRotation(angle);
        initDrag();

        selectedProperty().addListener((observable, oldValue, newValue) -> {
            setEffect(newValue ? new DropShadow(10, Color.DEEPSKYBLUE): null);
        });
    }


    /**
     * Sets the polygon's points using a list of {@link Double} objects.
     * The existing points are cleared before the new ones are added.
     *
     * @param points a list of points to define the polygon's vertices
     */
    public void setPoints(List<Double> points){
        super.getPoints().clear();
        for(Double point : points){
            addPoint(point);
        }
    }

    /**
     * Adds a single {@link Double} to the polygon's list of vertices.
     *
     * @param point the point to add as a new vertex
     */
    public void addPoint(Double point){
        super.getPoints().add(point);
    }

    /**
     * Returns the current list of points defining the polygon's vertices.
     *
     * @return an observable list of {@code Double} values representing the X and Y coordinates
     */
    public ObservableList<Double> getPointsList(){
        return super.getPoints();
    }

    /**
     * Returns the JavaFX {@code BooleanProperty} that indicates whether this shape is selected.
     * <p>
     * This property can be used for binding or observing changes in the shape's selection state.
     *
     * @return the {@code BooleanProperty} representing the selection status of this shape
     */
    @Override
    public BooleanProperty selectedProperty() {
        return this.selectedProperty;
    }

    /**
     * Checks whether the shape is currently selected.
     * <p>
     * This is a convenience method that returns the current value of {@code selectedProperty()}.
     *
     * @return {@code true} if the shape is selected, {@code false} otherwise
     */
    @Override
    public boolean isSelected() {
        return this.selectedProperty.get();
    }

    /**
     * Updates the selection state of this shape.
     * <p>
     * Setting this flag to {@code true} marks the shape as selected, which can be reflected
     * visually or used to enable additional interaction. Setting it to {@code false}
     * clears the selection.
     *
     * @param selected {@code true} to select the shape; {@code false} to deselect it
     */
    @Override
    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    /**
     * Initializes mouse event handlers to support dragging behavior for this shape.
     * <p>
     * The drag functionality is only active when the shape is selected. During a drag operation:
     * <ul>
     *   <li><b>MousePressed</b>: Stores the offset from the cursor to the shape and changes the cursor appearance.</li>
     *   <li><b>MouseDragged</b>: Repositions the shape based on the current mouse location and initial offset.</li>
     *   <li><b>MouseReleased</b>: Resets the cursor to the default and completes the drag operation.</li>
     * </ul>
     * Mouse events are consumed to prevent propagation to parent nodes.
     * <p>
     * <b>Note:</b> This method must be implemented to enable actual dragging behavior.
     */
    @Override
    public void initDrag() {

        this.setOnMousePressed(event -> {
            if (isSelected()) {
                dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();

                event.consume();
            }
        });

        this.setOnMouseDragged(event -> {
            if (isSelected() && event.getButton() == MouseButton.PRIMARY) {
                if (shouldTriggerInteraction) {
                    interactionProperty.set(true);
                    shouldTriggerInteraction = false;
                }

                double deltaX = event.getSceneX() - dragStartX;
                double deltaY = event.getSceneY() - dragStartY;

                ObservableList<Double> points = getPoints();
                for (int i = 0; i < points.size(); i += 2) {
                    points.set(i, points.get(i) + deltaX);
                    points.set(i + 1, points.get(i + 1) + deltaY);
                }

                dragStartX = event.getSceneX();
                dragStartY = event.getSceneY();

                setCursor(Cursor.CLOSED_HAND);
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
     * Returns the {@link BooleanProperty} representing the interaction state of this shape.
     * <p>
     * This property can be used for binding or listening to changes in the interaction state,
     * such as enabling or disabling shape-related events or behaviors.
     *
     * @return the {@code BooleanProperty} tracking interaction status
     */
    @Override
    public BooleanProperty interactionPropertyProperty() {
        return this.interactionProperty;
    }

    /**
     * Sets the interaction state of this shape.
     * <p>
     * This flag can be used to enable or disable interactive behavior such as selection,
     * dragging, or responding to mouse events.
     *
     * @param interactionProperty {@code true} to enable interaction; {@code false} to disable it
     */
    @Override
    public void setInteractionProperty(boolean interactionProperty) {
        this.interactionProperty.set(interactionProperty);
    }

    /**
     * Creates and returns a copy of the specified {@link SelectableShape} instance.
     * <p>
     * The new shape should replicate all relevant visual and logical properties
     * (such as color, position, and dimensions) but remain independent of the original.
     * This allows modifications to the copy without affecting the original shape.
     * <p>
     * <b>Note:</b> This method is not implemented and currently returns {@code null}.
     *
     * @return a new {@code SelectableShape} instance that is a copy of the provided shape,
     *         or {@code null} if not implemented
     */
    @Override
    public SelectableShape getCopy() {
        Color edge = new Color(getEdgeColor().getRed(), getEdgeColor().getGreen(), getEdgeColor().getBlue(), getEdgeColor().getOpacity());
        Color fill = new Color(getFillColor().getRed(), getFillColor().getGreen(), getFillColor().getBlue(), getFillColor().getOpacity());
        PolygonShape polygon = new PolygonShape(fill,edge,getPointsList());
        polygon.setScaleX(getScaleX());
        polygon.setScaleY(getScaleY());
        polygon.setRotation(getRotation());
        return polygon;
    }

    /**
     * Sets the edge color (stroke) of this shape.
     * <p>
     * This color defines the outline or border of the shape.
     *
     * @param edgeColor the {@code Color} to apply to the shape's edge
     */
    @Override
    public void setEdgeColor(Color edgeColor) {
        super.setStroke(edgeColor);
    }

    /**
     * Returns the current edge (stroke) color of this shape.
     *
     * @return the {@code Color} used for the shape's outline
     */
    @Override
    public Color getEdgeColor() {
        return (Color) getStroke();
    }

    /**
     * Sets the fill color of the shape.
     * <p>
     * This color is used to fill the interior area of the shape.
     *
     * @param fillColor the {@code Color} to apply as the fill
     */
    @Override
    public void setFillColor(Color fillColor) {
        super.setFill(fillColor);
    }

    /**
     * Returns the current fill color of the shape.
     *
     * @return the {@code Color} used to fill the shape
     */
    @Override
    public Color getFillColor() {
        return (Color) getFill();
    }

    /**
     * Sets the X coordinate of the shape's anchor point.
     * <p>
     * The anchor point typically defines the shape's initial or reference position on the X-axis.
     * This implementation also inserts the X coordinate into the point list at index 0.
     *
     * @param x the X coordinate to assign to the anchor point
     */
    @Override
    public void setAnchorX(double x) {
        super.getPoints().add(0,x);
    }

    /**
     * Returns the current X coordinate of the shape's anchor point.
     *
     * @return the X coordinate of the anchor point
     */
    @Override
    public double getAnchorX() {
        return super.getPoints().get(0);
    }

    /**
     * Sets the Y coordinate of the shape's anchor point.
     * <p>
     * The anchor point typically defines the shape's initial or reference position on the Y-axis.
     * This implementation also inserts the Y coordinate into the point list at index 1.
     *
     * @param y the Y coordinate to assign to the anchor point
     */
    @Override
    public void setAnchorY(double y) {
        super.getPoints().add(1,y);
    }

    /**
     * Returns the current Y coordinate of the shape's anchor point.
     *
     * @return the Y coordinate of the anchor point
     */
    @Override
    public double getAnchorY() {
        return super.getPoints().get(1);
    }



    /**
     * Returns the horizontal dimension of the shape.
     * <p>
     * This could represent the width, horizontal radius, or other shape-specific horizontal measurement.
     *
     * @return the current horizontal dimension value
     */
    @Override
    public double getDimensionX() {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        ObservableList<Double> points = getPoints();
        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
        }
        return maxX - minX;
    }


    /**
     * Sets the horizontal dimension of the shape.
     * <p>
     * The specific meaning of this value depends on the shape implementation.
     * For instance, it could be the width of a rectangle, the horizontal radius of an ellipse,
     * or the length of a horizontal line segment.
     *
     * @param x the value to set for the horizontal dimension
     */
    @Override
    public void setDimensionX(double x) {

    }

    /**
     * Sets the vertical dimension of the shape.
     * <p>
     * The exact meaning of this value varies by shape.
     * Examples include the height of a rectangle, the vertical radius of an ellipse,
     * or the vertical length of a line segment.
     *
     * @param y the value to set for the vertical dimension
     */
    @Override
    public void setDimensionY(double y) {

    }

    /**
     * Returns the vertical dimension of the shape.
     * <p>
     * This may represent the height, vertical radius, or other shape-specific vertical measurement.
     *
     * @return the current vertical dimension value
     */
    @Override
    public double getDimensionY() {
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        ObservableList<Double> points = getPoints();
        for (int i = 1; i < points.size(); i += 2) {
            double y = points.get(i);
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
        return maxY - minY;
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
        obj.put("type", "polygon");
        obj.put("points", this.getPointsList());
        obj.put("fill", Shape.getColorString(this.getFillColor()));
        obj.put("stroke",Shape.getColorString(this.getEdgeColor()));
        obj.put("strokeWidth",this.getStrokeWidth());
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
        params.setFillColor((Color) Paint.valueOf(obj.optString("fill","BLACK")));
        params.setEdgeColor((Color) Paint.valueOf(obj.optString("stroke","BLACK")));
        params.setRotationValueProperty(obj.getDouble("rotation"));
        params.setScaleX(obj.getDouble("flipHorizontal"));
        params.setScaleY(obj.getDouble("flipVertical"));
    }
}
