package group2128.sadproject.sadproject.memento;

import group2128.sadproject.sadproject.factory.*;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import java.util.List;

/**
 * A memento class that captures and stores the state of a drawing canvas.
 *
 * <p>This class implements the Memento design pattern and is used to create a snapshot
 * of an {@link AnchorPane} (representing the drawing canvas), including all its child nodes.
 * It allows restoring the canvas to a previous state by re-adding the cloned shapes.</p>
 *
 * @see Memento
 */
public class DrawingCanvasMemento implements Memento {

    private final List<Node> nodeList;
    private final AnchorPane drawingCanvas;

    /**
     * Constructs a {@code DrawingCanvasMemento} by cloning all nodes from the given canvas.
     *
     * <p>Only shapes that implement {@link SelectableShape} and are created via supported
     * {@link ShapeFactory} implementations (e.g., {@link RectangleFactory}, {@link EllipseFactory}) are cloned.</p>
     *
     * @param drawingCanvas the original {@code AnchorPane} from which to create a snapshot
     */
    public DrawingCanvasMemento(AnchorPane drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
        this.nodeList = new ArrayList<>();
        for (Node child : drawingCanvas.getChildren()) {
            nodeList.add(cloneNode(child));
        }
    }

    /**
     * Returns a new {@link AnchorPane} that will serve as the container for restoring the saved nodes.
     *
     * @return an empty {@code AnchorPane} ready to be populated via {@link #restore()}
     */
    public AnchorPane getDrawingCanvas() {
        return drawingCanvas;
    }

    /**
     * Restores the saved nodes to the {@code drawingCanvas}.
     *
     * <p>This method repopulates the canvas with the cloned nodes that were stored during construction,
     * effectively restoring it to its previous visual state.</p>
     */
    public void restore() {
        drawingCanvas.getChildren().clear();
        for (Node node : nodeList) {
            if (node!=null) {
                drawingCanvas.getChildren().add(node);
            }
        }
    }

    /**
     * Clones a supported {@link Node} (RectangleShape, EllipseShape, SegmentShape, PolygonShape, TextShape)
     * by creating a new instance with copied properties.
     *
     * @param node the {@code Node} to clone
     * @return a cloned {@code Node}, or {@code null} if the node type is unsupported
     */
    private Node cloneNode(Node node) {
        if (node instanceof RectangleShape) {
            RectangleShape r = (RectangleShape) node;
            return (Node) r.getCopy();
        } else if (node instanceof EllipseShape) {
            EllipseShape e = (EllipseShape) node;
            return (Node) e.getCopy();
        } else if (node instanceof SegmentShape) {
            SegmentShape s = (SegmentShape) node;
            return (Node) s.getCopy();
        } else if (node instanceof PolygonShape) {
            PolygonShape p = (PolygonShape) node;
            return (Node) p.getCopy();
        } else if (node instanceof TextShape) {
            TextShape t = (TextShape) node;
            return (Node) t.getCopy();
        }
        return null;
    }
}
