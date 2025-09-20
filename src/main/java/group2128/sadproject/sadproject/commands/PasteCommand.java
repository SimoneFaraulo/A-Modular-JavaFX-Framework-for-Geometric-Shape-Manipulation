package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.*;
import group2128.sadproject.sadproject.strategy.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A command that pastes a previously copied shape onto the drawing canvas
 * at a specified location.
 * <p>
 * The shape is placed at the coordinates defined by {@code pasteX} and {@code pasteY},
 * and is added to the canvas. If the shape already exists on the canvas, a copy is created
 * before pasting.
 * </p>
 */
public class PasteCommand extends Command {

    /**
     * The X coordinate where the shape will be pasted on the drawing pane.
     */
    private Double pasteX;

    /**
     * The Y coordinate where the shape will be pasted on the drawing pane.
     */
    private Double pasteY;

    /**
     * The shape to be pasted. It is expected to be a previously copied shape.
     */
    private SelectableShape shape;

    /**
     * The drawing parameters to be used when rendering the pasted shape,
     * including dimensions, colors, and command history.
     */
    private DrawingParams drawingParams;

    /**
     * The specific drawing strategy used to render the shape being pasted.
     * It is selected dynamically based on the shape's type.
     */
    private DrawingStrategy pasteDrawingStrategy;

    /**
     * Sets the X coordinate where the shape will be pasted.
     *
     * @param pasteX the X coordinate
     */
    public void setPasteX(Double pasteX) {
        this.pasteX = pasteX;
    }

    /**
     * Sets the Y coordinate where the shape will be pasted.
     *
     * @param pasteY the Y coordinate
     */
    public void setPasteY(Double pasteY) {
        this.pasteY = pasteY;
    }

    /**
     * Sets the shape to be pasted.
     *
     * @param shape the shape to paste
     */
    public void setShape(SelectableShape shape) {
        this.shape = shape;
    }

    /**
     * Executes the paste command.
     * <p>
     * If the shape is already present on the canvas, a new copy is made.
     * The shape's position is updated to the specified paste coordinates.
     * Depending on the shape type (rectangle, ellipse, segment, text, polygon), additional
     * properties such as endpoints may also be updated.
     * </p>
     */
    @Override
    public void execute() {
        if(shape instanceof RectangleShape){
            pasteDrawingStrategy = new RectangleDrawingStrategy();
            pasteDrawingStrategy.draw(this.pasteX,this.pasteY, drawingParams);
        }
        if(shape instanceof EllipseShape){
            pasteDrawingStrategy = new EllipseDrawingStrategy();
            pasteDrawingStrategy.draw(this.pasteX,this.pasteY, drawingParams);
        }
        if(shape instanceof SegmentShape){
            pasteDrawingStrategy = new SegmentDrawingStrategy();
            double finalPasteX = ((((SegmentShape) shape).getEndPointX()) - shape.getAnchorX()) + this.pasteX;
            double finalPasteY = ((((SegmentShape) shape).getEndPointY()) - shape.getAnchorY()) + this.pasteY;
            pasteDrawingStrategy.draw(this.pasteX,this.pasteY, drawingParams);
            pasteDrawingStrategy.draw(finalPasteX, finalPasteY, drawingParams);
        }
        if (shape instanceof TextShape){
            pasteDrawingStrategy = new TextDrawingStrategy();
            pasteDrawingStrategy.draw(this.pasteX,this.pasteY, drawingParams);
        }
        if (shape instanceof PolygonShape){
            pasteDrawingStrategy = new PolygonDrawingStrategy();
            List<Double> originalPoints = ((PolygonShape) shape).getPoints();
            List<Double> points = new ArrayList<>(originalPoints);

            double dx = this.pasteX - points.get(0);
            double dy = this.pasteY - points.get(1);

            for (int i = 0; i < points.size(); i += 2) {
                double newX = points.get(i) + dx;
                double newY = points.get(i + 1) + dy;
                pasteDrawingStrategy.draw(newX, newY, drawingParams);
            }
            ((PolygonDrawingStrategy) pasteDrawingStrategy).completeShape(drawingParams);
        }
    }

    /**
     * Undoes the previously executed fill color change command.
     *
     * <p>If a memento of the drawing canvas is available, this method restores
     * the canvas to its previous state before the fill color was changed.</p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

    public void setDrawingParams(DrawingParams drawingParams) {
        this.drawingParams = drawingParams;
    }


}
