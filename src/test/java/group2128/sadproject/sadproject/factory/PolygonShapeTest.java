package group2128.sadproject.sadproject.factory;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PolygonShape} class.
 * <p>
 * This test suite covers:
 * <ul>
 *     <li>Constructor behavior and color assignments</li>
 *     <li>Point manipulation methods (add/set/get)</li>
 *     <li>Selection handling</li>
 *     <li>Anchor coordinates</li>
 *     <li>Dimension calculation</li>
 *     <li>Copy creation</li>
 *     <li>Interaction property management</li>
 * </ul>
 * </p>
 */
class PolygonShapeTest {

    /**
     * Tests that the constructor correctly sets the fill and edge colors,
     * and initializes the shape as unselected.
     */
    @Test
    void testConstructorAndGetters() {
        PolygonShape polygon = new PolygonShape(Color.RED, Color.BLACK);
        assertEquals(Color.RED, polygon.getFillColor());
        assertEquals(Color.BLACK, polygon.getEdgeColor());
        assertFalse(polygon.isSelected());
    }

    /**
     * Tests setting and retrieving polygon points.
     * Verifies that the points list is properly updated.
     */
    @Test
    void testSetAndGetPoints() {
        PolygonShape polygon = new PolygonShape(Color.BLUE, Color.GREEN);
        List<Double> points = Arrays.asList(0.0, 0.0, 50.0, 0.0, 25.0, 50.0);
        polygon.setPoints(points);

        ObservableList<Double> actualPoints = polygon.getPointsList();
        assertEquals(points.size(), actualPoints.size());
        assertEquals(points, actualPoints);
    }

    /**
     * Tests the {@code addPoint} method by adding individual coordinates
     * and verifying they are added to the list.
     */
    @Test
    void testAddPoint() {
        PolygonShape polygon = new PolygonShape(Color.BLUE, Color.GREEN);
        polygon.addPoint(10.0);
        polygon.addPoint(20.0);
        ObservableList<Double> points = polygon.getPointsList();
        assertEquals(2, points.size());
        assertEquals(10.0, points.get(0));
        assertEquals(20.0, points.get(1));
    }

    /**
     * Tests selection state changes using {@code setSelected} and {@code isSelected}.
     */
    @Test
    void testSelection() {
        PolygonShape polygon = new PolygonShape(Color.YELLOW, Color.PURPLE);
        polygon.setSelected(true);
        assertTrue(polygon.isSelected());
        polygon.setSelected(false);
        assertFalse(polygon.isSelected());
    }

    /**
     * Tests setting and retrieving anchor coordinates (X and Y).
     */
    @Test
    void testAnchorMethods() {
        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.GRAY);
        polygon.setAnchorX(100.0);
        polygon.setAnchorY(200.0);
        assertEquals(100.0, polygon.getAnchorX());
        assertEquals(200.0, polygon.getAnchorY());
    }

    /**
     * Tests calculation of horizontal (X) and vertical (Y) dimensions.
     * Verifies they are computed from the difference between min and max coordinates.
     */
    @Test
    void testDimensionXandY() {
        PolygonShape polygon = new PolygonShape(Color.RED, Color.BLACK);
        polygon.setPoints(Arrays.asList(10.0, 20.0, 30.0, 50.0, 70.0, 10.0));
        assertEquals(60.0, polygon.getDimensionX());
        assertEquals(40.0, polygon.getDimensionY());
    }

    /**
     * Tests the {@code getCopy} method.
     * Ensures that the copy has the same properties and points,
     * but is a different instance.
     */
    @Test
    void testCopy() {
        List<Double> points = Arrays.asList(10.0, 10.0, 40.0, 40.0, 20.0, 60.0);
        PolygonShape polygon = new PolygonShape(Color.RED, Color.BLUE, points);
        SelectableShape copy = polygon.getCopy();

        assertNotSame(polygon, copy);
        assertEquals(polygon.getFillColor(), copy.getFillColor());
        assertEquals(polygon.getEdgeColor(), copy.getEdgeColor());
        assertEquals(polygon.getPointsList(), ((PolygonShape) copy).getPointsList());
    }

    /**
     * Tests setting and retrieving the interaction property.
     */
    @Test
    void testInteractionProperty() {
        PolygonShape polygon = new PolygonShape(Color.RED, Color.BLUE);
        polygon.setInteractionProperty(true);
        assertTrue(polygon.interactionPropertyProperty().get());
    }
}
