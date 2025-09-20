package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.json.JSONObject;

/**
 * Represents a generic shape that supports basic geometric and styling operations,
 * including position, size, and color customization.
 * <p>
 * This interface defines the essential methods required to manipulate
 * a drawable shape within a graphical user interface.
 */
public interface Shape {

    /**
     * Sets the color of the shape's edge (stroke).
     *
     * @param edgeColor the color to be applied to the edge
     */
    void setEdgeColor(Color edgeColor);

    /**
     * Returns the current edge (stroke) color of the shape.
     *
     * @return the edge color
     */
    Color getEdgeColor();

    /**
     * Sets the fill color of the shape.
     *
     * @param fillColor the color to be used to fill the shape
     */
    void setFillColor(Color fillColor);

    /**
     * Returns the current fill color of the shape.
     *
     * @return the fill color
     */
    Color getFillColor();

    /**
     * Sets the X coordinate of the shape's anchor point.
     * This point typically represents the reference or origin position of the shape.
     *
     * @param x the X coordinate to set
     */
    void setAnchorX(double x);

    /**
     * Returns the current X coordinate of the shape's anchor point.
     *
     * @return the X coordinate
     */
    double getAnchorX();

    /**
     * Sets the Y coordinate of the shape's anchor point.
     * This point typically represents the reference or origin position of the shape.
     *
     * @param y the Y coordinate to set
     */
    void setAnchorY(double y);

    /**
     * Returns the current Y coordinate of the shape's anchor point.
     *
     * @return the Y coordinate
     */
    double getAnchorY();

    /**
     * Sets the horizontal dimension of the shape.
     * <p>
     * The meaning of this value depends on the specific shape implementation.
     * For example, it may represent the width of a rectangle, the horizontal radius of an ellipse,
     * or the length of a line.
     *
     * @param x the value to set for the horizontal dimension
     */
    void setDimensionX(double x);

    /**
     * Returns the horizontal dimension of the shape.
     * <p>
     * This value may correspond to the width, horizontal radius, or other context-specific
     * horizontal measurement of the shape.
     *
     * @return the horizontal dimension
     */
    double getDimensionX();

    /**
     * Sets the vertical dimension of the shape.
     * <p>
     * The meaning of this value depends on the specific shape implementation.
     * For example, it may represent the height of a rectangle, the vertical radius of an ellipse,
     * or the vertical length of a line.
     *
     * @param y the value to set for the vertical dimension
     */
    void setDimensionY(double y);

    /**
     * Returns the vertical dimension of the shape.
     * <p>
     * This value may correspond to the height, vertical radius, or other context-specific
     * vertical measurement of the shape.
     *
     * @return the vertical dimension
     */
    double getDimensionY();

    /**
     * Sets the rotation angle of the shape.
     *
     * @param angle the angle to set for the rotation
     */
    void setRotation(double angle);

    /**
     * Returns the rotation angle of the shape.
     * @return the rotation angle in degrees.
     */
    double getRotation();

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
    void saveJson(JSONObject obj);

    /**
     * Converts a {@link Paint} object to its string representation.
     * <p>
     * If the paint is {@code null}, returns black ("#000000") as default.
     * </p>
     *
     * @param paint the paint to convert
     * @return a string representation of the paint color
     */
     static String getColorString(Paint paint) {
        if(paint != null) {
            return paint.toString();
        } else {
            return "#000000";
        }
    }
}
