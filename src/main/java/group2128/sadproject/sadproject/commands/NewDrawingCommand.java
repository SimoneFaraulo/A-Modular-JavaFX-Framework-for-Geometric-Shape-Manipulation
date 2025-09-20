package group2128.sadproject.sadproject.commands;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * A concrete implementation of the {@link Command} class that represents
 * the creation of a new drawing within the application.
 * <p>
 * This command is intended to clear the current canvas or initialize a new
 * drawing environment. The specific behavior should be implemented in the
 * {@code execute()} method.
 * </p>
 */
public class NewDrawingCommand extends Command {

    /**
     * Executes the load command with user confirmation.
     * <p>
     * If the drawing canvas is not empty, prompts the user with a confirmation dialog
     * asking whether they want to save the current drawing before continuing.
     * If the user chooses to save, it triggers a {@link SaveCommand} before clearing the canvas.
     * If the user cancels the operation, the command is aborted.
     * If the user proceeds without saving or if the canvas is already empty, the canvas is cleared.
     * </p>
     */
    @Override
    public void execute() {
        if (!super.getDrawingCanvas().getChildren().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("The current drawing will be deleted.");
            alert.setContentText("Do you want to save the drawing before continuing?");

            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Continue without saving");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    SaveCommand saveCommand = new SaveCommand();
                    saveCommand.setDrawingCanvas(super.getDrawingCanvas());
                    saveCommand.setStage((Stage) super.getDrawingCanvas().getScene().getWindow());
                    saveCommand.execute();
                } else if (result.get() == discardButton) {
                    super.getDrawingCanvas().getChildren().clear();
                }
            }
        } else {
            super.getDrawingCanvas().getChildren().clear();
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