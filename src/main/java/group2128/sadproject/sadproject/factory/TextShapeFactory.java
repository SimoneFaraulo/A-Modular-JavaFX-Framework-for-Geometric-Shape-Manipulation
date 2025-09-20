package group2128.sadproject.sadproject.factory;

import javafx.scene.paint.Color;

public class TextShapeFactory extends ShapeFactory {
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
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge, double anchorX, double anchorY, double dimensionX, double dimensionY) {
        return null;
    }

    /**
     * Not implemented: text shape creation requires both width and height to define the text box area.
     * <p>
     * This method is intentionally not implemented because a {@link TextShape} needs full
     * dimension parameters to be properly rendered and positioned. Without width and height,
     * it is not possible to allocate space or apply formatting accurately.
     * </p>
     *
     * @param fill the fill color of the text background or content
     * @param edge the edge (stroke) color of the text bounding box
     * @return {@code null}, as text shape creation without dimensions is unsupported
     */
    @Override
    protected Shape createShapeWithParams(Color fill, Color edge) {
        return null;
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
        TextShape text = new TextShape(anchorX,anchorY, dimensionX, dimensionY, fontSize, fill, edge, angle);
        setShape(text);
        return text;
    }
}
