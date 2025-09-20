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
 * Integration tests for stretching polygon shapes in the JavaFX application.
 * <p>
 * These tests simulate user interactions to verify that polygons can be drawn
 * and stretched correctly using the application's stretch command.
 * </p>
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StretchPolygonCommandTest {

    private AppController controller;

    /**
     * Sets up the JavaFX application stage before all tests.
     * Loads the FXML view and initializes the controller.
     *
     * @param stage the primary stage for the application.
     * @throws Exception if the FXML file cannot be loaded.
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
     * Resets the application state before each test by creating a new drawing pane.
     *
     * @param robot the TestFX robot used to perform GUI interactions.
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that a polygon drawn on the canvas can be stretched correctly.
     * Simulates the drawing of a 5-point polygon and applies the stretch command
     * to verify that a specific vertex is moved by the expected delta.
     *
     * @param robot the TestFX robot used to perform GUI interactions.
     */
    @Test
    void testPolygonShapeIsStretched(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(20,-70).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-60,-50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70,50).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape drawn = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        double origPointX = drawn.getPointsList().get(6);
        double origPointY = drawn.getPointsList().get(7);

        robot.clickOn("#polygonBtn");

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX + 60,centerY - 120).press(MouseButton.PRIMARY);
        robot.moveBy(50, -100);
        robot.release(MouseButton.PRIMARY);

        double newPointX = drawn.getPointsList().get(6);
        double newPointY = drawn.getPointsList().get(7);

        assertThat(newPointX).isEqualTo(origPointX + 50, within(0.5));
        assertThat(newPointY).isEqualTo(origPointY - 100, within(0.5));
    }

    /**
     * Tests stretching of a more complex polygon with additional vertices.
     * Verifies the anchor point is moved by the correct amount after the stretch command.
     *
     * @param robot the TestFX robot used to perform GUI interactions.
     */
    @Test
    void testPolygonShapeIsStretched2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(20,-70).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-60,-50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-60,+20).clickOn(MouseButton.PRIMARY);
        robot.moveBy(10,-20).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70,50).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape drawn = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        double origPointX = drawn.getAnchorX();
        double origPointY = drawn.getAnchorY();

        robot.clickOn("#polygonBtn");

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX,centerY).press(MouseButton.PRIMARY);
        robot.moveBy(100, -10);
        robot.release(MouseButton.PRIMARY);

        double newPointX = drawn.getAnchorX();
        double newPointY = drawn.getAnchorY();

        assertThat(newPointX).isEqualTo(origPointX + 100, within(0.5));
        assertThat(newPointY).isEqualTo(origPointY - 10, within(0.5));
    }
}
