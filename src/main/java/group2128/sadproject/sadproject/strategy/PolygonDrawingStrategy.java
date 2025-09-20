package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.InteractionCommand;
import group2128.sadproject.sadproject.factory.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public class PolygonDrawingStrategy implements DrawingStrategy {

    /**
     * The list of points used to define polygon.
     */
    private final List<Double> points = new ArrayList<>();

    /**
     * A JavaFX {@link Group} used to manage and group the graphical elements drawn on the canvas.
     */
    private final Group group = new Group();


    /**
     * Performs a drawing operation at the specified coordinates using the provided parameters.
     *
     * @param x             the x-coordinate where the drawing occurs
     * @param y             the y-coordinate where the drawing occurs
     * @param drawingParams the {@link DrawingParams} containing canvas and color information
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {

        points.add(x);
        points.add(y);
        Circle c = new Circle(x, y, 3, Color.BLACK);
        group.getChildren().add(c);

        drawingParams.getDrawingCanvas().getChildren().remove(group);
        drawingParams.getDrawingCanvas().getChildren().add(group);

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
        drawingParams.getDrawingCanvas().getChildren().remove(group);
        group.getChildren().clear();
        points.clear();
    }

    /**
     * Finalizes the creation of a polygon shape and adds it to the drawing canvas.
     * <p>
     * This method is typically called after the user has input enough points to define a polygon
     * (at least 3 vertices, i.e., 6 coordinate values). It creates a {@link PolygonShape}
     * using a {@link PolygonFactory}, sets its points, and adds it to the canvas.
     * </p>
     *
     * <p>
     * An interaction listener is also attached to the polygon shape. When interaction is triggered,
     * an {@link InteractionCommand} is executed, and a snapshot of the canvas is pushed to the
     * command history for potential undo/redo functionality.
     * </p>
     *
     * @param drawingParams the drawing parameters including fill color, edge color, drawing canvas,
     *                      and command history
     */
    public void completeShape(DrawingParams drawingParams) {
        if (points.size() >= 6) {
            ShapeFactory factory = new PolygonFactory();
            PolygonShape polygon = (PolygonShape) factory.createShape(drawingParams.getFillColor(), drawingParams.getEdgeColor(),0,0,0,0,0, drawingParams.getScaleX(), drawingParams.getScaleY(), drawingParams.getRotationValueProperty().get());
            polygon.setPoints(points);

            polygon.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    InteractionCommand intCommand = new InteractionCommand();
                    intCommand.setDrawingCanvas(drawingParams.getDrawingCanvas());
                    intCommand.saveBackup();
                    intCommand.execute();
                    drawingParams.getCommandHistory().push(intCommand);
                    polygon.setInteractionProperty(false);
                }
            });
            polygon.setInteractionProperty(true);

            drawingParams.getDrawingCanvas().getChildren().add(polygon);
            drawingParams.getDrawingCanvas().getChildren().remove(group);

            group.getChildren().clear();
            points.clear();
        }
    }
}
