package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.strategy.IdleStrategy;
import group2128.sadproject.sadproject.strategy.RectangleDrawingStrategy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
 * Integration test class for the {@link StretchRectangleCommand} in a JavaFX application.
 * <p>
 * This test suite verifies the correct behavior of rectangle stretching operations
 * along the X axis, Y axis, and diagonally. It uses TestFX to simulate real user interactions
 * on the canvas, such as selecting a shape and dragging it to stretch.
 * <p>
 * Each test ensures that stretching actions modify the rectangle’s width and/or height
 * as expected while maintaining the intended proportions when necessary.
 * The setup process creates a rectangle at the center of the canvas with predefined dimensions,
 * which is then manipulated through simulated drag gestures.
 * <p>
 * These tests validate both increase and decrease of dimensions in all directions,
 * ensuring robustness of the stretching logic in the UI.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StretchRectangleCommandTest {

    /**
     * Reference to the main application controller for UI interaction.
     */
    private AppController controller;

    /**
     * Initializes the JavaFX test environment with a basic canvas and a rectangle shape.
     * Automatically called by TestFX before each test starts.
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
     * Resets the drawing pane and clears shapes before each test to ensure a clean state.
     * Called before each test method.
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests stretching a rectangle to the right (along the X-axis),
     * checking that the width increases and the height is updated proportionally.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchedX(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("50");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newWidth).isEqualTo(initialWidth+displacement, within(0.5));
        assertThat(newHeight).isEqualTo(initialHeight/2, within(0.5));
    }

    /**
     * Tests stretching a rectangle to the left (along the X-axis),
     * checking that the width decreases while maintaining correct height.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchedX2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
            heightField.setText("200");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 50;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY + initialHeight/2);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);

        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newWidth).isEqualTo(Math.abs(initialWidth-displacement), within(0.5));
        assertThat(newHeight).isEqualTo(initialHeight, within(0.5));
    }

    /**
     * Tests stretching a rectangle downward (along the Y-axis),
     * verifying that height increases while the width remains unchanged.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchedY(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("150");
            heightField.setText("150");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY + initialHeight/2);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(0, displacement);
        robot.release(MouseButton.PRIMARY);

        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newHeight).isEqualTo((initialHeight + displacement), within(0.5));
        assertThat(newWidth).isEqualTo(initialWidth, within(0.5));
    }

    /**
     * Tests stretching a rectangle upward (along the Y-axis),
     * verifying that the height decreases and width remains as expected.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchedY2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("50");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 30;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX, centerY + initialHeight / 2);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(0, -displacement);
        robot.release(MouseButton.PRIMARY);

        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newHeight).isEqualTo(Math.abs(initialHeight - displacement), within(0.5));
        assertThat(newWidth).isEqualTo(Math.abs(initialWidth/2), within(0.5));
    }

    /**
     * Tests diagonal stretching from the bottom-right corner to increase both width and height.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchDiagonal(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("137");
            heightField.setText("70");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX+initialWidth/2, centerY+initialHeight/2);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, displacement);
        robot.release(MouseButton.PRIMARY);

        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newHeight).isEqualTo(Math.abs(initialHeight + displacement), within(1.0));
        assertThat(newWidth).isEqualTo(Math.abs(initialWidth + displacement), within(1.0));
    }

    /**
     * Tests diagonal stretching from the bottom in the opposite direction,
     * verifying that both dimensions shrink proportionally.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsStretchDiagonal2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
            heightField.setText("250");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();


        Point2D borderPointRectangle = new Point2D(centerX, centerY+initialHeight/2);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, -displacement);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();

        assertThat(newWidth).isEqualTo((initialWidth/2 + displacement), within(0.5));
        assertThat(newHeight).isEqualTo(Math.abs(initialHeight - displacement), within(0.5));

    }

}
