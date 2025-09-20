package group2128.sadproject.sadproject.memento;

import group2128.sadproject.sadproject.factory.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the {@link DrawingCanvasMemento} class.
 *
 * <p>This test suite ensures that the {@code DrawingCanvasMemento} correctly captures and restores
 * the state of an {@link AnchorPane} (the drawing canvas), including all supported custom shapes.
 * It verifies deep cloning of the shapes and their properties, ensuring that restored shapes
 * have the same attributes but are different instances in memory.</p>
 *
 * <p>Supported shapes tested include:
 * <ul>
 *     <li>{@link RectangleShape}</li>
 *     <li>{@link EllipseShape}</li>
 *     <li>{@link SegmentShape}</li>
 *     <li>{@link PolygonShape}</li>
 *     <li>{@link TextShape}</li>
 * </ul>
 *
 * The test cases also verify that:
 * <ul>
 *     <li>Unsupported or null nodes are not restored</li>
 *     <li>An empty canvas state is handled gracefully</li>
 * </ul>
 * </p>
 */
public class DrawingCanvasMementoTest {

    private AnchorPane canvas;

    @BeforeEach
    void setUp() {
        canvas = new AnchorPane();
    }

    /**
     * Tests that a {@link RectangleShape} is correctly cloned and restored with
     * the same properties but as a different instance.
     */
    @Test
    void testMementoClonesRectangleShape() {
        RectangleFactory factory = new RectangleFactory();
        RectangleShape rect = (RectangleShape) factory.createShape(Color.RED, Color.BLUE, 10, 10, 100, 50);
        canvas.getChildren().add(rect);

        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);

        canvas.getChildren().clear();
        assertEquals(0, canvas.getChildren().size());

        memento.restore();

        assertEquals(1, canvas.getChildren().size());
        RectangleShape restored = (RectangleShape) canvas.getChildren().get(0);
        assertNotSame(rect, restored);
        assertNotSame(rect.getFill(), restored.getFill());
        assertNotSame(rect.getEdgeColor(),restored.getEdgeColor());
        assertNotSame(rect.getDimensionX(), restored.getDimensionX());
        assertNotSame(rect.getDimensionY(), restored.getDimensionY());
        assertEquals(rect.getFillColor(), restored.getFillColor());
        assertEquals(rect.getEdgeColor(), restored.getEdgeColor());
        assertEquals(rect.getDimensionY(), restored.getDimensionY());
        assertEquals(rect.getDimensionX(), restored.getDimensionX());
        assertEquals(rect.getAnchorX(), restored.getAnchorX());
        assertEquals(rect.getAnchorY(), restored.getAnchorY());
    }

    /**
     * Tests that an {@link EllipseShape} is cloned and restored correctly.
     * Verifies that the shape instance is different but the properties are equal.
     */
    @Test
    void testMementoClonesEllipseShape() {
        EllipseFactory factory = new EllipseFactory();
        EllipseShape ellipse = (EllipseShape) factory.createShape(Color.GREEN, Color.BLACK, 20, 20, 40, 40);
        canvas.getChildren().add(ellipse);

        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();

        memento.restore();

        EllipseShape restored = (EllipseShape) canvas.getChildren().get(0);
        assertNotSame(ellipse, restored);
        assertNotSame(ellipse.getFill(), restored.getFill());
        assertNotSame(ellipse.getEdgeColor(),restored.getEdgeColor());
        assertNotSame(ellipse.getDimensionX(), restored.getDimensionX());
        assertNotSame(ellipse.getDimensionY(), restored.getDimensionY());
        assertEquals(ellipse.getFillColor(), restored.getFillColor());
        assertEquals(ellipse.getEdgeColor(), restored.getEdgeColor());
        assertEquals(ellipse.getDimensionX(), restored.getDimensionX());
        assertEquals(ellipse.getDimensionY(), restored.getDimensionY());
        assertEquals(ellipse.getAnchorX(), restored.getAnchorX());
        assertEquals(ellipse.getAnchorY(), restored.getAnchorY());
    }

    /**
     * Tests cloning and restoring of a {@link SegmentShape}.
     * Ensures coordinates, colors, and dimensions match the original.
     */
    @Test
    void testMementoClonesSegmentShape() {
        SegmentFactory factory = new SegmentFactory();
        SegmentShape segment = (SegmentShape) factory.createShape(Color.BLACK, Color.BLACK, 0, 0, 50, 50);
        canvas.getChildren().add(segment);

        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();

        memento.restore();

        SegmentShape restored = (SegmentShape) canvas.getChildren().get(0);
        assertNotSame(segment, restored);
        assertNotSame(segment.getFill(), restored.getFill());
        assertNotSame(segment.getEdgeColor(),restored.getEdgeColor());
        assertNotSame(segment.getDimensionX(), restored.getDimensionX());
        assertNotSame(segment.getDimensionY(), restored.getDimensionY());
        assertEquals(segment.getEdgeColor(), restored.getEdgeColor());
        assertEquals(segment.getDimensionX(), restored.getDimensionX());
        assertEquals(segment.getDimensionY(), restored.getDimensionY());
        assertEquals(segment.getAnchorX(), restored.getAnchorX());
        assertEquals(segment.getAnchorY(), restored.getAnchorY());
        assertEquals(segment.getEndPointX(), restored.getEndPointX());
        assertEquals(segment.getEndPointY(), restored.getEndPointY());
    }

    /**
     * Verifies that a {@link PolygonShape} is correctly cloned,
     * including its list of points, while remaining a different instance.
     */
    @Test
    void testMementoClonesPolygonShape() {
        PolygonFactory factory = new PolygonFactory();
        PolygonShape polygon = (PolygonShape) factory.createShape(Color.PURPLE, Color.GRAY);
        polygon.getPoints().addAll(0.0, 0.0, 50.0, 50.0, 100.0, 0.0);
        canvas.getChildren().add(polygon);

        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();

        memento.restore();

        PolygonShape restored = (PolygonShape) canvas.getChildren().get(0);
        assertNotSame(polygon, restored);
        assertNotSame(polygon.getPoints(), restored.getPoints());
        assertNotSame(polygon.getDimensionX(), restored.getDimensionX());
        assertNotSame(polygon.getDimensionY(), restored.getDimensionY());
        assertEquals(polygon.getEdgeColor(), restored.getEdgeColor());
        assertEquals(polygon.getFillColor(), restored.getFillColor());
        assertEquals(polygon.getAnchorX(), restored.getAnchorX());
        assertEquals(polygon.getAnchorY(), restored.getAnchorY());
        assertEquals(polygon.getDimensionX(), restored.getDimensionX());
        assertEquals(polygon.getDimensionY(), restored.getDimensionY());
        assertEquals(polygon.getPoints(), restored.getPoints());
    }

    /**
     * Tests cloning and restoring of a {@link TextShape}.
     * Asserts text content and styling is preserved.
     */
    @Test
    void testMementoClonesTextShape() {
        TextShapeFactory factory = new TextShapeFactory();
        TextShape text = (TextShape) factory.createShape(Color.BLACK, Color.BLACK, 30, 30, 100, 40, 10, 100,40, 0);
        text.setText("Hello");
        canvas.getChildren().add(text);

        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();

        memento.restore();
        TextShape restored = (TextShape) canvas.getChildren().get(0);
        assertNotSame(text, restored);
        assertNotSame(text.getText(), restored.getText());
        assertNotSame(text.getDimensionX(), restored.getDimensionX());
        assertNotSame(text.getDimensionY(), restored.getDimensionY());
        assertEquals(text.getText(), restored.getText());
        assertEquals(text.getFillColor(), restored.getFillColor());
        assertEquals(text.getEdgeColor(), restored.getEdgeColor());
        assertEquals(text.getAnchorY(), restored.getAnchorY());
        assertEquals(text.getAnchorX(), restored.getAnchorX());
        assertEquals(text.getFontSize(), restored.getFontSize());
        assertEquals(text.getFontSize(), restored.getFontSize());
    }

    /**
     * Tests that an unsupported node (e.g., {@link javafx.scene.Group}) is not restored
     * and ignored in the memento cloning process.
     */
    @Test
    void testRestoreWithUnsupportedNode() {
        Node unsupported = new Group();
        canvas.getChildren().add(unsupported);
        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();
        memento.restore();
        List<Node> restoredNodes = canvas.getChildren();
        assertTrue(restoredNodes.isEmpty());
    }

    /**
     * Verifies that restoring from a canvas that had only null or no supported nodes
     * results in no children being added to the canvas.
     */
    @Test
    void testRestoreWithNullNode() {
        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        canvas.getChildren().clear();
        memento.restore();
        assertTrue(canvas.getChildren().isEmpty());
    }

    /**
     * Ensures that restoring from an empty canvas snapshot
     * does not throw errors and results in an empty canvas.
     */
    @Test
    void testEmptyCanvasRestore() {
        DrawingCanvasMemento memento = new DrawingCanvasMemento(canvas);
        memento.restore();
        assertTrue(canvas.getChildren().isEmpty());
    }
}
