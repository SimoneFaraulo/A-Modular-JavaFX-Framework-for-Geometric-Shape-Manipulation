package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

/**
 * A factory class for creating {@link RectangleShape} instances.
 * <p>
 * This class extends {@link ShapeFactory} and implements the required methods to construct
 * rectangular shapes with various levels of customization, including size and position parameters
 * as well as fill and edge colors.
 * </p>
 */
public class RectangleFactory extends ShapeFactory {

    /**
     * Creates a new {@link RectangleShape} using all available shape parameters.
     * <p>
     * This method allows full customization of the rectangle, including its fill and edge colors,
     * its anchor point (top-left corner), and its dimensions (width and height).
     * The resulting shape is also stored in the factory via {@code setShape}.
     * </p>
     *
     * @param fill       the fill color of the rectangle
     * @param edge       the edge (stroke) color of the rectangle
     * @param anchorX    the X coordinate of the rectangle's anchor point
     * @param anchorY    the Y coordinate of the rectangle's anchor point
     * @param dimensionX the width of the rectangle
     * @param dimensionY the height of the rectangle
     * @return a new {@link RectangleShape} instance initialized with the provided parameters
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
        Shape rectangle = new RectangleShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY);
        setShape(rectangle);
        return rectangle;
    }

    /**
     * Creates an {@link RectangleShape} with the specified fill and edge colors
     * <p>
     * This method is intended for use cases where the size of the rectangle will be set later
     * (e.g., during an interactive drawing process). Currently, this method returns {@code Shape}
     * and must be implemented if such functionality is required.
     * </p>
     *
     * @param fill the fill color of the rectangle
     * @param edge the edge (stroke) color of the rectangle
     * @return a new {@link RectangleShape} instance or {@code null} if not implemented
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge) {
        SelectableShape rectangle = new RectangleShape(fill, edge);
        setShape(rectangle);
        return rectangle;
    }

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
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY, double fontSize, double scaleX, double scaleY, double angle) {
        Shape rectangle = new RectangleShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY, scaleX, scaleY, angle);
        setShape(rectangle);
        return rectangle;
    }
}
