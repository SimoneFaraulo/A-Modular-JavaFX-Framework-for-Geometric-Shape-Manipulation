package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.InteractionCommand;
import group2128.sadproject.sadproject.factory.EllipseFactory;
import group2128.sadproject.sadproject.factory.EllipseShape;
import group2128.sadproject.sadproject.factory.ShapeFactory;

/**
 * A concrete implementation of the {@link DrawingStrategy} interface for drawing ellipses.
 * <p>
 * This strategy is responsible for handling user input to create and render ellipses
 * on the canvas using the parameters defined in {@link DrawingParams}.
 * </p>
 */
public class EllipseDrawingStrategy implements DrawingStrategy {

    /**
     * Handles the drawing logic for an ellipse based on the given coordinates and drawing parameters.
     * <p>
     * This method should determine the bounds of the ellipse and render it
     * on the canvas using the edge and fill colors defined in {@code drawingParams}.
     * </p>
     *
     * @param x              the x-coordinate of the drawing action
     * @param y              the y-coordinate of the drawing action
     * @param drawingParams  the parameters containing canvas and styling information
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {
        ShapeFactory factory = new EllipseFactory();
        EllipseShape ellipse = (EllipseShape) factory.createShape(
                drawingParams.getFillColor(),
                drawingParams.getEdgeColor(),
                x,
                y,
                drawingParams.getWidthValuePropertyProperty().get(),
                drawingParams.getHeightValuePropertyProperty().get(),
                0,
                drawingParams.getScaleX(),
                drawingParams.getScaleY(),
                drawingParams.getRotationValueProperty().get()
        );

        ellipse.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                InteractionCommand intCommand = new InteractionCommand();
                intCommand.setDrawingCanvas(drawingParams.getDrawingCanvas());
                intCommand.saveBackup();
                intCommand.execute();
                drawingParams.getCommandHistory().push(intCommand);
                ellipse.setInteractionProperty(false);
            }
        });

        ellipse.setInteractionProperty(true);

        drawingParams.getDrawingCanvas().getChildren().add(ellipse);
    }

    /**
     * Called when the strategy is exited or replaced by another.
     * <p>
     * This method can be used to clear temporary shapes or finalize the ellipse drawing.
     * </p>
     *
     * @param drawingParams  the parameters containing canvas and styling information
     */
    @Override
    public void onExit(DrawingParams drawingParams) {
        // TODO: Implement cleanup logic when exiting the strategy
    }
}
