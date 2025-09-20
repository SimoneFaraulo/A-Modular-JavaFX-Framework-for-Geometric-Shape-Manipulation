package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.RectangleShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for the {@link RectangleDrawingStrategy} class.
 * <p>
 * These tests simulate user interactions using the TestFX framework to verify that
 * rectangles are properly created and added to the canvas when the strategy is activated.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RectangleDrawingStrategyTest {

    private AppController controller;

    /**
     * Initializes the JavaFX environment before running the tests.
     * Loads the main FXML view and shows the stage.
     *
     * @param stage the primary stage used by the JavaFX test framework
     * @throws Exception if the FXML resource cannot be loaded
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
     * @param robot the {@link FxRobot} used to perform UI actions
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that when the rectangle drawing strategy is activated
     * and the canvas is clicked, a {@link RectangleShape} is added to the pane.
     *
     * @param robot the {@link FxRobot} used to simulate user interaction
     */
    @Test
    void testRectangleShapeIsDrawn(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);

        assertThat(drawn).isNotNull();
    }

    /**
     * Tests that when a rectangle is drawn after selecting a stroke color,
     * the resulting shape has the correct stroke color applied.
     *
     * <p>This test simulates a user selecting fuchsia as the edge color using the
     * {@code ColorPicker}, activating the rectangle drawing tool, and clicking
     * on the drawing pane to create a rectangle. It then verifies that the
     * created {@link RectangleShape} has the correct stroke color.</p>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testRectangleEdgeColor(FxRobot robot) {
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(Color.FUCHSIA);
            robot.sleep(500);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape rectangle = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(rectangle).isNotNull();
        assertThat(rectangle.getEdgeColor()).isEqualTo(Color.FUCHSIA);
        robot.sleep(500);
    }


    /**
     * Tests that when a rectangle is drawn after selecting a fill color,
     * the resulting shape has the correct fill color applied.
     *
     * <p>This test simulates a user selecting black as the fill color using the
     * {@code ColorPicker}, activating the rectangle drawing tool, and clicking
     * on the drawing pane to create a rectangle. It then verifies that the
     * created {@link RectangleShape} has the correct fill color.</p>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testRectangleFillColor(FxRobot robot) {
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.BLACK);
            robot.sleep(500);
            controller.onFillColorPicker(new ActionEvent());
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape rectangle = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(rectangle).isNotNull();
        assertThat(rectangle.getFillColor()).isEqualTo(Color.BLACK);
        robot.sleep(500);
    }

    /**
     * Tests that when width and height values are set in their respective text fields,
     * the drawn rectangle has the expected width and height.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testRectangleWidthAndHeight(FxRobot robot) {
        TextField widthField = robot.lookup("#widthTxt").queryAs(javafx.scene.control.TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(javafx.scene.control.TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            widthField.setText("120");
            heightField.setText("80");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape rectangle = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(rectangle).isNotNull();
        assertThat(rectangle.getDimensionX()).isEqualTo(120.0);
        assertThat(rectangle.getDimensionY()).isEqualTo(80.0);
    }



}