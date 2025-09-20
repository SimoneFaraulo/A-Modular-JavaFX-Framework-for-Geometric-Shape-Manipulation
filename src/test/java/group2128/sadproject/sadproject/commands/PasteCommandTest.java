package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.*;
import group2128.sadproject.sadproject.strategy.DrawingParams;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link PasteCommand} class.
 * <p>
 * These tests verify that shapes (Rectangle, Ellipse, Segment) are correctly pasted
 * into the JavaFX canvas (AnchorPane) with preserved graphical attributes.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PasteCommandTest {

    private AppController controller;

    /**
     * Initializes the JavaFX application context before all tests.
     *
     * @param stage the primary stage provided by TestFX
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
    }

    /**
     * Resets the drawing pane to a clean state before each test.
     *
     * @param robot TestFX robot for UI interaction
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that a {@link RectangleShape} can be pasted correctly into the drawing pane,
     * and its attributes (fill color, edge color, width, height) are preserved.
     *
     * @param robot TestFX robot for UI interaction
     */
    @Test
    public void testPasteRectangleShape(FxRobot robot) {
        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();
        RectangleShape rectangle = new RectangleShape(
                params.getFillColor(), params.getEdgeColor(),
                50, 50, params.getWidthValuePropertyProperty().get(), params.getHeightValuePropertyProperty().get()
        );

        pasteCommand.setShape(rectangle);
        pasteCommand.setPasteX(200.0);
        pasteCommand.setPasteY(150.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);

        AnchorPane pane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);

        RectangleShape pasted = (RectangleShape) pane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);

        assertThat(pasted).isNotNull();
        assertThat(pasted.getFillColor()).isEqualTo(rectangle.getFillColor());
        assertThat(pasted.getEdgeColor()).isEqualTo(rectangle.getEdgeColor());
        assertThat(pasted.getDimensionX()).isEqualTo(rectangle.getDimensionX());
        assertThat(pasted.getDimensionY()).isEqualTo(rectangle.getDimensionY());
        assertThat(pasted.getAnchorX()).isNotEqualTo(rectangle.getAnchorY());
        assertThat(pasted.getAnchorY()).isNotEqualTo(rectangle.getAnchorX());
        assertThat(pasted.getAnchorX()).isEqualTo(200.0);
        assertThat(pasted.getAnchorY()).isEqualTo(150.0);
    }

    /**
     * Tests that an {@link EllipseShape} can be pasted correctly into the drawing pane,
     * and its attributes (fill color, edge color, width, height) are preserved.
     *
     * @param robot TestFX robot for UI interaction
     */
    @Test
    public void testPasteEllipseShape(FxRobot robot) {
        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();
        EllipseShape ellipse = new EllipseShape(
                params.getFillColor(), params.getEdgeColor(),
                50, 50, params.getWidthValuePropertyProperty().get(), params.getHeightValuePropertyProperty().get()
        );

        pasteCommand.setShape(ellipse);
        pasteCommand.setPasteX(250.0);
        pasteCommand.setPasteY(180.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);

        AnchorPane pane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);

        EllipseShape pasted = (EllipseShape) pane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);

        assertThat(pasted).isNotNull();
        assertThat(pasted.getFillColor()).isEqualTo(ellipse.getFillColor());
        assertThat(pasted.getEdgeColor()).isEqualTo(ellipse.getEdgeColor());
        assertThat(pasted.getDimensionX()).isEqualTo(ellipse.getDimensionX());
        assertThat(pasted.getDimensionY()).isEqualTo(ellipse.getDimensionY());
        assertThat(pasted.getAnchorX()).isNotEqualTo(ellipse.getAnchorX());
        assertThat(pasted.getAnchorY()).isNotEqualTo(ellipse.getAnchorY());
        assertThat(pasted.getAnchorX()).isEqualTo(250.0);
        assertThat(pasted.getAnchorY()).isEqualTo(180.0);
    }

    /**
     * Tests that a {@link SegmentShape} can be pasted correctly into the drawing pane,
     * and its attributes (edge color and length) are preserved.
     *
     * @param robot TestFX robot for UI interaction
     */
    @Test
    public void testPasteSegmentShape(FxRobot robot) {
        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();
        SegmentShape segment = new SegmentShape(
                params.getFillColor(), params.getEdgeColor()
        );

        pasteCommand.setShape(segment);
        pasteCommand.setPasteX(300.0);
        pasteCommand.setPasteY(200.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);

        AnchorPane pane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);

        SegmentShape pasted = (SegmentShape) pane.getChildren().stream()
                .filter(node -> node instanceof SegmentShape)
                .findFirst()
                .orElse(null);

        assertThat(pasted).isNotNull();
        assertThat(pasted.getEdgeColor()).isEqualTo(segment.getEdgeColor());
        assertThat(pasted.getStartX() - pasted.getEndX()).isEqualTo(segment.getStartX() - segment.getEndX());
        assertThat(pasted.getStartY() - pasted.getEndY()).isEqualTo(segment.getStartY() - segment.getEndY());
        assertThat(pasted.getAnchorX()).isEqualTo(300.0);
        assertThat(pasted.getAnchorY()).isEqualTo(200.0);
    }

    /**
     * Tests that a {@link TextShape} can be pasted correctly into the drawing pane,
     * and its attributes (fill color, edge color, width, fontSize, text) are preserved.
     *
     * @param robot TestFX robot for UI interaction
     */
    @Test
    public void testPasteTextShape(FxRobot robot) {
        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();
        params.setFontSize(30.0);
        params.setText("Hello World");


        TextShape text = new TextShape(
                params.getFillColor(), params.getEdgeColor()
        );

        text.setDimensionX(params.getScaleX());
        text.setDimensionY(params.getScaleY());
        text.setFontSize(params.getFontSize());

        pasteCommand.setShape(text);
        pasteCommand.setPasteX(200.0);
        pasteCommand.setPasteY(150.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);

        AnchorPane pane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);

        TextShape pasted = (TextShape) pane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);

        assertThat(pasted).isNotNull();
        assertThat(pasted.getFillColor()).isEqualTo(text.getFillColor());
        assertThat(pasted.getEdgeColor()).isEqualTo(text.getEdgeColor());
        assertThat(pasted.getDimensionX()).isEqualTo(text.getDimensionX());
        assertThat(pasted.getDimensionY()).isEqualTo(text.getDimensionY());
        assertThat(pasted.getAnchorX()).isEqualTo(200.0);
        assertThat(pasted.getAnchorY()).isEqualTo(150.0);
        assertThat(pasted.getFontSize()).isEqualTo(text.getFontSize());

    }

    /**
     * Tests that a {@link PolygonShape} can be pasted correctly into the drawing pane,
     * and its attributes (edge color, fill color and dimension) are preserved.
     *
     * @param robot TestFX robot for UI interaction
     */
    @Test
    public void testPastePolygonShape(FxRobot robot) {
        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();

        List<Double> originalPoints = List.of(
                150.0, 150.0,
                200.0, 150.0,
                200.0, 250.0,
                150.0, 250.0
        );

        PolygonShape polygon = new PolygonShape(
                params.getFillColor(),
                params.getEdgeColor(),
                originalPoints
        );
        pasteCommand.setShape(polygon);
        pasteCommand.setPasteX(300.0);
        pasteCommand.setPasteY(200.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);

        AnchorPane pane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);

        PolygonShape pasted = (PolygonShape) pane.getChildren().stream()
                .filter(node -> node instanceof PolygonShape)
                .findFirst()
                .orElse(null);

        assertThat(pasted).isNotNull();
        assertThat(pasted.getEdgeColor()).isEqualTo(polygon.getEdgeColor());
        assertThat(pasted.getFillColor()).isEqualTo(polygon.getFillColor());

        double dx = 300.0 - 150.0;
        double dy = 200.0 - 150.0;

        List<Double> pastedPoints = pasted.getPointsList();
        assertThat(pastedPoints).hasSize(originalPoints.size());

        for (int i = 0; i < originalPoints.size(); i += 2) {
            double expectedX = originalPoints.get(i) + dx;
            double expectedY = originalPoints.get(i + 1) + dy;

            assertThat(pastedPoints.get(i)).isEqualTo(expectedX);
            assertThat(pastedPoints.get(i + 1)).isEqualTo(expectedY);
        }
    }

}

