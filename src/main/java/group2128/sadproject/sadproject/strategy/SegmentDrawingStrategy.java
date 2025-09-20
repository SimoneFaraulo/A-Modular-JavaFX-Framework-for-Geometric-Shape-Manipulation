package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.InteractionCommand;
import group2128.sadproject.sadproject.factory.SegmentFactory;
import group2128.sadproject.sadproject.factory.SegmentShape;
import group2128.sadproject.sadproject.factory.ShapeFactory;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of the {@link DrawingStrategy} interface for drawing segments (lines).
 * <p>
 * This strategy collects points on user interaction and draws line segments on the canvas.
 * It also manages a {@link Group} for grouping and manipulating the drawn shapes.
 * </p>
 */
public class SegmentDrawingStrategy implements DrawingStrategy {

    /**
     * The list of points used to define line segments (x1, y1, x2, y2).
     */
    private final List<Double> points = new ArrayList<>();

    /**
     * A JavaFX {@link Group} used to manage and group the graphical elements drawn on the canvas.
     */
    private final Group group = new Group();


    /**
     * Handles the drawing logic for a segment based on the given coordinates and drawing parameters.
     * <p>
     * This method should collect points and draw a segment when appropriate.
     * </p>
     *
     * @param x              the x-coordinate of the drawing action
     * @param y              the y-coordinate of the drawing action
     * @param drawingParams  the parameters containing canvas and styling information
     */
    @Override
    public void draw(double x, double y, DrawingParams drawingParams) {

        points.add(x);
        points.add(y);
        Circle c = new Circle(x,y,3 , Color.BLACK);
        group.getChildren().add(c);

        drawingParams.getDrawingCanvas().getChildren().remove(group);
        drawingParams.getDrawingCanvas().getChildren().add(group);

        if(points.size() == 4) {
            ShapeFactory factory = new SegmentFactory();
            SegmentShape segment = (SegmentShape) factory.createShape(
                    drawingParams.getFillColor(),
                    drawingParams.getEdgeColor(),
                    points.get(0),
                    points.get(1),
                    points.get(2),
                    points.get(3),
                    0,
                    drawingParams.getScaleX(),
                    drawingParams.getScaleY(),
                    drawingParams.getRotationValueProperty().get());

            segment.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    InteractionCommand intCommand = new InteractionCommand();
                    intCommand.setDrawingCanvas(drawingParams.getDrawingCanvas());
                    intCommand.saveBackup();
                    intCommand.execute();
                    drawingParams.getCommandHistory().push(intCommand);
                    segment.setInteractionProperty(false);
                }
            });
            segment.setInteractionProperty(true);

            drawingParams.getDrawingCanvas().getChildren().add(segment);
            drawingParams.getDrawingCanvas().getChildren().remove(group);

            group.getChildren().clear();
            points.clear();
        }
    }

    /**
     * Called when the strategy is exited or changed.
     * <p>
     * Can be used to clear temporary drawings or finalize the current segment.
     * </p>
     *
     * @param drawingParams  the parameters containing canvas and styling information
     */
    @Override
    public void onExit(DrawingParams drawingParams) {
        clear(drawingParams.getDrawingCanvas());
    }

    /**
     * Clears temporary elements or drawings from the canvas.
     *
     * @param canvas the {@link AnchorPane} from which elements should be removed
     */
    private void clear(AnchorPane canvas) {
        canvas.getChildren().remove(group);
        group.getChildren().clear();
        points.clear();
    }
}
