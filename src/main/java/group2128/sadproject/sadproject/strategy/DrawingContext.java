package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.factory.SelectableShape;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Context class for managing drawing behavior using the Strategy design pattern.
 * <p>
 * This class maintains a reference to a {@link DrawingStrategy} that defines how to handle
 * drawing operations, and it holds the shared drawing parameters used by strategies.
 * </p>
 */
public class DrawingContext {

    /**
     * The current drawing strategy used to handle drawing behavior.
     */
    private DrawingStrategy currentStrategy;

    /**
     * The parameters used for drawing, including canvas and colors.
     */
    private DrawingParams drawingParams;

    /**
     * The currently selected shape in the drawing area.
     *
     * <p>This reference is used to track which {@code SelectableShape} is currently selected
     * by the user. It allows operations such as highlighting, dragging, or showing a context menu
     * to be performed only on the selected shape. It is {@code null} when no shape is selected.</p>
     */
    private SelectableShape selectedShape;

    /**
     * Constructs a new {@code DrawingContext} with the specified drawing canvas.
     *
     * @param drawingCanvas the {@link AnchorPane} used as the drawing surface
     */
    public DrawingContext(AnchorPane drawingCanvas) {
        this.drawingParams = new DrawingParams();
        this.drawingParams.setDrawingCanvas(drawingCanvas);
    }

    /**
     * Sets the current drawing strategy.
     *
     * @param mode the {@link DrawingStrategy} to be used
     */
    public void setStrategyMode(DrawingStrategy mode) {
        this.currentStrategy = mode;
    }


    /**
     * Sets the currently selected shape.
     * <p>
     * This method updates the reference to the shape that is currently selected
     * by the user, typically in response to a selection event on the canvas.
     * </p>
     *
     * @param selectedShape the {@link SelectableShape} to set as selected
     */
    public void setSelectedShape(SelectableShape selectedShape) {
        this.selectedShape = selectedShape;
    }

    /**
     * Handles a click event at the given coordinates.
     * <p>
     * This method is intended to delegate the event to the current drawing strategy.
     * Currently, it is not implemented.
     * </p>
     *
     * @param x the x-coordinate of the click
     * @param y the y-coordinate of the click
     */
    public void handleClick(double x, double y) {
        clearSelectedShape();

        if (this.currentStrategy instanceof IdleStrategy) {
            ObservableList<Node> children = drawingParams.getDrawingCanvas().getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                Node node = children.get(i);
                if (node instanceof SelectableShape) {
                    SelectableShape shape = (SelectableShape) node;
                    if(shape.contains(x,y)) {
                        selectedShape = shape;
                        drawingParams.setEdgeColor(selectedShape.getEdgeColor());
                        drawingParams.setFillColor(selectedShape.getFillColor());
                        shape.setSelected(true);
                        return;
                    }
                }
            }
        } else {
            currentStrategy.draw(x,y,drawingParams);
        }

    }

    /**
     * Finalizes the shape currently being drawn, if applicable.
     * <p>
     * This method is specifically intended for strategies like {@link PolygonDrawingStrategy},
     * where the shape is built incrementally (e.g., by clicking multiple points), and needs an explicit
     * call to complete its construction. If the current strategy is not of type {@code PolygonDrawingStrategy},
     * the method performs no action.
     * </p>
     */
    public void completeShape(){
        if(currentStrategy.getClass().equals(PolygonDrawingStrategy.class)){
            ((PolygonDrawingStrategy)currentStrategy).completeShape(drawingParams);
        }
    }

    /**
     * Clears the current shape selection.
     *
     * <p>If a shape is currently selected, this method deselects it by setting its
     * selection state to {@code false}, and then sets the internal reference to {@code null}.
     * This is typically used to reset the selection when the user clicks outside any shape
     * or selects a different tool.</p>
     */
    private void clearSelectedShape() {
        if (selectedShape != null) {
            selectedShape.setSelected(false);
            selectedShape = null;
        }
    }

    /**
     * Returns the current drawing parameters associated with this object.
     * <p>
     * The {@code DrawingParams} object typically contains styling and configuration
     * details such as stroke width, color, or other rendering options used
     * during drawing operations.
     * </p>
     *
     * @return the {@link DrawingParams} used for rendering.
     */
    public DrawingParams getDrawingParams() {
        return drawingParams;
    }

    /**
     * Returns the currently selected shape in the drawing area.
     *
     * @return the {@code SelectableShape} that is currently selected, or {@code null} if no shape is selected
     */
    public SelectableShape getSelectedShape() {
        return selectedShape;
    }
}
