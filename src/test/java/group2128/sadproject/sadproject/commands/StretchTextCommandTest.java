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
 * Integration tests for verifying the stretching behavior of {@link TextShape}
 * using the {@link StretchTextCommand} in various directions.
 * <p>
 * These tests are executed using TestFX and simulate mouse interactions
 * on the JavaFX GUI to ensure correct scaling (both increase and decrease)
 * of the text shape on the drawing pane.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StretchTextCommandTest {

    private AppController controller;

    /**
     * Initializes the JavaFX environment and loads the main scene before tests.
     *
     * @param stage the primary stage
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
    }

    /**
     * Resets the state of the drawing pane before each test by:
     * - Setting the text to be used in the TextShape
     * - Calling createNewPane() to clear the canvas
     *
     * @param robot the TestFX robot used for interaction
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
     * Tests that stretching the text shape in the positive X direction
     * correctly increases the scale on the X axis.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedGrowX(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
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

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getDimensionX();
        double initialScaleY = text.getDimensionY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth / 2, centerY);

        double displacement = 100;
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() + displacement) - resizePoint.getX();
        double offDragY = (resizePoint.getY() + 0) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getDimensionX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getDimensionY(), within(0.5));
    }

    /**
     * Tests that stretching the text shape in the positive Y direction
     * correctly increases the scale on the Y axis.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedGrowY(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
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

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getDimensionX();
        double initialScaleY = text.getDimensionY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth / 2, centerY);

        double displacement = 100;
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(0, displacement);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() + 0) - resizePoint.getX();
        double offDragY = (resizePoint.getY() + displacement) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getDimensionX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getDimensionY(), within(0.5));
    }


    /**
     * Tests that stretching the text shape in the negative X direction
     * correctly decreases the scale on the X axis.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedReduceX(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
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

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getDimensionX();
        double initialScaleY = text.getDimensionY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth / 2, centerY);

        double displacement = 100;
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() - displacement) - resizePoint.getX();
        double offDragY = (resizePoint.getY() + 0) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getDimensionX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getDimensionY(), within(0.5));
    }

    /**
     * Tests that stretching the text shape in the negative Y direction
     * correctly decreases the scale on the Y axis.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedReduceY(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
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

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getDimensionX();
        double initialScaleY = text.getDimensionY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth / 2, centerY);

        double displacement = 100;
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(0, -displacement);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() + 0) - resizePoint.getX();
        double offDragY = (resizePoint.getY() - displacement) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getDimensionX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getDimensionY(), within(0.5));
    }

    /**
     * Tests that diagonally stretching the text shape towards the top-left
     * results in reduced scale in both X and Y directions.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedDiagonalReduce(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        Spinner<Double> fontSizeMenu = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            fontSizeMenu.getValueFactory().setValue(35.0);
            widthField.setText("250");
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getScaleX();
        double initialScaleY = text.getScaleY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth*0.8, centerY);

        double displacement = 25;

        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, -displacement*2);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() - displacement) - resizePoint.getX();
        double offDragY = (resizePoint.getY() - (displacement*2)) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getScaleX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getScaleY(), within(0.5));
    }

    /**
     * Tests that diagonally stretching the text shape towards the bottom-right
     * increases the scale in both X and Y directions.
     *
     * @param robot the TestFX robot
     */
    @Test
    void testTextShapeIsStretchedDiagonalGrow(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        Spinner<Double> fontSizeMenu = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            fontSizeMenu.getValueFactory().setValue(35.0);
            widthField.setText("250");
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
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double initialScaleX = text.getDimensionX();
        double initialScaleY = text.getDimensionY();
        double initialWrappingWidth = text.getWrappingWidth();
        Point2D resizePoint = new Point2D(centerX + initialWrappingWidth*0.8, centerY);

        double displacement = 25;

        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(+displacement*5, +displacement);
        robot.release(MouseButton.PRIMARY);

        double offDragX = (resizePoint.getX() + displacement*5) - resizePoint.getX();
        double offDragY = (resizePoint.getY() + displacement) - resizePoint.getY();

        assertThat(initialScaleX + offDragX/100).isEqualTo(text.getDimensionX(), within(0.5));
        assertThat(initialScaleY + offDragY/100).isEqualTo(text.getDimensionY(), within(0.5));
    }
}
