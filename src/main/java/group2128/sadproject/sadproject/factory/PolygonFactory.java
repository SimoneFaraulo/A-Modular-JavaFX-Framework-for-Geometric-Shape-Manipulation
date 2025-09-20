package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

/**
 * A concrete factory for creating {@link PolygonShape} instances.
 * <p>
 * This factory is responsible for producing polygon shapes using the specified colors and anchor point.
 * It extends {@link ShapeFactory} and overrides its creation methods accordingly.
 * </p>
 */
public class PolygonFactory extends ShapeFactory{

    /**
     * Unsupported method for polygon creation with dimensions.
     * <p>
     * Polygons are generally defined by an arbitrary number of points, not by fixed width or height dimensions.
     * As such, this method is not implemented and returns {@code null}.
     * </p>
     *
     * @param fill       the fill color of the polygon
     * @param edge       the edge (stroke) color of the polygon
     * @param anchorX    the X coordinate of the anchor point
     * @param anchorY    the Y coordinate of the anchor point
     * @param dimensionX ignored for polygons
     * @param dimensionY ignored for polygons
     * @return {@code null}, since polygon dimensions are not defined by width/height
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
       return null;
    }

    /**
     * Creates a basic {@link PolygonShape} instance using the specified fill and edge colors
     * <p>
     * The actual geometry (i.e., list of points) of the polygon should be set later via {@code setPoints}.
     * This method provides the base polygon object to be configured with vertex data.
     * </p>
     *
     * @param fill    the fill color of the polygon
     * @param edge    the edge (stroke) color of the polygon
     * @return a new {@link PolygonShape} with the specified styling and anchor point
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge) {
        SelectableShape polygon = new PolygonShape(fill,edge);
        setShape(polygon);
        return polygon;
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
        SelectableShape polygon = new PolygonShape(fill,edge,scaleX,scaleY,angle);
        setShape(polygon);
        return polygon;
    }
}
