package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.EllipseShape;
import group2128.sadproject.sadproject.factory.PolygonShape;
import group2128.sadproject.sadproject.factory.RectangleShape;
import group2128.sadproject.sadproject.factory.SegmentShape;
import group2128.sadproject.sadproject.factory.TextShape;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A concrete implementation of the {@link Command} class that represents
 * a save operation within the drawing application.
 * <p>
 * This command is intended to trigger the saving of the current state of the canvas,
 * though its behavior must be defined inside the {@code execute()} method.
 * </p>
 */
public class SaveCommand extends Command {

    /**
     * The stage used to display the file chooser dialog.
     */
    private Stage stage;

    /**
     * A variable used only for the automatic tests
     */
    private File outputFile;

    /**
     * Sets the stage used for the file chooser dialog.
     *
     * @param stage the primary stage of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the output file where the graphical shapes will be saved.
     * <p>
     * This method is intended to facilitate automated testing by allowing
     * the bypass of the FileChooser interaction.
     * If set, the {@code execute()} method will save directly to this file.
     *
     * @param file the destination file for saving
     */
    public void setOutputFile(File file) {
        this.outputFile = file;
    }

    /**
     * Executes the save command.
     * <p>
     * Iterates through the shapes present on the drawing canvas and creates
     * a JSON representation for each one. Opens a file chooser to let the user
     * select the save location, then writes the JSON content to the specified file.
     * Supported shape types are:
     * <ul>
     *     <li>{@code RectangleShape} — saved with x, y, width, height, fill, and stroke</li>
     *     <li>{@code EllipseShape} — saved with center coordinates, radii, fill, and stroke</li>
     *     <li>{@code SegmentShape} — saved with start and end coordinates, and stroke</li>
     *      <li>{@code TextShape} — saved with start and end coordinates, width, font, scaleX, scaleY, fill and stroke</li>
     *       <li>{@code PolygonShape} — saved with fill and stroke, and points list</li>
     * </ul>
     * </p>
     * <p>
     * If the canvas or stage is not initialized, the method exits without performing any operation.
     * </p>
     */
    @Override
    public void execute() {
        AnchorPane canvas = getDrawingCanvas();
        if (canvas == null || stage == null) {
            return;
        }

        JSONArray shapesArray = new JSONArray();

        for (Node node : canvas.getChildren()) {
            JSONObject obj = new JSONObject();

            if (node instanceof RectangleShape) {
                RectangleShape rectangle = (RectangleShape) node;
                rectangle.saveJson(obj);
            } else if (node instanceof EllipseShape) {
                EllipseShape ellipse = (EllipseShape) node;
                ellipse.saveJson(obj);
            } else if (node instanceof SegmentShape) {
                SegmentShape segment = (SegmentShape) node;
                segment.saveJson(obj);
            } else if (node instanceof TextShape) {
                TextShape text = (TextShape) node;
                text.saveJson(obj);
            } else if (node instanceof PolygonShape){
                PolygonShape polygon = (PolygonShape) node;
                polygon.saveJson(obj);
            }else{
                continue;
            }

            shapesArray.put(obj);
        }

        File fileToSave;

        if (this.outputFile != null) {
            fileToSave = this.outputFile;
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Shapes");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
            );
            fileToSave = fileChooser.showSaveDialog(stage);
        }

        if (fileToSave != null) {
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(shapesArray.toString(4));
                writer.flush();
            } catch (IOException e) {
                System.err.println("Errore nel salvataggio: " + e.getMessage());
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