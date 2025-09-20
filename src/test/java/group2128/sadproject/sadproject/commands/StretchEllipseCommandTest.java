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
 * Integration tests for verifying the behavior of the stretch command applied to ellipse shapes
 * in the application. These tests simulate various user interactions such as mouse clicks and
 * drags to validate the stretching (resizing) functionality in different directions (X, Y, diagonal).
 *
 * <p>Each test case:
 * <ul>
 *   <li>Draws an ellipse on the drawing pane</li>
 *   <li>Selects the ellipse</li>
 *   <li>Applies the stretch operation via simulated mouse drag</li>
 *   <li>Asserts that the resulting shape dimensions have changed as expected</li>
 * </ul>
 *
 * <p>This class uses TestFX for UI simulation and AssertJ for assertions.
 *
 * @see EllipseShape
 * @see AppController
 * @see EllipseDrawingStrategy
 * @see IdleStrategy
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StretchEllipseCommandTest {


    private AppController controller;

    /**
     * Sets up the JavaFX application scene using the FXML layout. This method is run once before all tests.
     *
     * @param stage the primary JavaFX stage provided by TestFX
     * @throws Exception if FXML loading fails
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
     * Resets the application state before each test by creating a new drawing pane through the controller.
     *
     * @param robot the FxRobot used to interact with UI elements
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests horizontal stretching of an ellipse by increasing its radius along the X-axis.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedX(FxRobot robot) {
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialRadiusX = drawn.getDimensionX();

        Point2D borderPointEllipse = new Point2D(centerX + initialRadiusX, centerY);

        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusY).isCloseTo(0, within(0.5));
        assertThat(newRadiusX).isEqualTo(initialRadiusX + displacement, within(0.5));
    }

    /**
     * Tests horizontal shrinking of an ellipse by decreasing its radius along the X-axis.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedX2(FxRobot robot) {
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 150;
        double initialRadiusX = drawn.getDimensionX();

        Point2D borderPointEllipse = new Point2D(centerX + initialRadiusX, centerY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusX).isEqualTo(Math.abs(initialRadiusX-displacement), within(0.5));
        assertThat(newRadiusY).isCloseTo(0, within(0.01));
    }


    /**
     * Tests vertical stretching of an ellipse by increasing its radius along the Y-axis.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedY(FxRobot robot) {
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();

        Point2D borderPointEllipse = new Point2D(centerX, centerY - initialRadiusY);


        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(0, -displacement);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusY).isEqualTo(initialRadiusY + displacement, within(0.1));
        assertThat(newRadiusX).isCloseTo(0, within(0.01));
    }

    /**
     * Tests vertical shrinking of an ellipse by decreasing its radius along the Y-axis.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedY2(FxRobot robot) {
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialRadiusY = drawn.getDimensionY();

        Point2D borderPointEllipse = new Point2D(centerX, centerY - initialRadiusY);

        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(0, displacement);
        robot.release(MouseButton.PRIMARY);


        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusY).isEqualTo(Math.abs(initialRadiusY-displacement), within(0.1));
        assertThat(newRadiusX).isCloseTo(0, within(0.01));
    }

    /**
     * Tests diagonal stretching of an ellipse by increasing both X and Y radii at a 75° angle.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedDiagonal(FxRobot robot) {
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialRadiusX = drawn.getDimensionX();
        double initialRadiusY = drawn.getDimensionY();
        double angleRad = Math.toRadians(75);
        double offsetX = initialRadiusX * Math.cos(angleRad);
        double offsetY = initialRadiusY * Math.sin(angleRad);

        Point2D borderDiagonalLocal = new Point2D(centerX + offsetX, centerY + offsetY);


        double move = 150;
        double moveX = move * Math.cos(angleRad);
        double moveY = move * Math.sin(angleRad);

        robot.moveTo(borderDiagonalLocal)
                .press(MouseButton.PRIMARY)
                .moveBy(moveX, moveY)
                .release(MouseButton.PRIMARY);

        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusX).isEqualTo(Math.abs(moveX + offsetX), within(1.52));
        assertThat(newRadiusY).isEqualTo(Math.abs(moveY + offsetY), within(1.52));
    }


    /**
     * Tests diagonal shrinking of an ellipse by decreasing both X and Y radii at a 120° angle.
     * This test simulates a resize operation preserving the aspect ratio using the resize command.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @Test
    void testEllipseShapeIsStretchedDiagonal2(FxRobot robot) {
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
        double angleRad = Math.toRadians(120);
        double offsetX = initialRadiusX * Math.cos(angleRad);
        double offsetY = initialRadiusY * Math.sin(angleRad);

        Point2D borderDiagonalLocal = new Point2D(centerX + offsetX, centerY + offsetY);


        double move = 70;
        double moveX = move * Math.cos(angleRad);
        double moveY = move * Math.sin(angleRad);

        robot.moveTo(borderDiagonalLocal)
                .press(MouseButton.PRIMARY)
                .moveBy(-moveX, -moveY)
                .release(MouseButton.PRIMARY);

        double newRadiusX = drawn.getDimensionX();
        double newRadiusY = drawn.getDimensionY();

        assertThat(newRadiusX).isEqualTo(Math.abs(offsetX - moveX), within(1.52));
        assertThat(newRadiusY).isEqualTo(Math.abs(offsetY - moveY), within(1.52));
    }
}
