package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.PolygonShape;
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
class PolygonDrawingStrategyTest {

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
     * Tests that clicking on the canvas after selecting PolygonDrawingStrategy
     * adds a Circle to a Group, representing the first 3 points of the polygon.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testCircleIsAddedToGroup(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70, -150).clickOn(MouseButton.PRIMARY);

        Group group = (Group) drawingPane.getChildren().stream()
                .filter(node -> node instanceof Group)
                .findFirst()
                .orElse(null);

        assertThat(group).isNotNull();
        long circleCount = group.getChildren().stream()
                .filter(node -> node instanceof Circle)
                .count();
        assertThat(circleCount).isEqualTo(3);
    }


    /**
     * Tests that three clicks on the canvas result in a PolygonShape being drawn.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testPolygonShapeIsDrawn(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70, -150).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape polygon = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);

        assertThat(polygon).isNotNull();
    }

    /**
     * Tests that seven clicks on the canvas result in a PolygonShape being drawn.
     *
     * @param robot the TestFX robot instance
     */
    @Test
    void testPolygonShapeIsDrawn2(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70, -150).clickOn(MouseButton.PRIMARY);
        robot.moveBy(200,200).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-100,-100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-200,0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(0,100).doubleClickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof PolygonShape);

        PolygonShape polygon = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);

        assertThat(polygon).isNotNull();
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

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70, -150).clickOn(MouseButton.PRIMARY);
        robot.moveBy(200,200).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#polygonBtn");

        assertThat(drawingPane.getChildren()).noneMatch(node ->
                node instanceof Circle || node instanceof PolygonShape || node instanceof Group
        );
    }


    /**
     * Tests that when a color is selected from the stroke ColorPicker, fill ColoPicker and a polygon is drawn,
     * the polygon's edge and fill colors match the selected color.
     * <p>
     * Steps:
     * <ul>
     *     <li>Set the stroke color to RED using the ColorPicker</li>
     *     <li>Set the fill color to YELLOW using the ColorPicker</li>
     *     <li>Activate the PolygonDrawingStrategy</li>
     *     <li>Draw a polygon on the canvas</li>
     *     <li>Verify that the polygon is added and its color is YELLOW for the fill and RED for the stroke</li>
     * </ul>
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testSelectColorsAndDrawPolygon(FxRobot robot) {

        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.RED);
            controller.onStrokeColorPicker(new ActionEvent());
            fillPicker.setValue(javafx.scene.paint.Color.YELLOW);
            controller.onFillColorPicker(new ActionEvent());
        });

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(200,200).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-70, -150).doubleClickOn(MouseButton.PRIMARY);

        PolygonShape polygon = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(polygon).isNotNull();
        assertThat(polygon.getEdgeColor()).isEqualTo(javafx.scene.paint.Color.RED);
        assertThat(polygon.getFillColor()).isEqualTo(javafx.scene.paint.Color.YELLOW);
    }

    /**
     * Tests that changing the stroke and fill colors between mouse clicks updates the polygon's edge and fill colors accordingly.
     * <p>
     * Steps:
     * <ul>
     *     <li>Set the stroke color to ORANGE</li>
     *     <li>Set the fill color to BLUE</li>
     *     <li>Start drawing a polygon (two clicks)</li>
     *     <li>Change the stroke color to YELLOW</li>
     *     <li>Change the fill color to PINK</li>
     *     <li>Complete the polygon (third click)</li>
     *     <li>Verify that the drawn polygon's edge and fill colors are YELLOW and PINK</li>
     * </ul>
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeColorAndDrawPolygon(FxRobot robot) {

        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(Color.ORANGE);
            controller.onStrokeColorPicker(new ActionEvent());
            fillPicker.setValue(Color.BLUE);
            controller.onFillColorPicker(new ActionEvent());
        });

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(40,0).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            strokePicker.setValue(Color.YELLOW);
            controller.onStrokeColorPicker(new ActionEvent());
            fillPicker.setValue(Color.PINK);
            controller.onFillColorPicker(new ActionEvent());
        });

        robot.moveBy(50, 50).doubleClickOn(MouseButton.PRIMARY);

        PolygonShape polygon = (PolygonShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);
        assertThat(polygon).isNotNull();
        assertThat(polygon.getEdgeColor()).isEqualTo(Color.YELLOW);
        assertThat(polygon.getFillColor()).isEqualTo(Color.PINK);
    }
}