package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.TextShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
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
 * Integration tests for the Text drawing strategy.
 * These tests simulate user interactions using TestFX to verify that
 * text shapes are properly created and configured.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TextDrawingStrategyTest {

    private AppController controller;

    /**
     * Initializes and shows the JavaFX stage before any test runs.
     * Loads the main FXML file and obtains a reference to the application controller.
     *
     * @param stage the primary stage for the application
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
     * Resets the state of the drawing pane before each test by simulating the creation
     * of a new pane through the controller.
     *
     * @param robot the TestFX robot used to interact with the UI
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Verifies that clicking on the drawing pane does not create a text shape
     * if the drawing strategy is not properly set (i.e., remains in the idle state).
     *
     * @param robot the TestFX robot used to simulate user interaction
     */
    @Test
    void testTextShapeIsNotDrawn(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            robot.clickOn("#textBtn");
        });

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);

        assertThat(text).isNull();
    }

    /**
     * Verifies that a text shape created via user interaction contains the correct
     * text content and font size as specified through the UI controls.
     *
     * @param robot the TestFX robot used to simulate user interaction
     */
    @Test
    void testTextContentAndFontSize(FxRobot robot) {
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> fontSizeSpinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);
        assertThat(textField).isNotNull();
        assertThat(fontSizeSpinner).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            textField.setText("Font Size Test!");
            fontSizeSpinner.getValueFactory().setValue(24.0);
        });

        robot.clickOn("#textBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);

        assertThat(text).isNotNull();
        assertThat(text.getText()).isEqualTo("Font Size Test!");
        assertThat(text.getFont().getSize()).isEqualTo(24.0);
    }

    /**
     * Verifies that a text shape created via user interaction correctly applies
     * the selected fill and stroke colors from the UI.
     *
     * @param robot the TestFX robot used to simulate user interaction
     */
    @Test
    void testTextFillAndStrokeColor(FxRobot robot) {
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();
        assertThat(strokePicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            textField.setText("Text Fill and Stroke Test!");
            fillPicker.setValue(Color.BLUE);
            strokePicker.setValue(Color.ORANGE);
            controller.onFillColorPicker(new ActionEvent());
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.clickOn("#textBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);

        assertThat(text).isNotNull();
        assertThat(text.getFill()).isEqualTo(Color.BLUE);
        assertThat(text.getStroke()).isEqualTo(Color.ORANGE);
    }

}
