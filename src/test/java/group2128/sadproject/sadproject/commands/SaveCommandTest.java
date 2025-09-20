package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.AppController;
import group2128.sadproject.sadproject.factory.EllipseShape;
import group2128.sadproject.sadproject.factory.PolygonShape;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.factory.SegmentShape;
import group2128.sadproject.sadproject.factory.TextShape;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for the SaveCommand class.
 * These tests verify the correct JSON serialization of different shapes
 * (RectangleShape, EllipseShape, SegmentShape) added to the canvas.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveCommandTest {

    private AppController controller;
    private Stage stage;

    /**
     * Initializes the JavaFX environment and loads the application UI.
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
     * Resets the canvas state before each test by creating a new pane.
     */
    @BeforeEach
    void resetState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests saving a RectangleShape to JSON.
     */
    @Test
    void testSaveRectangleShapeToJson(FxRobot robot) throws IOException {
        final File tempFile = File.createTempFile("testSave", ".json");
        tempFile.deleteOnExit();

        robot.interact(() -> {
            AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

            RectangleShape rect = new RectangleShape(Color.RED, Color.BLACK,150,160,120,90);
            rect.setStrokeWidth(2.5);
            canvas.getChildren().add(rect);

            SaveCommand saveCommand = new SaveCommand();
            saveCommand.setDrawingCanvas(canvas);
            saveCommand.setStage(stage);
            saveCommand.setOutputFile(tempFile);
            saveCommand.execute();
        });

        String content = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
        JSONArray json = new JSONArray(content);
        assertThat(json).hasSize(1);
        JSONObject rectJson = json.getJSONObject(0);

        assertThat(rectJson.getString("type")).isEqualTo("rectangle");
        assertThat(rectJson.getDouble("x")).isEqualTo(150.0);
        assertThat(rectJson.getDouble("y")).isEqualTo(160.0);
        assertThat(rectJson.getDouble("width")).isEqualTo(120.0);
        assertThat(rectJson.getDouble("height")).isEqualTo(90.0);
        assertThat(rectJson.getString("fill")).contains("0xff0000");
        assertThat(rectJson.getString("stroke")).contains("0x000000");
        assertThat(rectJson.getDouble("strokeWidth")).isEqualTo(2.5);
        assertThat(rectJson.getDouble("flipHorizontal")).isEqualTo(1.0);
        assertThat(rectJson.getDouble("flipVertical")).isEqualTo(1.0);
        assertThat(rectJson.getDouble("rotation")).isEqualTo(0.0);
    }

    /**
     * Tests saving an EllipseShape to JSON.
     */
    @Test
    void testSaveEllipseShapeToJson(FxRobot robot) throws IOException {
        final File tempFile = File.createTempFile("testEllipseSave", ".json");
        tempFile.deleteOnExit();

        robot.interact(() -> {
            AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

            EllipseShape ellipse = new EllipseShape(Color.GREEN, Color.BLUE);
            ellipse.setAnchorX(100.0);
            ellipse.setAnchorY(150.0);
            canvas.getChildren().add(ellipse);

            SaveCommand saveCommand = new SaveCommand();
            saveCommand.setDrawingCanvas(canvas);
            saveCommand.setStage(stage);
            saveCommand.setOutputFile(tempFile);
            saveCommand.execute();
        });

        String content = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
        JSONArray json = new JSONArray(content);
        assertThat(json).hasSize(1);
        JSONObject ellipseJson = json.getJSONObject(0);

        assertThat(ellipseJson.getString("type")).isEqualTo("ellipse");
        assertThat(ellipseJson.getDouble("x")).isEqualTo(100.0);
        assertThat(ellipseJson.getDouble("y")).isEqualTo(150.0);
        assertThat(ellipseJson.getString("fill")).contains("0x008000");
        assertThat(ellipseJson.getString("stroke")).contains("0x0000ff");
        assertThat(ellipseJson.getDouble("flipHorizontal")).isEqualTo(1.0);
        assertThat(ellipseJson.getDouble("flipVertical")).isEqualTo(1.0);
        assertThat(ellipseJson.getDouble("rotation")).isEqualTo(0.0);
    }

    /**
     * Tests saving a SegmentShape to JSON.
     */
    @Test
    void testSaveSegmentShapeToJson(FxRobot robot) throws IOException {
        final File tempFile = File.createTempFile("testSegmentSave", ".json");
        tempFile.deleteOnExit();

        robot.interact(() -> {
            AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

            SegmentShape segment = new SegmentShape(null,Color.ORANGE);
            segment.setAnchorX(10.0);
            segment.setAnchorY(20.0);
            segment.setEndPointX(110.0);
            segment.setEndPointY(220.0);
            canvas.getChildren().add(segment);

            SaveCommand saveCommand = new SaveCommand();
            saveCommand.setDrawingCanvas(canvas);
            saveCommand.setStage(stage);
            saveCommand.setOutputFile(tempFile);
            saveCommand.execute();
        });

        String content = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
        JSONArray json = new JSONArray(content);
        assertThat(json).hasSize(1);
        JSONObject segmentJson = json.getJSONObject(0);

        assertThat(segmentJson.getString("type")).isEqualTo("segment");
        assertThat(segmentJson.getDouble("startX")).isEqualTo(10.0);
        assertThat(segmentJson.getDouble("startY")).isEqualTo(20.0);
        assertThat(segmentJson.getDouble("endX")).isEqualTo(110.0);
        assertThat(segmentJson.getDouble("endY")).isEqualTo(220.0);
        assertThat(segmentJson.getString("stroke")).contains("0xffa500");
        assertThat(segmentJson.getDouble("flipHorizontal")).isEqualTo(1.0);
        assertThat(segmentJson.getDouble("flipVertical")).isEqualTo(1.0);
        assertThat(segmentJson.getDouble("rotation")).isEqualTo(0.0);
    }

    /**
     * Tests saving a TextShape to JSON.
     */
    @Test
    void testSaveTextShapeToJson(FxRobot robot) throws IOException {
        final File tempFile = File.createTempFile("testTextSave", ".json");
        tempFile.deleteOnExit();

        robot.interact(() -> {
            AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

            TextShape text = new TextShape(Color.GREEN,Color.ORANGE);
            text.setAnchorX(10.0);
            text.setAnchorY(20.0);
            text.setDimensionX(2.0);
            text.setDimensionY(3.0);
            text.setFontSize(220);
            text.setText("testSaveTextShapeToJson");
            canvas.getChildren().add(text);

            SaveCommand saveCommand = new SaveCommand();
            saveCommand.setDrawingCanvas(canvas);
            saveCommand.setStage(stage);
            saveCommand.setOutputFile(tempFile);
            saveCommand.execute();
        });

        String content = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
        JSONArray json = new JSONArray(content);
        assertThat(json).hasSize(1);
        JSONObject textJson = json.getJSONObject(0);

        assertThat(textJson.getString("type")).isEqualTo("text");
        assertThat(textJson.getDouble("x")).isEqualTo(10.0);
        assertThat(textJson.getDouble("y")).isEqualTo(20.0);
        assertThat(textJson.getDouble("fontSize")).isEqualTo(220.0);
        assertThat(textJson.getDouble("flipHorizontal")).isEqualTo(2.0);
        assertThat(textJson.getDouble("flipVertical")).isEqualTo(3.0);
        assertThat(textJson.getString("text")).contains("testSaveTextShapeToJson");
        assertThat(textJson.getString("fill")).contains("0x008000");
        assertThat(textJson.getString("stroke")).contains("0xffa500");
        assertThat(textJson.getDouble("fontSize")).isEqualTo(220);
        assertThat(textJson.getDouble("rotation")).isEqualTo(0.0);
    }

    /**
     * Tests saving a PolygonShape to JSON.
     */
    @Test
    void testSavePolygonShapeToJson(FxRobot robot) throws IOException {
        final File tempFile = File.createTempFile("testPolygonSave", ".json");
        tempFile.deleteOnExit();

        robot.interact(() -> {
            AnchorPane canvas = controller.getDrawingContext().getDrawingParams().getDrawingCanvas();

            PolygonShape polygon = new PolygonShape(Color.GREEN,Color.ORANGE);
            List<Double> points = new ArrayList<>(List.of(10.0, 20.0, 110.0, 220.0, 100.0, 200.0));
            polygon.setPoints(points);
            canvas.getChildren().add(polygon);

            SaveCommand saveCommand = new SaveCommand();
            saveCommand.setDrawingCanvas(canvas);
            saveCommand.setStage(stage);
            saveCommand.setOutputFile(tempFile);
            saveCommand.execute();
        });

        String content = new String(Files.readAllBytes(tempFile.toPath()), StandardCharsets.UTF_8);
        JSONArray json = new JSONArray(content);
        assertThat(json).hasSize(1);
        JSONObject polygonJson = json.getJSONObject(0);

        assertThat(polygonJson.getString("type")).isEqualTo("polygon");
        JSONArray pointsJson = polygonJson.getJSONArray("points");
        List<Double> expectedPoints = List.of(10.0, 20.0, 110.0, 220.0, 100.0, 200.0);
        assertThat(pointsJson).hasSize(expectedPoints.size());
        for (int i = 0; i < expectedPoints.size(); i++) {
            assertThat(pointsJson.getDouble(i)).isEqualTo(expectedPoints.get(i));
        }
        assertThat(polygonJson.getString("fill")).contains("0x008000");
        assertThat(polygonJson.getString("stroke")).contains("0xffa500");
        assertThat(polygonJson.getDouble("flipHorizontal")).isEqualTo(1.0);
        assertThat(polygonJson.getDouble("flipVertical")).isEqualTo(1.0);
        assertThat(polygonJson.getDouble("rotation")).isEqualTo(0.0);
    }

}