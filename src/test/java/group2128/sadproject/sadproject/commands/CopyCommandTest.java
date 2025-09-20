package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.factory.SelectableShape;
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
 * Integration test class for the {@link CopyCommand} functionality.
 * This test verifies that a shape can be correctly copied from the drawing canvas.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CopyCommandTest {

    private AppController controller;
    private Stage stage;

    /**
     * Initializes the JavaFX application before all tests.
     * Loads the main FXML layout and retrieves the {@link AppController} instance.
     *
     * @param stage the primary stage provided by TestFX
     * @throws Exception if the FXML cannot be loaded
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
     * Resets the drawing canvas before each test by creating a new empty pane.
     *
     * @param robot the TestFX robot used to interact with the JavaFX UI
     */
    @BeforeEach
    void resetCanvas(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests the {@link CopyCommand} by:
     * <ul>
     *   <li>Adding a {@link RectangleShape} to the canvas</li>
     *   <li>Executing the copy command</li>
     *   <li>Verifying that the copied shape is not null and is a new instance</li>
     * </ul>
     *
     * @param robot the TestFX robot used to interact with the JavaFX UI
     */
    @Test
    void testCopyShape(FxRobot robot) {
        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        RectangleShape rectangle = new RectangleShape(Color.BLUE, Color.LIGHTBLUE, 100, 50);
        robot.interact(() -> canvas.getChildren().add(rectangle));
        assertThat(canvas.getChildren()).contains(rectangle);

        CopyCommand copyCommand = new CopyCommand();
        copyCommand.setSelectedShape(rectangle);

        robot.interact(copyCommand::execute);

        SelectableShape copiedShape = copyCommand.getCopiedShape();
        assertThat(copiedShape).isNotNull();
        assertThat(copiedShape).isNotSameAs(rectangle);
    }
}

