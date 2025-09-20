package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.SegmentShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
 * Integration tests for SegmentDrawingStrategy.
 * Simulates user interaction to verify that segments are drawn correctly on the canvas.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SegmentDrawingStrategyTest {

    private AppController controller;

    /**
     * Sets up the JavaFX application stage before each test class run.
     *
     * @param stage the primary stage used for testing
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
     * Resets the canvas state before each test to ensure isolation.
     *
     * @param robot the TestFX robot instance
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that clicking on the canvas after selecting SegmentDrawingStrategy
     * adds a Circle to a Group, representing the first point of the segment.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testCircleIsAddedToGroup(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        Group group = (Group) drawingPane.getChildren().stream()
                .filter(node -> node instanceof Group)
                .findFirst()
                .orElse(null);

        assertThat(group).isNotNull();
        assertThat(group.getChildren()).anyMatch(node -> node instanceof Circle);
    }

    /**
     * Tests that two clicks on the canvas result in a SegmentShape being drawn.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testSegmentShapeIsDrawn(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof SegmentShape);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);

        assertThat(segment).isNotNull();
        assertThat(segment.getAnchorX()).isNotEqualTo(segment.getEndPointX());
    }

    /**
     * Tests that calling onExit via reselecting the same strategy
     * clears temporary graphical elements (Group, Circle, etc.).
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testOnExitClearsTemporaryShapes(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.clickOn("#segmentBtn");

        assertThat(drawingPane.getChildren()).noneMatch(node ->
                node instanceof Circle || node instanceof SegmentShape || node instanceof Group
        );
    }

    /**
     * Tests that switching from SegmentDrawingStrategy to another strategy
     * (e.g., RectangleDrawingStrategy) triggers onExit and removes temporary shapes.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testOnExitClearsTemporaryShapes2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.clickOn("#rectangleBtn");

        assertThat(drawingPane.getChildren()).noneMatch(node ->
                node instanceof Circle || node instanceof SegmentShape || node instanceof Group
        );
    }

    /**
     * Tests that when a color is selected from the stroke ColorPicker and a segment is drawn,
     * the segment's edge color matches the selected color.
     * <p>
     * Steps:
     * <ul>
     *     <li>Set the stroke color to BLUE using the ColorPicker</li>
     *     <li>Activate the SegmentDrawingStrategy</li>
     *     <li>Draw a segment on the canvas</li>
     *     <li>Verify that the segment is added and its color is BLUE</li>
     * </ul>
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testSelectColorAndDrawSegment(FxRobot robot) {

        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.BLUE);
            robot.sleep(500);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);
        assertThat(segment).isNotNull();
        assertThat(segment.getEdgeColor()).isEqualTo(javafx.scene.paint.Color.BLUE);
    }

    /**
     * Tests that changing the stroke color between mouse clicks updates the segment's edge color accordingly.
     * <p>
     * Steps:
     * <ul>
     *     <li>Set the stroke color to BLUE</li>
     *     <li>Start drawing a segment (first click)</li>
     *     <li>Change the stroke color to YELLOW</li>
     *     <li>Complete the segment (second click)</li>
     *     <li>Verify that the drawn segment's edge color is YELLOW</li>
     * </ul>
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeColorAndDrawSegment(FxRobot robot) {

        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.BLUE);
            robot.sleep(500);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            strokePicker.setValue(Color.YELLOW);
            robot.sleep(500);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);

        SegmentShape segment = (SegmentShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);
        assertThat(segment).isNotNull();
        assertThat(segment.getEdgeColor()).isEqualTo(Color.YELLOW);
        robot.sleep(500);
    }

}
