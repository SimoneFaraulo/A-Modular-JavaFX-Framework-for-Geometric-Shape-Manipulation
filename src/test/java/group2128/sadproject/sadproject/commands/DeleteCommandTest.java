package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.RectangleShape;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
 * Integration tests for the {@link DeleteCommand} class.
 * <p>
 * These tests verify that a shape is correctly removed from the canvas
 * when the DeleteCommand is executed.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteCommandTest {

    private AppController controller;
    private Stage stage;

    /**
     * Initializes the JavaFX stage and loads the FXML view before all tests.
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
        this.stage = stage;
    }

    /**
     * Resets the canvas to a clean state before each test.
     *
     * @param robot FxRobot used to interact with JavaFX thread
     */
    @BeforeEach
    void resetCanvas(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that a {@link RectangleShape} is successfully removed from the canvas.
     *
     * @param robot FxRobot used to interact with JavaFX thread
     */
    @Test
    void testDeleteShapeFromCanvas(FxRobot robot) {
        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

        RectangleShape shape = new RectangleShape(Color.GREEN, Color.YELLOW, 60, 40);
        robot.interact(() -> canvas.getChildren().add(shape));

        assertThat(canvas.getChildren()).contains(shape);

        robot.interact(() -> {
            DeleteCommand deleteCommand = new DeleteCommand();
            deleteCommand.setDrawingCanvas(canvas);
            deleteCommand.setSelectedShape(shape);
            deleteCommand.execute();
        });

        assertThat(canvas.getChildren()).doesNotContain(shape);
    }

    /**
     * Tests that executing {@link DeleteCommand} with a null shape does not alter the canvas.
     *
     * @param robot FxRobot used to interact with JavaFX thread
     */
    @Test
    void testDeleteNullShapeDoesNothing(FxRobot robot) {
        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

        RectangleShape shape = new RectangleShape(Color.GREEN, Color.YELLOW, 60, 40);
        robot.interact(() -> canvas.getChildren().add(shape));

        assertThat(canvas.getChildren()).contains(shape);

        robot.interact(() -> {
            DeleteCommand deleteCommand = new DeleteCommand();
            deleteCommand.setDrawingCanvas(canvas);
            deleteCommand.setSelectedShape(null);
            deleteCommand.execute();
        });

        assertThat(canvas.getChildren()).contains(shape);
    }
}
