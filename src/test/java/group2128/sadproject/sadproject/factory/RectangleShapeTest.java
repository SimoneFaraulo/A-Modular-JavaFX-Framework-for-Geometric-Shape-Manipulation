package group2128.sadproject.sadproject.factory;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.input.MouseEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link RectangleShape} class.
 * <p>
 * These tests verify the correct initialization, color management,
 * coordinate handling, and default dimensions of a RectangleShape instance.
 */
public class RectangleShapeTest {

    private RectangleShape rectangle;

    /**
     * Sets up a default RectangleShape instance before each test.
     * Initializes it with fill color BLUE and edge color BLACK.
     */
    @BeforeEach
    public void setUp() {
        rectangle = new RectangleShape(Color.BLUE, Color.BLACK);
    }

    /**
     * Tests that the initial colors (fill and edge) of the rectangle
     * are correctly set in the constructor.
     */
    @Test
    public void testInitialColors() {
        assertEquals(Color.BLACK, rectangle.getEdgeColor());
        assertEquals(Color.BLUE, rectangle.getFillColor());
    }

    /**
     * Tests the ability to set the edge color of the rectangle.
     * Also verifies that the stroke property is updated accordingly.
     */
    @Test
    public void testSetColorEdge() {
        rectangle.setEdgeColor(Color.RED);
        assertEquals(Color.RED, rectangle.getEdgeColor());
        assertEquals(Color.RED, rectangle.getStroke());
    }

    /**
     * Tests the ability to set the fill color of the rectangle.
     * Also verifies that the fill property is updated accordingly.
     */
    @Test
    public void testSetFillColor() {
        rectangle.setFillColor(Color.GREEN);
        assertEquals(Color.GREEN, rectangle.getFillColor());
        assertEquals(Color.GREEN, rectangle.getFill());
    }

    /**
     * Tests setting and retrieving the top-left X and Y coordinates
     * of the rectangle. Ensures values are properly stored and returned.
     */
    @Test
    public void testSetAndGetCoordinates() {
        rectangle.setAnchorX(150.0);
        rectangle.setAnchorY(90.0);
        assertEquals(150.0, rectangle.getAnchorX(), 0.01);
        assertEquals(90.0, rectangle.getAnchorY(), 0.01);
    }

    /**
     * Tests that the default dimensions (width and height)
     * of the rectangle are as expected.
     */
    @Test
    public void testDefaultDimensions() {
        assertEquals(60.0, rectangle.getDimensionX(), 0.01);
        assertEquals(40.0, rectangle.getDimensionY(), 0.01);
    }

    /**
     * Tests that the 'contains' method correctly detects whether a point is within the rectangle.
     */
    @Test
    public void testContainsPoint() {
        rectangle.setAnchorX(100.0);
        rectangle.setAnchorY(50.0);
        assertTrue(rectangle.contains(110.0, 60.0));
        assertFalse(rectangle.contains(200.0, 200.0));
    }

    /**
     * Tests that the selection property works and is bound to a visual effect (DropShadow).
     */
    @Test
    public void testSelectionBindingEffect() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                assertNull(rectangle.getEffect());

                rectangle.setSelected(true);

                assertNotNull(rectangle.getEffect());
                assertInstanceOf(DropShadow.class, rectangle.getEffect());

                rectangle.setSelected(false);

                assertNull(rectangle.getEffect());
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    /**
     * Tests the drag initialization and behavior of the {@link RectangleShape}.
     * <p>
     * This test performs the following steps:
     * <ul>
     *   <li>Creates and configures a {@code RectangleShape} with specific colors, position, and size.</li>
     *   <li>Initializes dragging behavior by calling {@code initDrag()}.</li>
     *   <li>Simulates a mouse press event on the rectangle to start dragging,
     *       verifying the cursor changes to {@code CLOSED_HAND}.</li>
     *   <li>Simulates a mouse drag event to move the rectangle,
     *       verifying the new position is calculated correctly with respect to the initial offset.</li>
     *   <li>Simulates a mouse release event to end dragging,
     *       verifying the cursor returns to {@code DEFAULT}.</li>
     * </ul>
     */
    @Test
    public void testInitDrag() {
        RectangleShape rectangle = new RectangleShape(Color.BLACK, Color.RED,100,120,100,100);
        rectangle.setSelected(true);
        rectangle.initDrag();
        MouseEvent pressEvent = new MouseEvent(
                MouseEvent.MOUSE_PRESSED,
                150, 150, 150, 150,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(rectangle, 150, 150)
        );
        rectangle.getOnMousePressed().handle(pressEvent);
        MouseEvent dragEvent = new MouseEvent(
                MouseEvent.MOUSE_DRAGGED,
                200, 200, 200, 200,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(rectangle, 200, 200)
        );
        rectangle.getOnMouseDragged().handle(dragEvent);
        assertEquals(Cursor.CLOSED_HAND, rectangle.getCursor());
        assertEquals(150.0, rectangle.getAnchorX(), 0.01);
        assertEquals(170.0, rectangle.getAnchorY(), 0.01);
        MouseEvent releaseEvent = new MouseEvent(
                MouseEvent.MOUSE_RELEASED,
                200, 200, 200, 200,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(rectangle, 200, 200)
        );
        rectangle.getOnMouseReleased().handle(releaseEvent);
        assertEquals(Cursor.DEFAULT, rectangle.getCursor());
    }

    /**
     * Tests the {@code getCopy} method of {@code RectangleShape}.
     * <p>
     * This test ensures that the {@code getCopy} method returns a new {@code RectangleShape}
     * instance with the same fill color, edge color, dimensions, and position as the original shape.
     * It also verifies that the copied object is a distinct instance from the original.
     */
    @Test
    public void testGetCopy() {
        RectangleShape rectangle = new RectangleShape(Color.YELLOW, Color.BLACK);
        rectangle.setDimensionX(120);
        rectangle.setDimensionY(80);
        rectangle.setAnchorX(50);
        rectangle.setAnchorY(70);
        SelectableShape copiedShape = rectangle.getCopy();
        assertNotSame(rectangle, copiedShape);
        assertInstanceOf(RectangleShape.class, copiedShape);
        RectangleShape copy = (RectangleShape) copiedShape;
        assertEquals(rectangle.getFillColor(), copy.getFillColor());
        assertEquals(rectangle.getEdgeColor(), copy.getEdgeColor());
        assertEquals(rectangle.getDimensionX(), copy.getDimensionX(), 0.001);
        assertEquals(rectangle.getDimensionY(), copy.getDimensionY(), 0.001);
        assertEquals(rectangle.getAnchorX(), copy.getAnchorX(), 0.001);
        assertEquals(rectangle.getAnchorY(), copy.getAnchorY(), 0.001);
    }


    /**
     * Tests setting and retrieving the dimensions (width and height).
     */
    @Test
    public void testSetAndGetDimensions() {
        rectangle.setDimensionX(80.0);
        rectangle.setDimensionY(40.0);
        assertEquals(80.0, rectangle.getDimensionX(), 0.01);
        assertEquals(40.0, rectangle.getDimensionY(), 0.01);
    }

    /**
     * Tests the selectedProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testSelectedPropertyBehavior() {
        assertFalse(rectangle.isSelected(), "Default selectedProperty should be false");

        rectangle.selectedProperty().set(true);
        assertTrue(rectangle.selectedProperty().get(), "selectedProperty should be true after set");

        rectangle.setSelected(false);
        assertFalse(rectangle.isSelected(), "selectedProperty should reflect setSelected(false)");
    }

    /**
     * Tests the interactionProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testInteractionPropertyBehavior() {
        assertFalse(rectangle.interactionPropertyProperty().get(), "Default interactionProperty should be false");

        rectangle.setInteractionProperty(true);
        assertTrue(rectangle.interactionPropertyProperty().get(), "interactionProperty should be true after set");

        rectangle.setInteractionProperty(false);
        assertFalse(rectangle.interactionPropertyProperty().get(), "interactionProperty should be false after reset");
    }

}