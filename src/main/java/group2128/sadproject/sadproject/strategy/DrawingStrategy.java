package group2128.sadproject.sadproject.strategy;

/**
 * Strategy interface for defining drawing behaviors in the drawing application.
 * <p>
 * Implementations of this interface encapsulate specific drawing logic that can be
 * dynamically selected and applied using a {@link DrawingContext}.
 * </p>
 */
public interface DrawingStrategy {

    /**
     * Performs a drawing operation at the specified coordinates using the provided parameters.
     *
     * @param x              the x-coordinate where the drawing occurs
     * @param y              the y-coordinate where the drawing occurs
     * @param drawingParams  the {@link DrawingParams} containing canvas and color information
     */
    void draw(double x, double y, DrawingParams drawingParams);

    /**
     * Called when the strategy is deactivated or switched.
     * <p>
     * This method can be used to clean up temporary states or reset visual effects.
     * </p>
     *
     * @param drawingParams  the {@link DrawingParams} used by the strategy
     */
    void onExit(DrawingParams drawingParams);
}
