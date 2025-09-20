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
 * Integration test class for verifying vertical flipping behavior
 * of {@link RectangleShape} instances within the drawing pane.
 * <p>
 * This test uses TestFX to simulate realistic user interactions such as drawing,
 * selecting, and flipping shapes.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlipVerticalCommandTest {

    /**
     * Controller instance for interacting with the application UI.
     */
    private AppController controller;

    /**
     * Initializes and launches the JavaFX application environment before any test methods are run.
     *
     * @param stage the primary stage provided by the TestFX framework
     * @throws Exception if the FXML resource fails to load
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
     * Resets the drawing pane to a clean state before each test.
     *
     * @param robot the FxRobot used to simulate user interactions
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that a {@link RectangleShape} is flipped vertically when
     * the "Flip Vertical" button is activated from the context menu.
     * <p>
     * The test simulates creating a rectangle, selecting it, and triggering the vertical flip.
     *
     * @param robot the FxRobot instance used to simulate UI interactions
     */
    @Test
    void testRectangleShapeIsFlipped(FxRobot robot) {
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
        robot.moveTo("#flipVerticalBtn").clickOn(MouseButton.PRIMARY);

        assertThat(drawn.getScaleY()).isEqualTo(-1.0);
    }
}
