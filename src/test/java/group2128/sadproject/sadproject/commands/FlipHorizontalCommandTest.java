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
 * Integration test class for verifying horizontal flipping behavior
 * of {@link EllipseShape} instances within the drawing pane.
 * <p>
 * Uses TestFX to simulate real user interaction.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlipHorizontalCommandTest {

    /**
     * Reference to the application controller, used to access and manipulate UI components.
     */
    private AppController controller;

    /**
     * Initializes and launches the JavaFX UI environment before any test is run.
     *
     * @param stage the primary stage supplied by TestFX
     * @throws Exception if the FXML file fails to load
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
     * @param robot the FxRobot instance used to simulate user actions
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Verifies that an {@link EllipseShape} is correctly flipped horizontally
     * when the "Flip Horizontal" command is triggered via the context menu.
     *
     * @param robot the FxRobot instance used to simulate user input
     */
    @Test
    void testEllipseShapeIsFlipped(FxRobot robot) {
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
        robot.moveTo("#flipHorizontalBtn").clickOn(MouseButton.PRIMARY);
        assertThat(drawn.getScaleX()).isEqualTo(-1.0);
    }
}
