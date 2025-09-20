package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.PolygonShape;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import java.util.ArrayList;
import java.util.List;

public class ResizePolygonCommand extends ResizeCommand{
    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    @Override
    public void execute() {
        PolygonShape shape = (PolygonShape) getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.SE_RESIZE);


                shape.setUserData(new ArrayList<>(shape.getPoints()));
                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                @SuppressWarnings("unchecked")
                List<Double> originalPoints = (List<Double>) shape.getUserData();

                double centerX = 0, centerY = 0;
                for (int i = 0; i < originalPoints.size(); i += 2) {
                    centerX += originalPoints.get(i);
                    centerY += originalPoints.get(i + 1);
                }

                int pointCount = originalPoints.size() / 2;
                centerX /= pointCount;
                centerY /= pointCount;

                double mouseX = event.getX();
                double mouseY = event.getY();

                double currentDist = Math.hypot(mouseX - centerX, mouseY - centerY);
                double baseDist = Math.hypot(originalPoints.get(0) - centerX, originalPoints.get(1) - centerY);

                double scale = Math.max(currentDist / baseDist, 0.1);

                ObservableList<Double> newPoints = shape.getPoints();
                newPoints.clear();

                for (int i = 0; i < originalPoints.size(); i += 2) {
                    double ox = originalPoints.get(i);
                    double oy = originalPoints.get(i + 1);

                    double scaledX = centerX + (ox - centerX) * scale;
                    double scaledY = centerY + (oy - centerY) * scale;

                    newPoints.add(scaledX);
                    newPoints.add(scaledY);
                }

                event.consume();
            }
        });

        shape.setOnMouseReleased(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.DEFAULT);
                setShouldTriggerInteraction(true);
            }
        });
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
