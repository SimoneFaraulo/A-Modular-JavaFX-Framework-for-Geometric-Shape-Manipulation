package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.InteractionCommand;
import group2128.sadproject.sadproject.factory.*;

public class TextDrawingStrategy implements DrawingStrategy {
    /**
     * Performs a drawing operation at the specified coordinates using the provided parameters.
     *
     * @param x             the x-coordinate where the drawing occurs
     * @param y             the y-coordinate where the drawing occurs
     * @param drawingParams the {@link DrawingParams} containing canvas and color information
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {
        ShapeFactory shapeFactory = new TextShapeFactory();
        TextShape textShape = (TextShape) shapeFactory.createShape(drawingParams.getFillColor(), drawingParams.getEdgeColor(),x,y, drawingParams.getScaleX(), drawingParams.getScaleY(), drawingParams.getFontSize(), drawingParams.getScaleX(), drawingParams.getScaleY(), drawingParams.getRotationValueProperty().get());
        textShape.setText(drawingParams.getText());

        textShape.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                InteractionCommand intCommand = new InteractionCommand();
                intCommand.setDrawingCanvas(drawingParams.getDrawingCanvas());
                intCommand.saveBackup();
                intCommand.execute();
                drawingParams.getCommandHistory().push(intCommand);
                textShape.setInteractionProperty(false);
            }
        });

        textShape.setInteractionProperty(true);
        drawingParams.getDrawingCanvas().getChildren().add(textShape);
    }

    /**
     * Called when the strategy is deactivated or switched.
     * <p>
     * This method can be used to clean up temporary states or reset visual effects.
     * </p>
     *
     * @param drawingParams the {@link DrawingParams} used by the strategy
     */
    @Override
    public void onExit(DrawingParams drawingParams) {
        //Do nothing
    }
}
