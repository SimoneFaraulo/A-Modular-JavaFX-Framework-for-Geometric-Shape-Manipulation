package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.SegmentShape;
import group2128.sadproject.sadproject.strategy.SegmentDrawingStrategy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.*;

/**
 * Integration test class for the ResizeSegmentCommand functionality.
 * <p>
 * These tests simulate user interactions using TestFX to verify that a segment
 * shape (line segment) can be correctly resized by dragging its endpoint on the canvas.
 * The tests verify that resizing updates the segment length and screen coordinates as expected.
 * </p>
 * <p>
 * The class sets up the application UI using FXMLLoader and interacts with the
 * drawing pane and segment shapes via the {@link AppController} and TestFX robot.
 * </p>
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResizeSegmentCommandTest {

    /**
     * Reference to the main application controller for UI interaction.
     */
    private AppController controller;

    /**
     * Sets up the JavaFX application stage for the test environment.
     * Loads the FXML view and obtains the controller instance.
     *
     * @param stage The primary stage provided by TestFX.
     * @throws Exception if the FXML loading fails.
     */
    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group2128/sadproject/sadproject/hello-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Resets the drawing pane state by creating a new pane before each test.
     * This ensures tests run independently without interference from previous state.
     *
     * @param robot TestFX robot used to interact with the UI thread safely.
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests resizing a segment shape by dragging its endpoint to the right.
     * Verifies that the segment length increases and the screen endpoint moves correctly along the X-axis.
     *
     * @param robot TestFX robot for simulating user input and querying UI components.
     */
    @Test
    void testSegmentShapeIsResized1(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof SegmentShape);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(segment);

        double initialLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double initialLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double initialLength = Math.sqrt(initialLengthX*initialLengthX + initialLengthY*initialLengthY);

        robot.clickOn("#segmentBtn");

        Point2D localEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D sceneEnd = segment.localToScene(localEnd);
        Point2D screenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(sceneEnd);

        double initialScreenX = screenEnd.getX();
        double initialScreenY = screenEnd.getY();

        robot.moveTo(screenEnd).clickOn(MouseButton.PRIMARY).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#resizeBtn");

        double displacement = 100;
        robot.moveTo(screenEnd)
            .press(MouseButton.PRIMARY)
            .moveBy(displacement, 0)
            .release(MouseButton.PRIMARY);

        Point2D newLocalEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D newSceneEnd = segment.localToScene(newLocalEnd);
        Point2D newScreenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(newSceneEnd);

        double finalScreenX = newScreenEnd.getX();
        double finalScreenY = newScreenEnd.getY();

        double newLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double newLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double newLength = Math.sqrt(newLengthX*newLengthX + newLengthY*newLengthY);

        assertThat(newLength).isGreaterThan(initialLength);
        assertThat(finalScreenX).isCloseTo(initialScreenX + displacement, within(1.0));
        assertThat(finalScreenY).isCloseTo(initialScreenY, within(1.0));

    }

    /**
     * Tests resizing a segment shape by dragging its endpoint diagonally to the top-left.
     * Verifies that the segment length increases and the screen endpoint moves correctly along both axes.
     *
     * @param robot TestFX robot for simulating user input and querying UI components.
     */
    @Test
    void testSegmentShapeIsResized2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(0, 50).clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof SegmentShape);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(segment);

        double initialLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double initialLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double initialLength = Math.sqrt(initialLengthX*initialLengthX + initialLengthY*initialLengthY);

        robot.clickOn("#segmentBtn");

        Point2D localEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D sceneEnd = segment.localToScene(localEnd);
        Point2D screenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(sceneEnd);

        double initialScreenX = screenEnd.getX();
        double initialScreenY = screenEnd.getY();

        robot.moveTo(screenEnd).clickOn(MouseButton.PRIMARY).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#resizeBtn");

        double displacement = 60;
        robot.moveTo(screenEnd)
                .press(MouseButton.PRIMARY)
                .moveBy(-displacement, -displacement)
                .release(MouseButton.PRIMARY);

        Point2D newLocalEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D newSceneEnd = segment.localToScene(newLocalEnd);
        Point2D newScreenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(newSceneEnd);

        double finalScreenX = newScreenEnd.getX();
        double finalScreenY = newScreenEnd.getY();

        double newLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double newLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double newLength = Math.sqrt(newLengthX*newLengthX + newLengthY*newLengthY);

        assertThat(newLength).isGreaterThan(initialLength);
        assertThat(finalScreenX).isCloseTo(initialScreenX - displacement, within(1.0));
        assertThat(finalScreenY).isCloseTo(initialScreenY - displacement, within(1.0));

    }

    /**
     * Tests resizing a segment shape by dragging its endpoint diagonally to the top-left,
     * but verifies a length decrease scenario.
     * Checks that the length is shorter after the operation and screen coordinates are updated.
     *
     * @param robot TestFX robot for simulating user input and querying UI components.
     */
    @Test
    void testSegmentShapeIsResized3(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 50).clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof SegmentShape);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(segment);

        double initialLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double initialLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double initialLength = Math.sqrt(initialLengthX*initialLengthX + initialLengthY*initialLengthY);

        robot.clickOn("#segmentBtn");

        Point2D localEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D sceneEnd = segment.localToScene(localEnd);
        Point2D screenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(sceneEnd);

        double initialScreenX = screenEnd.getX();
        double initialScreenY = screenEnd.getY();

        robot.moveTo(screenEnd).clickOn(MouseButton.PRIMARY).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#resizeBtn");

        double displacement = 60;
        robot.moveTo(screenEnd)
                .press(MouseButton.PRIMARY)
                .moveBy(-displacement, -displacement)
                .release(MouseButton.PRIMARY);

        Point2D newLocalEnd = new Point2D(segment.getEndPointX(), segment.getEndPointY());
        Point2D newSceneEnd = segment.localToScene(newLocalEnd);
        Point2D newScreenEnd = segment.getScene().getWindow().getScene().getRoot().localToScreen(newSceneEnd);

        double finalScreenX = newScreenEnd.getX();
        double finalScreenY = newScreenEnd.getY();

        double newLengthX = Math.abs(segment.getEndPointX()-segment.getAnchorX());
        double newLengthY = Math.abs(segment.getEndPointY()-segment.getAnchorY());
        double newLength = Math.sqrt(newLengthX*newLengthX + newLengthY*newLengthY);

        assertThat(newLength).isLessThan(initialLength);
        assertThat(finalScreenX).isCloseTo(initialScreenX - displacement, within(1.0));
        assertThat(finalScreenY).isCloseTo(initialScreenY - displacement, within(1.0));

    }
}
