package group2128.sadproject.sadproject.strategy;

/**
 * A concrete implementation of the {@link DrawingStrategy} interface that performs no drawing actions.
 * <p>
 * This strategy represents an idle or inactive state where no shapes are drawn in response to user input.
 * It can be used as a default or fallback mode when no drawing behavior is desired.
 * </p>
 */
public class IdleStrategy implements DrawingStrategy {

    /**
     * Does nothing. This method is intentionally left empty to represent idle behavior.
     *
     * @param x              the x-coordinate of the (ignored) drawing action
     * @param y              the y-coordinate of the (ignored) drawing action
     * @param drawingParams  the parameters containing canvas and styling information (ignored)
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {
        // No operation performed in idle mode
    }

    /**
     * Does nothing. This method is intentionally left empty as there is no state to clean up in idle mode.
     *
     * @param drawingParams  the parameters used by the drawing context (ignored)
     */
    @Override
    public void onExit(DrawingParams drawingParams) {
        // No cleanup needed in idle mode
    }
}
