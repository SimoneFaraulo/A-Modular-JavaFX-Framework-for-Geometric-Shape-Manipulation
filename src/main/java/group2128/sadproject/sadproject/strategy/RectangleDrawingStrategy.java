package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.InteractionCommand;
import group2128.sadproject.sadproject.factory.RectangleFactory;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.factory.ShapeFactory;

/**
 * A concrete implementation of the {@link DrawingStrategy} interface for drawing rectangles.
 * <p>
 * This strategy handles user interactions to define and render rectangles on the canvas,
 * using the drawing parameters such as edge color, fill color, and the drawing surface.
 * </p>
 */
public class RectangleDrawingStrategy implements DrawingStrategy {

    /**
     * Handles the drawing logic for a rectangle based on the given coordinates and drawing parameters.
     * <p>
     * This method should determine the position and dimensions of the rectangle and render it
     * on the canvas provided in {@code drawingParams}.
     * </p>
     *
     * @param x             the x-coordinate of the drawing action
     * @param y             the y-coordinate of the drawing action
     * @param drawingParams the parameters containing canvas and styling information
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {

        ShapeFactory shapeFactory = new RectangleFactory();
        RectangleShape rectangleShape = (RectangleShape) shapeFactory.createShape(drawingParams.getFillColor(), drawingParams.getEdgeColor(),x,y, drawingParams.getWidthValuePropertyProperty().get(), drawingParams.getHeightValuePropertyProperty().get(), 0, drawingParams.getScaleX(), drawingParams.getScaleY(), drawingParams.getRotationValueProperty().get());

        rectangleShape.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                InteractionCommand intCommand = new InteractionCommand();
                intCommand.setDrawingCanvas(drawingParams.getDrawingCanvas());
                intCommand.saveBackup();
                intCommand.execute();
                drawingParams.getCommandHistory().push(intCommand);
                rectangleShape.setInteractionProperty(false);
            }
        });
        rectangleShape.setInteractionProperty(true);
        drawingParams.getDrawingCanvas().getChildren().add(rectangleShape);
    }

    /**
     * Called when the strategy is exited or replaced by another.
     * <p>
     * This method can be used to clean up any temporary shapes or reset state variables
     * related to rectangle drawing.
     * </p>
     *
     * @param drawingParams the parameters containing canvas and styling information
     */
    @Override
    public void onExit(DrawingParams drawingParams) {
        // TODO: Implement cleanup logic when exiting the strategy
    }
}