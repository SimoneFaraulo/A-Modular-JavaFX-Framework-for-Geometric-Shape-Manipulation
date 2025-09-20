package group2128.sadproject.sadproject.factory;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link EllipseShape}.
 * <p>
 * These tests verify the correct behavior of color settings, coordinate handling,
 * and default radii for the {@code EllipseShape} class.
 * </p>
 */
public class EllipseShapeTest {

    private EllipseShape ellipse;

    /**
     * Initializes a new {@code EllipseShape} instance before each test,
     * with predefined edge and fill colors.
     */
    @BeforeEach
    public void setUp() {
        ellipse = new EllipseShape(Color.BLACK, Color.BLUE);
    }

    /**
     * Verifies that the ellipse is initialized with the correct edge and fill colors.
     */
    @Test
    public void testInitialColors() {
        assertEquals(Color.BLUE, ellipse.getEdgeColor());
        assertEquals(Color.BLACK, ellipse.getFillColor());
    }

    /**
     * Verifies that setting a new edge color updates both the internal state and stroke.
     */
    @Test
    public void testSetColorEdge() {
        ellipse.setEdgeColor(Color.RED);
        assertEquals(Color.RED, ellipse.getEdgeColor());
        assertEquals(Color.RED, ellipse.getStroke());
    }

    /**
     * Verifies that setting a new fill color updates both the internal state and the fill.
     */
    @Test
    public void testSetFillColor() {
        ellipse.setFillColor(Color.GREEN);
        assertEquals(Color.GREEN, ellipse.getFillColor());
        assertEquals(Color.GREEN, ellipse.getFill());
    }

    /**
     * Tests setting the X and Y coordinates of the ellipse,
     * ensuring that the values are correctly stored and reflected in the shape's center.
     */
    @Test
    public void testSetAndGetCoordinates() {
        ellipse.setAnchorX(120.0);
        ellipse.setAnchorY(80.0);

        assertEquals(120.0, ellipse.getAnchorX(), 0.01);
        assertEquals(80.0, ellipse.getAnchorY(), 0.01);
        assertEquals(120.0, ellipse.getCenterX(), 0.01);
        assertEquals(80.0, ellipse.getCenterY(), 0.01);
    }

    /**
     * Verifies the default radius values for the X and Y axes.
     */
    @Test
    public void testRadiiDefaults() {
        assertEquals(50.0, ellipse.getRadiusX(), 0.01);
        assertEquals(60.0, ellipse.getRadiusY(), 0.01);
    }

    /**
     * Tests whether the {@code contains} method of the {@link EllipseShape} correctly identifies
     * if a point lies within the bounds of the ellipse.
     * <p>
     * The test sets the center of the ellipse and checks:
     * <ul>
     *   <li>A point exactly at the center should return {@code true}.</li>
     *   <li>A point far from the center should return {@code false}.</li>
     * </ul>
     */
    @Test
    public void testContainsPoint() {
        ellipse.setAnchorX(100.0);
        ellipse.setAnchorY(50.0);

        assertTrue(ellipse.contains(100.0, 50.0));

        assertFalse(ellipse.contains(200.0, 200.0));
    }

    /**
     * Tests that the {@code selectedProperty} is bound to a {@link DropShadow} effect.
     * <p>
     * When the shape is selected, a blue drop shadow should be applied.
     * When the shape is deselected, the effect should be removed.
     * <p>
     * This test runs on the JavaFX Application Thread using {@link Platform#startup}
     * and waits for completion using a {@link CountDownLatch}.
     *
     * @throws InterruptedException if the thread waiting for the JavaFX platform is interrupted
     */
    @Test
    public void testSelectionBindingEffect() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                assertNull(ellipse.getEffect());

                ellipse.setSelected(true);
                assertNotNull(ellipse.getEffect());
                assertInstanceOf(DropShadow.class, ellipse.getEffect());

                ellipse.setSelected(false);
                assertNull(ellipse.getEffect());
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    /**
     * Tests the drag initialization and drag behavior of the {@link EllipseShape}.
     * <p>
     * This test verifies that:
     * <ul>
     *   <li>An {@code EllipseShape} can be configured with colors, position, and radius.</li>
     *   <li>Calling {@code initDrag()} sets up the drag event handlers correctly.</li>
     *   <li>On mouse press, the cursor changes to {@code CLOSED_HAND} to indicate dragging.</li>
     *   <li>During mouse drag, the ellipse's position updates correctly based on the initial offset from the press point.</li>
     *   <li>On mouse release, the cursor reverts to {@code DEFAULT}.</li>
     * </ul>
     */
    @Test
    public void testInitDrag() {
        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.RED);
        ellipse.setSelected(true);
        ellipse.setAnchorX(100);
        ellipse.setAnchorY(120);
        ellipse.setRadiusX(50);
        ellipse.setRadiusY(100);
        ellipse.initDrag();
        MouseEvent pressEvent = new MouseEvent(
                MouseEvent.MOUSE_PRESSED,
                150, 150, 150, 150,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(ellipse, 150, 150)
        );
        ellipse.getOnMousePressed().handle(pressEvent);
        MouseEvent dragEvent = new MouseEvent(
                MouseEvent.MOUSE_DRAGGED,
                200, 200, 200, 200,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(ellipse, 200, 200)
        );
        ellipse.getOnMouseDragged().handle(dragEvent);
        assertEquals(Cursor.CLOSED_HAND, ellipse.getCursor());
        assertEquals(150.0, ellipse.getAnchorX(), 0.01);
        assertEquals(170.0, ellipse.getAnchorY(), 0.01);
        MouseEvent releaseEvent = new MouseEvent(
                MouseEvent.MOUSE_RELEASED,
                200, 200, 200, 200,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(ellipse, 200, 200)
        );
        ellipse.getOnMouseReleased().handle(releaseEvent);
        assertEquals(Cursor.DEFAULT, ellipse.getCursor());
    }

    /**
     * Tests the {@code getCopy} method of {@code EllipseShape}.
     * <p>
     * This test verifies that the method creates a new {@code EllipseShape} instance
     * that is a deep copy of the original, with the same fill color, edge color,
     * radiusX, radiusY, and position (X and Y).
     * It also asserts that the copied shape is a different object from the original.
     */
    @Test
    public void testGetCopy() {
        EllipseShape ellipse = new EllipseShape(Color.RED, Color.BLUE);
        ellipse.setDimensionX(40);
        ellipse.setDimensionY(20);
        ellipse.setAnchorX(100);
        ellipse.setAnchorY(150);
        SelectableShape copyShape = ellipse.getCopy();
        assertNotSame(ellipse, copyShape);
        assertInstanceOf(EllipseShape.class, copyShape);
        EllipseShape copiedShape = (EllipseShape) copyShape;
        assertEquals(ellipse.getFillColor(), copiedShape.getFillColor());
        assertEquals(ellipse.getEdgeColor(), copiedShape.getEdgeColor());
        assertEquals(ellipse.getDimensionX(), copiedShape.getDimensionX(), 0.001);
        assertEquals(ellipse.getDimensionY(), copiedShape.getDimensionY(), 0.001);
        assertEquals(ellipse.getAnchorX(), copiedShape.getAnchorX(), 0.001);
        assertEquals(ellipse.getAnchorY(), copiedShape.getAnchorY(), 0.001);
    }



    /**
     * Tests setting and retrieving the dimensions (width and height).
     */
    @Test
    public void testSetAndGetDimensions() {
        ellipse.setDimensionX(80.0);
        ellipse.setDimensionY(40.0);
        assertEquals(80.0, ellipse.getDimensionX(), 0.01);
        assertEquals(40.0, ellipse.getDimensionY(), 0.01);
    }

    /**
     * Tests the selectedProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testSelectedPropertyBehavior() {
        assertFalse(ellipse.isSelected(), "Default selectedProperty should be false");

        ellipse.selectedProperty().set(true);
        assertTrue(ellipse.selectedProperty().get(), "selectedProperty should be true after set");

        ellipse.setSelected(false);
        assertFalse(ellipse.isSelected(), "selectedProperty should reflect setSelected(false)");
    }

    /**
     * Tests the interactionProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testInteractionPropertyBehavior() {
        assertFalse(ellipse.interactionPropertyProperty().get(), "Default interactionProperty should be false");

        ellipse.setInteractionProperty(true);
        assertTrue(ellipse.interactionPropertyProperty().get(), "interactionProperty should be true after set");

        ellipse.setInteractionProperty(false);
        assertFalse(ellipse.interactionPropertyProperty().get(), "interactionProperty should be false after reset");
    }

}
