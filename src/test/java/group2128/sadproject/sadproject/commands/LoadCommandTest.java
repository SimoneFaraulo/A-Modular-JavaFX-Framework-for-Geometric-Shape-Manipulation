package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.*;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for the {@link LoadCommand} class.
 * <p>
 * These tests use the TestFX framework to simulate interaction with the JavaFX UI.
 * Each test verifies that the correct shape is loaded into the canvas from a JSON file.
 * The class also verifies that invalid JSON input is handled gracefully.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoadCommandTest {

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
     * Resets the drawing canvas before each test to ensure a clean state.
     *
     * @param robot the FxRobot used to interact with the JavaFX thread
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests loading a {@link RectangleShape} from a JSON file.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary JSON file cannot be created
     */
    @Test
    void testLoadRectangleShapeFromJson(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "[\n" +
                        "  {\n" +
                        "    \"type\": \"rectangle\",\n" +
                        "    \"x\": 50,\n" +
                        "    \"y\": 60,\n" +
                        "    \"width\": 100,\n" +
                        "    \"height\": 80,\n" +
                        "    \"fill\": \"RED\",\n" +
                        "    \"stroke\": \"BLACK\",\n" +
                        "    \"strokeWidth\": 2,\n" +
                        "    \"rotation\": 0,\n  " +
                        "    \"flipHorizontal\": 1.0,\n" +
                        "    \"flipVertical\": 1.0\n" +
                        "  }\n" +
                        "]"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());
            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        Optional<RectangleShape> rectangle = canvas.getChildren().stream()
                .filter(n -> n instanceof RectangleShape)
                .map(n -> (RectangleShape) n)
                .findFirst();

        assertThat(rectangle).isPresent();
        RectangleShape r = rectangle.get();
        assertThat(r.getAnchorX()).isEqualTo(50.0);
        assertThat(r.getAnchorY()).isEqualTo(60.0);
        assertThat(r.getFillColor()).isEqualTo(Color.RED);
        assertThat(r.getEdgeColor()).isEqualTo(Color.BLACK);
        assertThat(r.getDimensionX()).isEqualTo(100.0);
        assertThat(r.getDimensionY()).isEqualTo(80.0);
        assertThat(r.getRotation()).isEqualTo(0.0);
        assertThat(r.getScaleX()).isEqualTo(1.0);
        assertThat(r.getScaleY()).isEqualTo(1.0);
    }

    /**
     * Tests loading an {@link EllipseShape} from a JSON file.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary JSON file cannot be created
     */
    @Test
    void testLoadEllipseShapeFromJson(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "[\n" +
                        "  {\n" +
                        "    \"type\": \"ellipse\",\n" +
                        "    \"x\": 120,\n" +
                        "    \"y\": 90,\n" +
                        "    \"radiusX\": 40,\n" +
                        "    \"radiusY\": 30,\n" +
                        "    \"fill\": \"BLUE\",\n" +
                        "    \"stroke\": \"BLACK\",\n" +
                        "    \"strokeWidth\": 2,\n" +
                        "    \"rotation\": 0,\n  " +
                        "    \"flipHorizontal\": 1.0,\n" +
                        "    \"flipVertical\": 1.0\n" +
                        "  }\n" +
                        "]"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());

            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        Optional<EllipseShape> ellipse = canvas.getChildren().stream()
                .filter(n -> n instanceof EllipseShape)
                .map(n -> (EllipseShape) n)
                .findFirst();

        assertThat(ellipse).isPresent();
        EllipseShape e = ellipse.get();
        assertThat(e.getAnchorX()).isEqualTo(120.0);
        assertThat(e.getAnchorY()).isEqualTo(90.0);
        assertThat(e.getFillColor()).isEqualTo(Color.BLUE);
        assertThat(e.getEdgeColor()).isEqualTo(Color.BLACK);
        assertThat(e.getDimensionX()).isEqualTo(40.0);
        assertThat(e.getDimensionY()).isEqualTo(30.0);
        assertThat(e.getRotation()).isEqualTo(0.0);
        assertThat(e.getScaleX()).isEqualTo(1.0);
        assertThat(e.getScaleY()).isEqualTo(1.0);
    }

    /**
     * Tests loading a {@link SegmentShape} from a JSON file.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary JSON file cannot be created
     */
    @Test
    void testLoadSegmentShapeFromJson(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "[\n" +
                        "  {\n" +
                        "    \"type\": \"segment\",\n" +
                        "    \"startX\": 10,\n" +
                        "    \"startY\": 20,\n" +
                        "    \"endX\": 110,\n" +
                        "    \"endY\": 120,\n" +
                        "    \"stroke\": \"GREEN\",\n" +
                        "    \"strokeWidth\": 2,\n" +
                        "    \"rotation\": 0,\n  " +
                        "    \"flipHorizontal\": 1.0,\n" +
                        "    \"flipVertical\": 1.0\n" +
                        "  }\n" +
                        "]"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());
            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        Optional<SegmentShape> segment = canvas.getChildren().stream()
                .filter(n -> n instanceof SegmentShape)
                .map(n -> (SegmentShape) n)
                .findFirst();

        assertThat(segment).isPresent();
        SegmentShape s = segment.get();
        assertThat(s.getAnchorX()).isEqualTo(10.0);
        assertThat(s.getAnchorY()).isEqualTo(20.0);
        assertThat(s.getEndPointX()).isEqualTo(110.0);
        assertThat(s.getEndPointY()).isEqualTo(120.0);
        assertThat(s.getEdgeColor()).isEqualTo(Color.GREEN);
        assertThat(s.getStrokeWidth()).isEqualTo(3.0f);
        assertThat(s.getRotation()).isEqualTo(0.0);
        assertThat(s.getScaleX()).isEqualTo(1.0);
        assertThat(s.getScaleY()).isEqualTo(1.0);
    }

    /**
     * Tests loading a {@link PolygonShape} from a JSON file.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary JSON file cannot be created
     */
    @Test
    void testLoadPolygonShapeFromJson(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "[\n" +
                        "  {\n" +
                        "    \"type\": \"polygon\",\n" +
                        "    \"fill\": \"ORANGE\",\n" +
                        "    \"stroke\": \"GREEN\",\n" +
                        "    \"points\": [\n438.0,\n" +
                        "            256.0,\n" +
                        "            440.0,\n" +
                        "            276.0,\n" +
                        "            455.0,\n" +
                        "            274.0,\n" +
                        "            463.0,\n" +
                        "            261.0" +
                        "            ],\n" +
                        "    \"strokeWidth\": 2,\n" +
                        "    \"rotation\": 0,\n  " +
                        "    \"flipHorizontal\": 1.0,\n" +
                        "    \"flipVertical\": 1.0\n" +
                        "  }\n" +
                        "]"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());
            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        Optional<PolygonShape> polygon = canvas.getChildren().stream()
                .filter(n -> n instanceof PolygonShape)
                .map(n -> (PolygonShape) n)
                .findFirst();

        List<Double> expPoints = List.of(438.0, 256.0, 440.0, 276.0, 455.0, 274.0, 463.0, 261.0);
        assertThat(polygon).isPresent();
        PolygonShape s = polygon.get();
        assertThat(s.getFillColor()).isEqualTo(Color.ORANGE);
        assertThat(s.getEdgeColor()).isEqualTo(Color.GREEN);
        List<Double> points = s.getPointsList();
        for(int i = 0; i < expPoints.size(); i++) {
            assertThat(points.get(i)).isEqualTo(expPoints.get(i));
        }
        assertThat(s.getRotation()).isEqualTo(0.0);
        assertThat(s.getScaleX()).isEqualTo(1.0);
        assertThat(s.getScaleY()).isEqualTo(1.0);
    }


    /**
     * Tests loading a {@link TextShape} from a JSON file.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary JSON file cannot be created
     */
    @Test
    void testLoadTextShapeFromJson(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "[\n" +
                        "  {\n" +
                        "    \"type\": \"text\",\n" +
                        "    \"x\": 10,\n" +
                        "    \"y\": 20,\n" +
                        "    \"fontSize\": 120,\n" +
                        "    \"flipHorizontal\": \"1.0\",\n" +
                        "    \"flipVertical\": \"1.0\",\n" +
                        "    \"stroke\": \"GREEN\",\n" +
                        "    \"fill\": \"BLUE\",\n" +
                        "    \"strokeWidth\": 2,\n" +
                        "    \"rotation\": 0\n  " +
                        "  }\n" +
                        "]"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());
            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        Optional<TextShape> text = canvas.getChildren().stream()
                .filter(n -> n instanceof TextShape)
                .map(n -> (TextShape) n)
                .findFirst();

        assertThat(text).isPresent();
        TextShape t = text.get();
        assertThat(t.getAnchorX()).isEqualTo(10.0);
        assertThat(t.getAnchorY()).isEqualTo(20.0);
        assertThat(t.getDimensionX()).isEqualTo(1.0);
        assertThat(t.getDimensionY()).isEqualTo(1.0);
        assertThat(t.getFillColor()).isEqualTo(Color.BLUE);
        assertThat(t.getEdgeColor()).isEqualTo(Color.GREEN);
        assertThat(t.getFontSize()).isEqualTo(120);
        assertThat(t.getRotation()).isEqualTo(0.0);
    }

    /**
     * Tests the behavior of the {@link LoadCommand} when given an invalid JSON file.
     * The canvas should remain empty after execution.
     *
     * @param robot the FxRobot used to run the command on the JavaFX thread
     * @throws IOException if the temporary invalid JSON file cannot be created
     */
    @Test
    void testLoadInvalidJsonFormat(FxRobot robot) throws IOException {
        File tempJson = createJsonFileWithContent(
                "{ invalid_json: true }"
        );

        robot.interact(() -> {
            LoadCommand loadCommand = new LoadCommand(tempJson);
            loadCommand.setStage(stage);
            loadCommand.setCommandHistory(new CommandHistory());
            loadCommand.setDrawingCanvas(controller.getDrawingContext().getDrawingParams().getDrawingCanvas());
            loadCommand.execute();
        });

        AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();
        assertThat(canvas.getChildren()).isEmpty();
    }

    /**
     * Creates a temporary JSON file with the given content.
     * The file is marked for deletion on exit.
     *
     * @param content the JSON string to be written to the file
     * @return a reference to the created File
     * @throws IOException if the file cannot be created or written
     */
    private File createJsonFileWithContent(String content) throws IOException {
        File file = File.createTempFile("test_shape", ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        file.deleteOnExit();
        return file;
    }
}
