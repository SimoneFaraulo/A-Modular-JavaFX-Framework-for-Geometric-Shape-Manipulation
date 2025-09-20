package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.EllipseShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
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
 * Integration tests for EllipseDrawingStrategy.
 * Simulates user interaction to verify that ellipses are drawn correctly on the canvas.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EllipseDrawingStrategyTest {

    private AppController controller;

    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group2128/sadproject/sadproject/hello-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that activating the EllipseDrawingStrategy and clicking the pane adds an EllipseShape.
     */
    @Test
    void testEllipseShapeIsDrawn(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);

        assertThat(drawn).isNotNull();
    }

    /**
     * Tests that when an ellipse is drawn after selecting a stroke color,
     * the resulting shape has the correct stroke color applied.
     *
     * <p>This test simulates a user selecting orange as the edge color using the
     * {@code ColorPicker}, activating the ellipse drawing tool, and clicking
     * on the drawing pane to create an ellipse. It then verifies that the
     * created {@link EllipseShape} has the correct stroke color.</p>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testEllipseEdgeColor(FxRobot robot) {
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(Color.ORANGE);
            robot.sleep(500);
            controller.onStrokeColorPicker(new ActionEvent());
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
        assertThat(ellipse.getEdgeColor()).isEqualTo(Color.ORANGE);
        robot.sleep(500);
    }

    /**
     * Tests that when an ellipse is drawn after selecting a fill color,
     * the resulting shape is filled with the correct color.
     *
     * <p>This test simulates a user interaction where the fill color is set
     * to green using the {@code ColorPicker}, the ellipse drawing tool is activated,
     * and an ellipse is drawn on the drawing pane. The test then verifies that
     * the resulting {@link EllipseShape} has the correct fill color.</p>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testEllipseFillColor(FxRobot robot) {
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
            robot.sleep(500);
            controller.onFillColorPicker(new ActionEvent());
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
        assertThat(ellipse.getFillColor()).isEqualTo(Color.GREEN);
        robot.sleep(500);
    }

    /**
     * Tests that when width and height values are set in their respective text fields,
     * the drawn ellipse has the expected width and height.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testEllipseWidthAndHeight(FxRobot robot) {
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
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
        assertThat(ellipse.getDimensionX()).isEqualTo(100.0);
        assertThat(ellipse.getDimensionY()).isEqualTo(60.0);
    }


}
