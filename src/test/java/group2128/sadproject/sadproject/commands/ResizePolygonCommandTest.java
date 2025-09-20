package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.PolygonShape;
import group2128.sadproject.sadproject.strategy.PolygonDrawingStrategy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for verifying the behavior of the {@link ResizeRectangleCommand}.
 * This class uses TestFX to simulate user interactions with a rectangle shape
 * and checks if resizing operations along different axes and corners behave correctly,
 * preserving the aspect ratio.
 * The tests simulate drag events in various directions (X, Y, diagonal)
 * and verify changes in the shape's dimensions accordingly.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResizePolygonCommandTest {

    /**
     * Reference to the main application controller for UI interaction.
     */
    private AppController controller;

    /**
     * Initializes the JavaFX test environment with a basic canvas and a rectangle shape.
     * This method is automatically called by TestFX before each test.
     *
     * @param stage the primary stage provided by TestFX.
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
     * Resets the canvas state and shape list before each test to ensure test independence.
     * This method is automatically executed before each test method.
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that resizing a polygon shape along the X-axis increases its width,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement to the left.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testPolygonShapeIsResized(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-50,-70).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape drawn = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        double origDimX = drawn.getDimensionX();
        double origDimY = drawn.getDimensionY();

        robot.clickOn("#polygonBtn");

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX,centerY).press(MouseButton.PRIMARY);
        robot.moveBy(-100, 0);
        robot.release(MouseButton.PRIMARY);

        assertThat(drawn.getDimensionX()).isGreaterThan(origDimX);
        assertThat(drawn.getDimensionY()).isGreaterThan(origDimY);
    }


    /**
     * Tests that resizing a polygon shape along the X-axis increases its width,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement to the right.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testPolygonShapeIsResized2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-50,-70).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape drawn = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        double origDimX = drawn.getDimensionX();
        double origDimY = drawn.getDimensionY();

        robot.clickOn("#polygonBtn");

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX,centerY).press(MouseButton.PRIMARY);
        robot.moveBy(50, 0);
        robot.release(MouseButton.PRIMARY);

        assertThat(drawn.getDimensionX()).isLessThan(origDimX);
        assertThat(drawn.getDimensionY()).isLessThan(origDimY);
    }
}
