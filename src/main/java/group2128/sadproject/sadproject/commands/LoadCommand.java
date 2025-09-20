package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.*;
import group2128.sadproject.sadproject.strategy.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * A concrete implementation of the {@link Command} class that represents
 * a load operation within the drawing application.
 * <p>
 * This command is intended to restore a previously saved drawing,
 * though the actual behavior must be defined in the {@code execute()} method.
 * </p>
 */
public class LoadCommand extends Command {

    /**
     * JavaFX stage used to open the file chooser dialog.
     */
    private Stage stage;

    /**
     * JSON file used for testing purposes. If set, this file will be used instead of opening a file chooser dialog.
     */
    private File testFile;

    /**
     * Holds the history of executed commands, used to enable undo functionality.
     */
    private CommandHistory commandHistory;

    /**
     * Strategy used to draw shapes dynamically during loading.
     */
    private DrawingStrategy strategy;

    /**
     * Parameters used to configure drawing behavior, such as color and dimensions.
     */
    private DrawingParams params;

    /**
     * Constructor used for testing. Allows injecting a specific JSON file to load shapes from.
     *
     * @param testFile the JSON file containing shape data to load
     */
    public LoadCommand(File testFile) {
        params = new DrawingParams();
        this.testFile = testFile;
    }

    /**
     * Default constructor. Used in the production environment where the file is selected via a file chooser.
     */
    public LoadCommand() {
        params = new DrawingParams();
    }

    /**
     * Sets the JavaFX {@link Stage} reference used for displaying the file chooser dialog.
     *
     * @param stage the main window of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the command history used to support undo/redo functionality.
     *
     * @param commandHistory the command history instance
     */
    public void setCommandHistory(CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
    }

    /**
     * Executes the load command.
     * <p>
     * Opens a file chooser to select a previously saved JSON file,
     * reads its contents, and reconstructs the corresponding shapes on the canvas.
     * Supported shapes are: {@code rectangle}, {@code ellipse}, {@code segment}, {@code polygon} and {@code text}.
     * </p>
     * <p>
     * If an error occurs while reading or parsing the file,
     * an error alert dialog is shown and a message is printed to {@code System.err}.
     * </p>
     */
    @Override
    public void execute() {

        AnchorPane canvas = getDrawingCanvas();
        params.setDrawingCanvas(canvas);
        params.setCommandHistory(commandHistory);

        if (canvas == null || stage == null) {
            return;
        }

        File fileToLoad;

        if (testFile != null) {
            fileToLoad = testFile;
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Drawing File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File JSON", "*.json"));
            fileToLoad = fileChooser.showOpenDialog(stage);
        }

        if (fileToLoad != null) {
            try {
                byte[] encoded = Files.readAllBytes(fileToLoad.toPath());
                String content = new String(encoded, StandardCharsets.UTF_8);
                JSONArray shapesArray = new JSONArray(content);
                canvas.getChildren().clear();

                for (int i = 0; i < shapesArray.length(); i++) {
                    JSONObject obj = shapesArray.getJSONObject(i);
                    String type = obj.getString("type");

                    switch (type) {
                        case "rectangle":
                            strategy = new RectangleDrawingStrategy();
                            RectangleShape.loadJson(params, obj);
                            strategy.draw(obj.getDouble("x"), obj.getDouble("y"), params);
                            break;
                        case "ellipse":
                            strategy = new EllipseDrawingStrategy();
                            EllipseShape.loadJson(params, obj);
                            strategy.draw(obj.getDouble("x"), obj.getDouble("y"), params);
                            break;
                        case "segment":
                            strategy = new SegmentDrawingStrategy();
                            SegmentShape.loadJson(params, obj);
                            strategy.draw(obj.getDouble("startX"), obj.getDouble("startY"), params);
                            strategy.draw(obj.getDouble("endX"), obj.getDouble("endY"), params);
                            break;
                        case "text":
                            strategy = new TextDrawingStrategy();
                            TextShape.loadJson(params, obj);
                            strategy.draw(obj.getDouble("x"), obj.getDouble("y"), params);
                            break;
                        case "polygon":
                            strategy = new PolygonDrawingStrategy();
                            PolygonDrawingStrategy pStrategy = (PolygonDrawingStrategy) strategy;
                            PolygonShape.loadJson(params, obj);
                            JSONArray jsonPoints = obj.getJSONArray("points");
                            for (int j = 0; j < jsonPoints.length(); j+=2) {
                                pStrategy.draw(jsonPoints.getDouble(j),jsonPoints.getDouble(j+1),params);
                            }
                            pStrategy.completeShape(params);
                            break;

                        default:
                            System.err.println("Type of shape not recognized: " + type);
                            break;
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading Error");
                alert.setHeaderText("Unable to load the file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                System.err.println("Loading Error: " + e.getMessage());
            }
        }
    }

    /**
     * Undoes the previously executed fill color change command.
     *
     * <p>If a memento of the drawing canvas is available, this method restores
     * the canvas to its previous state before the fill color was changed.</p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

}
