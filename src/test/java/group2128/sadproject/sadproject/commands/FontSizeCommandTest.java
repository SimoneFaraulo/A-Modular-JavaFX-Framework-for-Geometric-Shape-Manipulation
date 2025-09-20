package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.TextShape;
import group2128.sadproject.sadproject.strategy.IdleStrategy;
import group2128.sadproject.sadproject.strategy.TextDrawingStrategy;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
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
 * Integration test for verifying the behavior of the {@link FontSizeCommand} via the UI.
 * <p>
 * This test class uses TestFX to simulate user interactions in a JavaFX application
 * and ensures that resizing a text shape through the font size spinner correctly affects
 * both the font size and visual representation of the text.
 * </p>
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FontSizeCommandTest {

    private AppController controller;

    /**
     * Initializes the JavaFX stage with the main application scene before any test is run.
     *
     * @param stage the primary stage for the test environment
     * @throws Exception if loading the FXML fails
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
     * Resets the application state before each test by:
     * <ul>
     *     <li>Setting default text in the text input field</li>
     *     <li>Triggering the creation of a new drawing pane</li>
     * </ul>
     *
     * @param robot the {@link FxRobot} used to simulate UI interaction
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
     * Verifies that resizing a {@link TextShape} through the font size spinner
     * updates its font size as expected.
     * <p>
     * The test performs the following steps:
     * <ol>
     *     <li>Sets the font size to an initial value and draws the text shape</li>
     *     <li>Selects the shape after drawing</li>
     *     <li>Changes the font size via the spinner</li>
     *     <li>Asserts that the shape's font size is updated accordingly</li>
     * </ol>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testTextShapeFontIsResized(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> spinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);

        robot.interact(() -> {
            textField.setText("testTextShapeFontIsResized");
            spinner.getValueFactory().setValue(20.0);
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

        robot.clickOn("#textBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(text);
        assertThat(text.isSelected()).isTrue();

        robot.interact(() -> {
            spinner.getValueFactory().setValue(40.0);
        });

        assertThat(40.0).isEqualTo(text.getFontSize());
    }
}