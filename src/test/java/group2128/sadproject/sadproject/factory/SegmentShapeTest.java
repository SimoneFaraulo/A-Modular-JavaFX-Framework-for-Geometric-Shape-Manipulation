package group2128.sadproject.sadproject.factory;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link SegmentShape}.
 * <p>
 * This class tests the getter and setter methods of the {@code SegmentShape}
 * including edge color and coordinate handling.
 * </p>
 */
public class SegmentShapeTest {

    private SegmentShape segment;

    /**
     * Initializes a new {@code SegmentShape} instance before each test.
     */
    @BeforeEach
    public void setUp() {
        segment = new SegmentShape(null,Color.BLACK);
    }

    /**
     * Tests that the initial edge color is correctly set through the constructor.
     */
    @Test
    public void testInitialEdgeColor() {
        assertEquals(Color.BLACK, segment.getEdgeColor());
        assertEquals(Color.BLACK, segment.getStroke());
    }

    /**
     * Tests that setting the edge color updates both internal state and stroke color.
     */
    @Test
    public void testSetEdgeColor() {
        segment.setEdgeColor(Color.RED);
        assertEquals(Color.RED, segment.getEdgeColor());
        assertEquals(Color.RED, segment.getStroke());
    }

    /**
     * Tests the setting and retrieval of the segment's start coordinates (X, Y).
     */
    @Test
    public void testSetAndGetStartCoordinates() {
        segment.setAnchorX(10.0);
        segment.setAnchorY(20.0);

        assertEquals(10.0, segment.getAnchorX(), 0.01);
        assertEquals(20.0, segment.getAnchorY(), 0.01);
    }

    /**
     * Tests the setting and retrieval of the segment's end coordinates (X, Y).
     */
    @Test
    public void testSetAndGetEndCoordinates() {
        segment.setEndPointX(100.0);
        segment.setEndPointY(200.0);

        assertEquals(100.0, segment.getEndPointX(), 0.01);
        assertEquals(200.0, segment.getEndPointY(), 0.01);
    }

    /**
     * Tests whether the {@code contains} method of the {@link SegmentShape} correctly identifies
     * whether a point lies close enough to be considered on the segment.
     * <p>
     * The test sets the start and end points of the segment and checks:
     * <ul>
     *   <li>A point on or near the line should return {@code true}.</li>
     *   <li>A point far from the segment should return {@code false}.</li>
     * </ul>
     */
    @Test
    public void testContainsPoint() {
        segment.setAnchorX(50.0);
        segment.setAnchorY(50.0);
        segment.setEndPointX(150.0);
        segment.setEndPointY(150.0);

        assertTrue(segment.contains(100.0, 100.0));

        assertFalse(segment.contains(10.0, 200.0));
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
                assertNull(segment.getEffect());

                segment.setSelected(true);
                assertNotNull(segment.getEffect());
                assertInstanceOf(DropShadow.class, segment.getEffect());

                segment.setSelected(false);
                assertNull(segment.getEffect());
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    /**
     * Unit test for the {@code initDrag} method of the {@code SegmentShape} class.
     * <p>
     * This test verifies the behavior of a segment shape when a drag is initialized:
     * <ul>
     *     <li>It checks if the cursor is changed to {@code Cursor.CLOSED_HAND} on mouse press.</li>
     *     <li>It verifies that the computed offsets between start and end points are correct.</li>
     * </ul>
     *
     * Preconditions:
     * <ul>
     *     <li>The segment is initialized with a start point at (100, 120) and an end point at (200, 220).</li>
     *     <li>The segment is marked as selected.</li>
     * </ul>
     *
     * The simulated mouse event is a primary button press at position (150, 150).
     */
    @Test
    public void testInitDrag() {
        SegmentShape segment = new SegmentShape(null,Color.BLACK);
        segment.setSelected(true);
        segment.setAnchorX(100);
        segment.setAnchorY(120);
        segment.setEndPointX(200);
        segment.setEndPointY(220);

        segment.initDrag();

        MouseEvent pressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                150, 160, 150, 160,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                false, false, false, false, false, false,
                null);
        segment.getOnMousePressed().handle(pressEvent);

        assertEquals(-100.0, segment.getAnchorX() - segment.getEndPointX(), 0.01);
        assertEquals(-100.0, segment.getAnchorY() - segment.getEndPointY(), 0.01);

        MouseEvent dragEvent = new MouseEvent(MouseEvent.MOUSE_DRAGGED,
                180, 190, 180, 190,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                false, false, false, false, false, false,
                null);
        segment.getOnMouseDragged().handle(dragEvent);

        assertEquals(Cursor.CLOSED_HAND, segment.getCursor());

        assertEquals(130.0, segment.getAnchorX(), 0.01);
        assertEquals(150.0, segment.getAnchorY(), 0.01);

        assertEquals(230.0, segment.getEndPointX(), 0.01);
        assertEquals(250.0, segment.getEndPointY(), 0.01);

        MouseEvent releaseEvent = new MouseEvent(MouseEvent.MOUSE_RELEASED,
                180, 190, 180, 190,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                false, false, false, false, false, false,
                null);
        segment.getOnMouseReleased().handle(releaseEvent);
        assertEquals(Cursor.DEFAULT, segment.getCursor());
    }

    /**
     * Tests the {@code getCopy} method of {@code SegmentShape}.
     * <p>
     * This test verifies that the method returns a new {@code SegmentShape} instance
     * that is a copy of the original, with identical edge color and start/end coordinates.
     * It also asserts that the copied shape is a different object from the original.
     */
    @Test
    public void testGetCopy() {
        SegmentShape segment = new SegmentShape(null,Color.GREEN);
        segment.setAnchorX(10);
        segment.setAnchorY(20);
        segment.setEndPointX(30);
        segment.setEndPointY(40);
        SegmentShape copiedShape = segment.getCopy();
        assertNotSame(segment, copiedShape);
        assertEquals(segment.getEdgeColor(), copiedShape.getEdgeColor());
        assertEquals(segment.getStartX(), copiedShape.getAnchorX(), 0.001);
        assertEquals(segment.getStartY(), copiedShape.getAnchorY(), 0.001);
        assertEquals(segment.getEndX(), copiedShape.getEndPointX(), 0.001);
        assertEquals(segment.getEndY(), copiedShape.getEndPointY(), 0.001);
    }


    /**
     * Tests setting and retrieving the dimensions (width and height).
     */

    /**
     * Tests the selectedProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testSelectedPropertyBehavior() {
        assertFalse(segment.isSelected(), "Default selectedProperty should be false");

        segment.selectedProperty().set(true);
        assertTrue(segment.selectedProperty().get(), "selectedProperty should be true after set");

        segment.setSelected(false);
        assertFalse(segment.isSelected(), "selectedProperty should reflect setSelected(false)");
    }

    /**
     * Tests the interactionProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testInteractionPropertyBehavior() {
        assertFalse(segment.interactionPropertyProperty().get(), "Default interactionProperty should be false");

        segment.setInteractionProperty(true);
        assertTrue(segment.interactionPropertyProperty().get(), "interactionProperty should be true after set");

        segment.setInteractionProperty(false);
        assertFalse(segment.interactionPropertyProperty().get(), "interactionProperty should be false after reset");
    }

}
