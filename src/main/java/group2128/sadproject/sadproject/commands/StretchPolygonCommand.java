package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.PolygonShape;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class StretchPolygonCommand extends StretchCommand{

    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    @Override
    public void execute() {
        PolygonShape shape = (PolygonShape) super.getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                int index = findIndex(event);
                shape.setUserData(new int[]{
                        index,
                });
                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                int[] data = (int[]) shape.getUserData();
                int index = data[0];
                List<Double> points = shape.getPointsList();

                if (index != -1) {
                    double mouseX = event.getX();
                    double mouseY = event.getY();
                    points.set(index, mouseX);
                    points.set(index + 1, mouseY);
                }
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
     * Undoes the previously executed command.
     * <p>
     * Subclasses must implement this method to restore the previous state of the canvas,
     * typically using the stored memento.
     * </p>
     */
    @Override
    public void undo() {
        if (getDrawingCanvasMemento() != null) {
            getDrawingCanvasMemento().restore();
        }
    }

    /**
     * Finds the index of the point in the polygon that is within a specified tolerance
     * of the mouse event's (x, y) coordinates.
     *
     * <p>This method iterates through the list of points of the {@link PolygonShape} and
     * compares each point's x and y values to the coordinates of the given {@link MouseEvent}.
     * If a point is found within a 5-pixel tolerance in both x and y directions, the index
     * of the x-coordinate in the point list is returned.</p>
     *
     * @param event the MouseEvent containing the x and y coordinates to compare.
     * @return the index of the x-coordinate in the point list if a nearby point is found;
     *         -1 if no matching point is within the tolerance.
     */
    private int findIndex(MouseEvent event) {
        PolygonShape shape = (PolygonShape) super.getShape();
        ObservableList<Double> points = shape.getPointsList();
        int tolerance = 5;
        for (int i = 0; i < points.size(); i += 2) {
            if (Math.abs(points.get(i) - event.getX()) < tolerance && Math.abs(points.get(i + 1) - event.getY()) < tolerance) {

                return i;
            }
        }
        return -1;
    }
}
