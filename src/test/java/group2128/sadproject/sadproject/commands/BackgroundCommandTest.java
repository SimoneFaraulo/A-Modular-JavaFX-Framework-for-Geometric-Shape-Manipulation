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
 * Integration test class for the {@link BackgroundCommand} functionality.
 * This test verifies that a shape can be sent to the back of the drawing canvas.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackgroundCommandTest {

    private AppController controller;
    private Stage stage;

    /**
     * Initializes the JavaFX application and loads the main UI.
     *
     * @param stage the JavaFX stage
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
        this.stage = stage;
    }

    /**
     * Clears the canvas before each test.
     *
     * @param robot the TestFX robot
     */
    @BeforeEach
    void resetCanvas(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests the {@link BackgroundCommand} by:
     * <ul>
     *   <li>Adding two shapes to the canvas</li>
     *   <li>Ensuring the second one is initially on top</li>
     *   <li>Using BackgroundCommand to send the second shape to the back</li>
     *   <li>Verifying the second shape is now at the back</li>
     * </ul>
     *
     * @param robot the TestFX robot
     */
    @Test
    void testSendShapeToBack(FxRobot robot) {
        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

        RectangleShape shape1 = new RectangleShape(Color.GREEN, Color.LIGHTGREEN, 100, 100);
        RectangleShape shape2 = new RectangleShape(Color.BLUE, Color.LIGHTBLUE, 100, 100);

        robot.interact(() -> {
            canvas.getChildren().addAll(shape1, shape2);
        });
        assertThat(canvas.getChildren().get(canvas.getChildren().size() - 1)).isEqualTo(shape2);
        BackgroundCommand command = new BackgroundCommand();
        command.setSelectedShape(shape2);
        robot.interact(command::execute);
        assertThat(canvas.getChildren().get(0)).isEqualTo(shape2);
    }
}
