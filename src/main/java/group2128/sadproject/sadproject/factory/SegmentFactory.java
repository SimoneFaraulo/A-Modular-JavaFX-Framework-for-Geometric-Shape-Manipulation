package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

/**
 * Factory class responsible for creating {@link SegmentShape} instances.
 * <p>
 * This class extends {@link ShapeFactory} and provides concrete implementations
 * of factory methods for producing {@code SegmentShape} objects with or without
 * additional geometric parameters. It follows the Factory Method design pattern.
 */
public class SegmentFactory extends ShapeFactory {

    /**
     * Creates a {@link SegmentShape} with all parameters provided.
     * <p>
     * This method constructs a new {@code SegmentShape} using the specified fill and edge colors,
     * as well as geometric parameters for positioning and sizing.
     *
     * @param fill       the fill color of the segment shape
     * @param edge       the edge (stroke) color of the segment shape
     * @param anchorX    the X coordinate of the segment's center point
     * @param anchorY    the Y coordinate of the segment's center point
     * @param dimensionX the horizontal dimension (e.g., length) of the segment
     * @param dimensionY the vertical dimension (e.g., thickness) of the segment
     * @return a new {@code SegmentShape} instance initialized with the given parameters
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
        Shape segment = new SegmentShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY);
        setShape(segment);
        return segment;
    }

    /**
     * Creates an {@link SegmentShape} with the specified fill and edge colors
     * <p>
     * This method is intended for use cases where the size of the segment will be set later
     * (e.g., during an interactive drawing process). Currently, this method returns {@code Shape}
     * and must be implemented if such functionality is required.
     * </p>
     *
     * @param fill the fill color of the segment
     * @param edge the edge (stroke) color of the segment
     * @return a new {@link SegmentShape} instance or {@code null} if not implemented
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge) {
        SelectableShape segment = new SegmentShape(fill, edge);
        setShape(segment);
        return segment;
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
        Shape segment = new SegmentShape(fill, edge, anchorX, anchorY, dimensionX, dimensionY, scaleX, scaleY, angle);
        setShape(segment);
        return segment;
    }
}
