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
 * Test class for verifying the behavior of the {@link ResizeRectangleCommand}.
 * This class uses TestFX to simulate user interactions with a rectangle shape
 * and checks if resizing operations along different axes and corners behave correctly,
 * preserving the aspect ratio.
 * The tests simulate drag events in various directions (X, Y, diagonal)
 * and verify changes in the shape's dimensions accordingly.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResizeRectangleCommandTest {

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
     * Tests that resizing a rectangle shape along the X-axis increases its width,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement to the right.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedX(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;


        double expectedScaleFactor = ((initialWidth/2 + displacement)*2) / initialWidth;

        assertThat(newWidth).isGreaterThan(initialWidth);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newWidth).isCloseTo(initialWidth * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests that resizing a rectangle shape along the X-axis decreases its width,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement to the left.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedX2(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 98;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;


        double expectedScaleFactor = Math.abs((initialWidth/2 - displacement)*2) / initialWidth;

        assertThat(newWidth).isLessThan(initialWidth);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newWidth).isCloseTo(initialWidth * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests that resizing a rectangle shape along the Y-axis increases its height,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement downward.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedY(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX, centerY + initialHeight/2);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(0, displacement);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;


        double expectedScaleFactor = ((initialHeight/2 + displacement)*2) / initialHeight;

        assertThat(newHeight).isGreaterThan(initialHeight);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newHeight).isCloseTo(initialHeight * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests that resizing a rectangle shape along the Y-axis decreases its height,
     * preserves the aspect ratio, and updates dimensions accordingly.
     * Simulates a drag movement upward.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedY2(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 30;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX, centerY+initialHeight/2);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(0, -displacement);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;


        double expectedScaleFactor = Math.abs((initialHeight/2 - displacement)*2) / initialHeight;

        assertThat(newHeight).isLessThan(initialHeight);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newHeight).isCloseTo(initialHeight * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests that resizing a rectangle shape diagonally by dragging the bottom-right corner
     * increases both width and height while maintaining the aspect ratio.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedDiagonal(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX+initialWidth/2, centerY+initialHeight/2);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, displacement);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;

        double expectedWidth = (Math.abs((initialHeight/2 + displacement)) / newRatio)*2;

        double expectedHeight = Math.abs((initialHeight/2 + displacement)*2);

        assertThat(newHeight).isGreaterThan(initialHeight);
        assertThat(newWidth).isGreaterThan(initialWidth);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newHeight).isCloseTo(expectedHeight, within(1.0));
        assertThat(newWidth).isCloseTo(expectedWidth, within(1.0));
    }

    /**
     * Tests that resizing a rectangle shape diagonally by dragging the bottom-right corner
     * in the opposite direction increases both width and height while maintaining the aspect ratio.
     *
     * @param robot TestFX robot used to simulate user interaction.
     */
    @Test
    void testRectangleShapeIsResizedDiagonal2(FxRobot robot) {
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
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 70;
        double initialWidth = drawn.getDimensionX();
        double initialHeight = drawn.getDimensionY();
        double initialRatio = initialHeight/initialWidth;

        Point2D borderPointRectangle = new Point2D(centerX+initialWidth/2, centerY+initialHeight/2);


        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, -displacement);
        robot.release(MouseButton.PRIMARY);


        double newWidth = drawn.getDimensionX();
        double newHeight = drawn.getDimensionY();
        double newRatio = newHeight / newWidth;

        double expectedWidth = (Math.abs((initialHeight/2 - displacement)) / newRatio)*2;

        double expectedHeight = Math.abs((initialHeight/2 - displacement)*2);

        assertThat(newHeight).isGreaterThan(initialHeight);
        assertThat(newWidth).isGreaterThan(initialWidth);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newHeight).isCloseTo(expectedHeight, within(1.0));
        assertThat(newWidth).isCloseTo(expectedWidth, within(1.0));
    }

}
