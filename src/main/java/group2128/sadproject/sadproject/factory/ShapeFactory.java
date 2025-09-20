package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

/**
 * Abstract factory class for creating {@link Shape} objects.
 * <p>
 * This class defines a standard interface for concrete shape factories
 * that create various types of shapes (e.g., rectangles, ellipses, segments) with specified parameters.
 * </p>
 * <p>
 * Subclasses must implement at least one of the protected methods:
 * {@link #createShapeWithParams(Color, Color, double, double, double, double)},
 * depending on the parameters required by the specific shape type.
 * </p>
 */
public abstract class ShapeFactory {

    /**
     * The shape instance managed or produced by the factory.
     * <p>
     * This field stores the last created shape and can be used by the factory or external
     * consumers to access or modify the most recently generated shape.
     * </p>
     */
    private Shape shape;

    /**
     * Returns the last shape instance created by this factory.
     *
     * @return the most recently created {@link Shape}, or {@code null} if no shape has been created
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Stores a reference to the shape instance created by this factory.
     *
     * @param shape the {@link Shape} to set as the current shape
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    /**
     * Creates a new {@link Shape} instance using all required parameters,
     * including color and geometric properties.
     * <p>
     * This method is intended to be used for shapes that require full specification of
     * position and size at creation time.
     * </p>
     *
     * @param colorFill  the fill (interior) color of the shape
     * @param colorEdge  the edge (stroke) color of the shape
     * @param anchorX    the X coordinate of the anchor point (e.g., center or top-left)
     * @param anchorY    the Y coordinate of the anchor point
     * @param dimensionX the horizontal dimension (e.g., width, radiusX)
     * @param dimensionY the vertical dimension (e.g., height, radiusY)
     * @return a new {@code Shape} instance created with the given parameters
     */
    public Shape createShape(Color colorFill, Color colorEdge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
        Shape shape = createShapeWithParams(colorFill, colorEdge, anchorX, anchorY, dimensionX, dimensionY);
        setShape(shape);
        return shape;
    }

    /**
     * Creates a new {@link Shape} instance using only fill and edge colors.
     * <p>
     * This method is intended for shapes that do not require positional or dimensional parameters
     * at creation time, such as default-sized shapes or symbolic markers.
     * </p>
     *
     * @param colorFill the fill (interior) color of the shape
     * @param colorEdge the edge (stroke) color of the shape
     * @return a new {@code Shape} instance created with the given colors
     */
    public Shape createShape(Color colorFill, Color colorEdge) {
        Shape shape = createShapeWithParams(colorFill, colorEdge);
        setShape(shape);
        return shape;
    }

    /**
     * Creates a new {@link Shape} instance using full geometric and styling parameters.
     * <p>
     * This method is suitable for complex shapes that require positioning, sizing, font scaling,
     * transformations (scaling), and rotation at creation time.
     * </p>
     *
     * @param colorFill  the fill (interior) color of the shape
     * @param colorEdge  the edge (stroke) color of the shape
     * @param anchorX    the X coordinate of the anchor point
     * @param anchorY    the Y coordinate of the anchor point
     * @param dimensionX the horizontal dimension (e.g., width, radius)
     * @param dimensionY the vertical dimension (e.g., height, radius)
     * @param fontSize   the font size to be used (applicable for text-based shapes)
     * @param scaleX     the scale factor in the horizontal direction
     * @param scaleY     the scale factor in the vertical direction
     * @param angle      the rotation angle (in degrees) around the anchor point
     * @return a new {@code Shape} instance configured with the given parameters
     */
    public Shape createShape(Color colorFill, Color colorEdge, double anchorX, double anchorY, double dimensionX, double dimensionY, double fontSize, double scaleX, double scaleY, double angle) {
        Shape shape = createShapeWithParams(colorFill, colorEdge, anchorX, anchorY, dimensionX, dimensionY, fontSize, scaleX, scaleY, angle);
        setShape(shape);
        return shape;
    }

    /**
     * Creates a new {@link Shape} using the provided fill and edge colors and two additional parameters.
     * <p>
     * This abstract method must be implemented by subclasses to define how shapes with detailed
     * geometry should be constructed.
     * </p>
     *
     * @param fill       the fill color of the shape
     * @param edge       the edge (stroke) color of the shape
     * @param anchorX    the X coordinate of the anchor point (e.g., center, start point)
     * @param anchorY    the Y coordinate of the anchor point
     * @param dimensionX the horizontal dimension of the shape (e.g., width, radius)
     * @param dimensionY the vertical dimension of the shape (e.g., height, radius)
     * @return a fully configured {@code Shape} instance
     */
    protected abstract Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY);


    /**
     * Creates a new {@link Shape} using the provided fill and edge colors and two additional parameters.
     * <p>
     * This abstract method must be implemented by subclasses to define how shapes with detailed
     * geometry should be constructed.
     * </p>
     *
     * @param fill the fill color of the shape
     * @param edge the edge (stroke) color of the shape
     * @return a fully configured {@code Shape} instance
     */
    protected abstract Shape createShapeWithParams(Color fill, Color edge);

    /**
     * Creates a new {@link Shape} using the provided fill and edge colors, anchor coordinates,
     * dimensions, font size, scale factors, and rotation angle.
     * <p>
     * This abstract method must be implemented by subclasses that support complex shape construction
     * involving text or geometric transformations such as scaling and rotation.
     * </p>
     *
     * @param fill       the fill color of the shape
     * @param edge       the edge (stroke) color of the shape
     * @param anchorX    the X coordinate of the anchor point (e.g., center or origin)
     * @param anchorY    the Y coordinate of the anchor point
     * @param dimensionX the horizontal dimension (e.g., width, radius)
     * @param dimensionY the vertical dimension (e.g., height, radius)
     * @param fontSize   the font size, if applicable (e.g., for text shapes); ignored otherwise
     * @param scaleX     the horizontal scaling factor applied to the shape
     * @param scaleY     the vertical scaling factor applied to the shape
     * @param angle      the rotation angle in degrees, applied around the anchor point
     * @return a fully configured {@code Shape} instance, ready to be added to the scene
     */
    protected abstract Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY, double fontSize, double scaleX, double scaleY, double angle);
}
