package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.EllipseShape;
import group2128.sadproject.sadproject.strategy.EllipseDrawingStrategy;
import group2128.sadproject.sadproject.strategy.IdleStrategy;
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
 * Integration tests for verifying the behavior of ellipse resizing within the drawing pane.
 * These tests simulate user interactions for creating and resizing ellipses using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResizeEllipseCommandTest {

    /**
     * Reference to the main application controller for UI interaction.
     */
    private AppController controller;

    /**
     * Initializes and launches the JavaFX application before all tests.
     *
     * @param stage the primary stage provided by TestFX
     * @throws Exception if the FXML file cannot be loaded
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
     * Resets the state before each test by creating a new drawing pane.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests horizontal resizing (increase in X direction) of an ellipse.
     */
    @Test
    void testEllipseShapeIsResizedX(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

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
        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderPointEllipse = new Point2D(centerX + initialRadiusX, centerY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;


        double expectedScaleFactor = (initialRadiusX + displacement) / initialRadiusX;

        assertThat(newRadiusX).isGreaterThan(initialRadiusX);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusX).isCloseTo(initialRadiusX * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests horizontal resizing (decrease in X direction) of an ellipse.
     */
    @Test
    void testEllipseShapeIsResizedX2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 150;
        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderPointEllipse = new Point2D(centerX + initialRadiusX, centerY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;


        double expectedScaleFactor = Math.abs(initialRadiusX - displacement) / initialRadiusX;

        assertThat(newRadiusX).isLessThan(initialRadiusX);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusX).isCloseTo(initialRadiusX * expectedScaleFactor, within(1.0));
    }


    /**
     * Tests vertical resizing (increase in Y direction) of an ellipse.
     */
    @Test
    void testEllipseShapeIsResizedY(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

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
        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderPointEllipse = new Point2D(centerX, centerY - initialRadiusY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(0, -displacement);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;


        double expectedScaleFactor = (initialRadiusY + displacement) / initialRadiusY;

        assertThat(newRadiusY).isGreaterThan(initialRadiusY);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusY).isCloseTo(initialRadiusY * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests vertical resizing (decrease in Y direction) of an ellipse.
     */
    @Test
    void testEllipseShapeIsResizedY2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

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
        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderPointEllipse = new Point2D(centerX, centerY - initialRadiusY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(0, displacement);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;


        double expectedScaleFactor = Math.abs(initialRadiusY - displacement) / initialRadiusY;

        assertThat(newRadiusY).isLessThan(initialRadiusY);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusX).isCloseTo(initialRadiusX * expectedScaleFactor, within(1.0));
    }

    /**
     * Tests diagonal resizing (increase in both X and Y) of an ellipse while preserving aspect ratio.
     */
    @Test
    void testEllipseShapeIsResizedDiagonal(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double angleRad = Math.toRadians(45);
        double offsetX = initialRadiusX * Math.cos(angleRad);
        double offsetY = initialRadiusY * Math.sin(angleRad);
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderDiagonalLocal = new Point2D(centerX + offsetX, centerY + offsetY);


        double move = 150;
        double moveX = move * Math.cos(angleRad);
        double moveY = move * Math.sin(angleRad);

        robot.moveTo(borderDiagonalLocal)
                .press(MouseButton.PRIMARY)
                .moveBy(moveX, moveY)
                .release(MouseButton.PRIMARY);


        double deltaY = offsetY + moveY;
        double deltaX = deltaY/initialRatio;

        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;

        assertThat(newRadiusX).isGreaterThan(initialRadiusX);
        assertThat(newRadiusY).isGreaterThan(initialRadiusY);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusX).isCloseTo(deltaX, within(1.0));
        assertThat(newRadiusY).isCloseTo(deltaY, within(1.0));
    }


    /**
     * Tests diagonal resizing (decrease in both X and Y) of an ellipse while preserving aspect ratio.
     */
    @Test
    void testEllipseShapeIsResizedDiagonal2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double angleRad = Math.toRadians(45);
        double offsetX = initialRadiusX * Math.cos(angleRad);
        double offsetY = initialRadiusY * Math.sin(angleRad);
        double initialRatio = initialRadiusY / initialRadiusX;

        Point2D borderDiagonalLocal = new Point2D(centerX + offsetX, centerY + offsetY);


        double move = 70;
        double moveX = move * Math.cos(angleRad);
        double moveY = move * Math.sin(angleRad);

        robot.moveTo(borderDiagonalLocal)
                .press(MouseButton.PRIMARY)
                .moveBy(-moveX, -moveY)
                .release(MouseButton.PRIMARY);


        double localX = centerX + offsetX - moveX;
        double localY = centerY + offsetY - moveY;

        double deltaX = Math.abs(localX - centerX);
        double deltaY = Math.abs(localY - centerY);

        if (deltaX * initialRatio > deltaY) {
            deltaY = deltaX * initialRatio;
        } else {
            deltaX = deltaY / initialRatio;
        }

        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();
        double newRatio = newRadiusY / newRadiusX;

        assertThat(newRadiusX).isLessThan(initialRadiusX);
        assertThat(newRadiusY).isLessThan(initialRadiusY);
        assertThat(newRatio).isCloseTo(initialRatio, within(0.01));
        assertThat(newRadiusX).isCloseTo(deltaX, within(1.5));
        assertThat(newRadiusY).isCloseTo(deltaY, within(1.5));
    }
}
