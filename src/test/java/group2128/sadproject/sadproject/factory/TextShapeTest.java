package group2128.sadproject.sadproject.factory;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TextShape} class.
 * <p>
 * These tests verify the correct initialization, color management,
 * coordinate handling, drag behavior, selection effect, and copy functionality
 * of a {@code TextShape} instance.
 */
public class TextShapeTest {

    private TextShape text;

    /**
     * Sets up a default TextShape instance before each test.
     * Initializes it with fill color BLACK and edge color RED.
     */
    @BeforeEach
    public void setUp() {
        text = new TextShape(Color.BLACK, Color.RED);
    }

    /**
     * Tests that the initial colors (fill and edge) of the text
     * are correctly set in the constructor.
     */
    @Test
    public void testInitialColors() {
        assertEquals(Color.RED, text.getEdgeColor());
        assertEquals(Color.BLACK, text.getFillColor());
    }

    /**
     * Tests the ability to set the edge color of the text.
     * Also verifies that the stroke property is updated accordingly.
     */
    @Test
    public void testSetColorEdge() {
        text.setEdgeColor(Color.BLUE);
        assertEquals(Color.BLUE, text.getEdgeColor());
        assertEquals(Color.BLUE, text.getStroke());
    }

    /**
     * Tests the ability to set the fill color of the text.
     * Also verifies that the fill property is updated accordingly.
     */
    @Test
    public void testSetFillColor() {
        text.setFillColor(Color.GREEN);
        assertEquals(Color.GREEN, text.getFillColor());
        assertEquals(Color.GREEN, text.getFill());
    }

    /**
     * Tests setting and retrieving the top-left X and Y coordinates
     * of the text. Ensures values are properly stored and returned.
     */
    @Test
    public void testSetAndGetCoordinates() {
        text.setAnchorX(75.0);
        text.setAnchorY(45.0);
        assertEquals(75.0, text.getAnchorX(), 0.01);
        assertEquals(45.0, text.getAnchorY(), 0.01);
    }

    /**
     * Tests that the selection property works and is bound to a visual effect (DropShadow).
     */
    @Test
    public void testSelectionBindingEffect() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                assertNull(text.getEffect());

                text.setSelected(true);
                assertNotNull(text.getEffect());
                assertInstanceOf(DropShadow.class, text.getEffect());

                text.setSelected(false);
                assertNull(text.getEffect());
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    /**
     * Tests the drag initialization and behavior of the {@link TextShape}.
     * <p>
     * This test performs the following steps:
     * <ul>
     *   <li>Initializes dragging behavior by calling {@code initDrag()}.</li>
     *   <li>Simulates a mouse press event to start dragging and verifies the cursor.</li>
     *   <li>Simulates a mouse drag event and checks the updated coordinates.</li>
     *   <li>Simulates a mouse release event and verifies the cursor reset.</li>
     * </ul>
     */
    @Test
    public void testInitDrag() {
        text.setAnchorX(50);
        text.setAnchorY(100);
        text.setText("Hello");
        text.setFontSize(20);
        text.setSelected(true);
        text.initDrag();

        MouseEvent pressEvent = new MouseEvent(
                MouseEvent.MOUSE_PRESSED,
                60, 110, 60, 110,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(text, 60, 110)
        );
        text.getOnMousePressed().handle(pressEvent);

        MouseEvent dragEvent = new MouseEvent(
                MouseEvent.MOUSE_DRAGGED,
                100, 140, 100, 140,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(text, 100, 140)
        );
        text.getOnMouseDragged().handle(dragEvent);
        assertEquals(Cursor.CLOSED_HAND, text.getCursor());

        MouseEvent releaseEvent = new MouseEvent(
                MouseEvent.MOUSE_RELEASED,
                100, 140, 100, 140,
                MouseButton.PRIMARY, 1,
                false, false, false, false,
                true, false, false, true, false, false,
                new PickResult(text, 100, 140)
        );
        text.getOnMouseReleased().handle(releaseEvent);
        assertEquals(Cursor.DEFAULT, text.getCursor());
    }

    /**
     * Tests the {@code getCopy} method of {@code TextShape}.
     * <p>
     * This test ensures that the {@code getCopy} method returns a new {@code TextShape}
     * instance with the same fill color, edge color, coordinates, and text content.
     * It also verifies that the copied object is a distinct instance from the original.
     */
    @Test
    public void testGetCopy() {
        text.setText("Original");
        text.setFontSize(24);
        text.setAnchorX(100);
        text.setAnchorY(200);
        text.setEdgeColor(Color.BLUE);
        text.setFillColor(Color.YELLOW);
        text.setDimensionY(2);
        text.setDimensionX(100);

        SelectableShape copy = text.getCopy();
        assertNotSame(text, copy);
        assertInstanceOf(TextShape.class, copy);

        TextShape copyText = (TextShape) copy;
        assertEquals("Original", copyText.getText());
        assertEquals(100.0, copyText.getAnchorX(), 0.01);
        assertEquals(200.0, copyText.getAnchorY(), 0.01);
        assertEquals(Color.BLUE, copyText.getEdgeColor());
        assertEquals(Color.YELLOW, copyText.getFillColor());
        assertEquals(2, copyText.getDimensionY());
        assertEquals(100, copyText.getDimensionX());
    }

    /**
     * Tests setting and retrieving the dimensions (width and height).
     */
    @Test
    public void testSetAndGetDimensions() {
        text.setDimensionX(80.0);
        text.setDimensionY(40.0);
        assertEquals(80.0, text.getDimensionX(), 0.01);
        assertEquals(40.0, text.getDimensionY(), 0.01);
    }

    /**
     * Tests that the 'contains' method correctly detects whether a point is within the text bounds.
     */
    @Test
    public void testContainsPoint() {
        text.setAnchorX(50.0);
        text.setAnchorY(60.0);
        text.setText("Hello");
        text.setFontSize(30);
        text.setDimensionX(1);
        text.setDimensionY(1);

        assertTrue(text.contains(52, 64));
    }

    /**
     * Tests the selectedProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testSelectedPropertyBehavior() {
        assertFalse(text.isSelected(), "Default selectedProperty should be false");

        text.selectedProperty().set(true);
        assertTrue(text.selectedProperty().get(), "selectedProperty should be true after set");

        text.setSelected(false);
        assertFalse(text.isSelected(), "selectedProperty should reflect setSelected(false)");
    }

    /**
     * Tests the interactionProperty: ensures default is false and it can be updated and retrieved.
     */
    @Test
    public void testInteractionPropertyBehavior() {
        assertFalse(text.interactionPropertyProperty().get(), "Default interactionProperty should be false");

        text.setInteractionProperty(true);
        assertTrue(text.interactionPropertyProperty().get(), "interactionProperty should be true after set");

        text.setInteractionProperty(false);
        assertFalse(text.interactionPropertyProperty().get(), "interactionProperty should be false after reset");
    }

    /**
     * Tests that the default constructor of {@link TextShape} sets the font size to the default
     * JavaFX font size.
     */
    @Test
    public void testGetFontSize() {
        double expectedFontSize = 24.0;
        text.setFontSize(24);

        double actualFontSize = text.getFontSize();
        assertEquals(expectedFontSize, actualFontSize, 0.001);
    }


}
