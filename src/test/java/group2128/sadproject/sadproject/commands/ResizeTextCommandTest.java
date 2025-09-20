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
import static org.assertj.core.api.Assertions.*;

/**
 * Test class for verifying the behavior of the {@link ResizeTextCommand}.
 * This class uses TestFX to simulate interactions with a text shape
 * and checks how resizing affects its font size and wrapping width.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResizeTextCommandTest {

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
        robot.interact(() -> {
            TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
            textField.setText("Hello World");
            controller.createNewPane(null);
        });
    }

    /**
     * Tests that resizing a text shape increases both the font size and wrapping width proportionally.
     */
    @Test
    void testTextShapeIsResizedGrow(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

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

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double initialDimX = text.getDimensionX();
        double initialDimY = text.getDimensionY();
        Point2D resizePoint = new Point2D(centerX + initialDimX / 2, centerY);

        double displacement = 50;

        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, -displacement);
        robot.release(MouseButton.PRIMARY);

        double newDimX = text.getDimensionX();
        double newDimY = text.getDimensionY();

        assertThat(newDimX).isGreaterThan(initialDimX);
        assertThat(newDimY).isGreaterThan(initialDimY);
    }

    /**
     * Tests that resizing a text shape decreases both the font size and wrapping width proportionally.
     */
    @Test
    void testTextShapeIsResizedShrink(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        Spinner<Double> fontSizeMenu = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            fontSizeMenu.getValueFactory().setValue(40.0);
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

        robot.moveTo(new Point2D(centerX+5, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(text);
        assertThat(text.isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double initialDimX = text.getDimensionX();
        double initialDimY = text.getDimensionY();
        Point2D resizePoint = new Point2D(centerX + initialDimX / 2, centerY);

        double displacement = 40;

        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(+displacement, +displacement);
        robot.release(MouseButton.PRIMARY);

        double newDimX = text.getDimensionX();
        double newDimY = text.getDimensionY();

        assertThat(newDimX).isLessThan(initialDimX);
        assertThat(newDimY).isLessThan(initialDimY);
    }
}
