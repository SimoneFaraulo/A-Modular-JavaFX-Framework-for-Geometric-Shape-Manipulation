package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

/**
 * A concrete factory for creating {@link EllipseShape} instances.
 * <p>
 * This class extends the abstract {@link ShapeFactory} and provides implementations
 * for creating ellipse shapes with specified parameters, such as fill and edge colors,
 * and optionally position and dimensions.
 * </p>
 */
public class EllipseFactory extends ShapeFactory {

    /**
     * Creates a fully parameterized {@link EllipseShape} with the specified colors, anchor point,
     * and dimensions.
     *
     * @param fill       the fill color of the ellipse
     * @param edge       the edge (stroke) color of the ellipse
     * @param anchorX    the x-coordinate of the ellipse's anchor point
     * @param anchorY    the y-coordinate of the ellipse's anchor point
     * @param dimensionX the horizontal radius (or width factor) of the ellipse
     * @param dimensionY the vertical radius (or height factor) of the ellipse
     * @return a new {@link EllipseShape} instance initialized with the given parameters
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
        EllipseShape ellipse = new EllipseShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY);
        setShape(ellipse);
        return ellipse;
    }

    /**
     * Creates an {@link EllipseShape} with the specified fill and edge colors
     * <p>
     * This method is intended for use cases where the size of the ellipse will be set later
     * (e.g., during an interactive drawing process). Currently, this method returns {@code Shape}
     * and must be implemented if such functionality is required.
     * </p>
     *
     * @param fill the fill color of the ellipse
     * @param edge the edge (stroke) color of the ellipse
     * @return a new {@link EllipseShape} instance
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge) {
        SelectableShape ellipse = new EllipseShape(fill, edge);
        setShape(ellipse);
        return ellipse;
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
        EllipseShape ellipse = new EllipseShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY, scaleX, scaleY, angle);
        setShape(ellipse);
        return ellipse;
    }
}
