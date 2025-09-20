package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.EllipseShape;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.factory.TextShape;
import group2128.sadproject.sadproject.strategy.*;
import javafx.event.ActionEvent;
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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for verifying the rotation functionality of drawable shapes
 * in the application, including {@link TextShape}, {@link RectangleShape}, and {@link EllipseShape}.
 * <p>
 * This class uses TestFX to simulate user interactions with the JavaFX UI,
 * ensuring that shapes correctly update their rotation angle when a new value
 * is entered in the rotation input field and applied via {@code controller.onAngleAction()}.
 * </p>
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RotateCommandTest {

    private AppController controller;

    /**
     * Initializes the JavaFX stage with the main application scene before tests are run.
     *
     * @param stage the primary stage for the test environment
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
     * Resets the application state before each test by setting default values
     * in relevant text fields and creating a new drawing pane.
     *
     * @param robot the {@link FxRobot} used for simulating UI interaction
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> {
            TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
            textField.setText("Hello World");
            controller.createNewPane(null);
        });
    }

    /**
     * Verifies that a {@link TextShape} is correctly rotated when a new angle is specified.
     * <p>
     * The test performs the following steps:
     * <ol>
     *     <li>Draws a text shape with an initial rotation</li>
     *     <li>Selects the shape</li>
     *     <li>Updates the angle using the input field</li>
     *     <li>Triggers the rotation command</li>
     *     <li>Asserts that the shape's rotation is updated</li>
     * </ol>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testTextShapeFontIsRotated(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        TextField angleField = robot.lookup("#angleTxt").queryAs(TextField.class);

        robot.interact(() -> {
            textField.setText("testTextShapeFontIsResized");
            angleField.setText("10");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof TextShape);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);
        assertThat(text).isNotNull();
        assertThat(10.0).isEqualTo(text.getRotation());

        robot.clickOn("#textBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(text);
        assertThat(text.isSelected()).isTrue();

        robot.interact(() -> {
            angleField.setText("30");
            controller.onAngleAction(new ActionEvent());
        });

        assertThat(30.0).isEqualTo(text.getRotation());
    }

    /**
     * Verifies that a {@link RectangleShape} is correctly rotated when a new angle is specified.
     * <p>
     * This test follows similar steps as for the text shape but uses the rectangle drawing strategy.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testRectangleShapeFontIsRotated(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField angleField = robot.lookup("#angleTxt").queryAs(TextField.class);

        robot.interact(() -> {
            angleField.setText("10");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape rect = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(rect).isNotNull();
        assertThat(10.0).isEqualTo(rect.getRotation());

        robot.clickOn("#rectangleBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(rect);
        assertThat(rect.isSelected()).isTrue();

        robot.interact(() -> {
            angleField.setText("40");
            controller.onAngleAction(new ActionEvent());
        });

        assertThat(40.0).isEqualTo(rect.getRotation());
    }

    /**
     * Verifies that an {@link EllipseShape} is correctly rotated when a new angle is provided.
     * <p>
     * This test confirms correct behavior for the ellipse drawing strategy.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testEllipseShapeFontIsRotated(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField angleField = robot.lookup("#angleTxt").queryAs(TextField.class);
        TextField widthTxt = robot.lookup("#widthTxt").queryAs(TextField.class);

        robot.interact(() -> {
            widthTxt.setText("200");
            angleField.setText("20");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape ellipse = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(ellipse).isNotNull();
        assertThat(20.0).isEqualTo(ellipse.getRotation());

        robot.clickOn("#ellipseBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(ellipse);
        assertThat(ellipse.isSelected()).isTrue();

        robot.interact(() -> {
            angleField.setText("40");
            controller.onAngleAction(new ActionEvent());
        });

        assertThat(40.0).isEqualTo(ellipse.getRotation());
    }
}