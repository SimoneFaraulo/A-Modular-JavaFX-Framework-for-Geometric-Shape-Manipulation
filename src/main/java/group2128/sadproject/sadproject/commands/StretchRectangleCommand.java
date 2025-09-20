package group2128.sadproject.sadproject.commands;

import group2128.sadproject.sadproject.factory.RectangleShape;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;

public class StretchRectangleCommand extends StretchCommand{
    /**
     * Executes the command.
     * <p>
     * Subclasses must provide a concrete implementation of this method to define
     * the specific action to be performed when the command is invoked.
     * </p>
     */
    @Override
    public void execute() {
        RectangleShape shape = (RectangleShape) super.getShape();

        if (shape == null) {
            return;
        }

        shape.setOnMousePressed(event -> {
            if (shape.isSelected()) {
                shape.setCursor(Cursor.MOVE);
                Point2D localMouse = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double fixedX,fixedY;

                if(localMouse.getX() - shape.getAnchorX() < 0){
                    fixedX = shape.getAnchorX() + shape.getDimensionX()/2;
                }else{
                    fixedX = shape.getAnchorX() - shape.getDimensionX()/2;
                }

                if(localMouse.getY() - shape.getAnchorY() < 0){
                    fixedY = shape.getAnchorY() + shape.getDimensionY()/2;
                }else{
                    fixedY = shape.getAnchorY() - shape.getDimensionY()/2;
                }

                shape.setUserData(new double[]{fixedX,fixedY});

                event.consume();
            }
        });

        shape.setOnMouseDragged(event -> {
            if (shape.isSelected()) {
                if (getShouldTriggerInteraction()) {
                    shape.setInteractionProperty(true);
                    setShouldTriggerInteraction(false);
                }

                double[] data = (double[]) shape.getUserData();
                double fixedX = data[0];
                double fixedY = data[1];

                Point2D localPoint = shape.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                double mouseX = localPoint.getX();
                double mouseY = localPoint.getY();

                double newX = Math.min(fixedX,mouseX);
                double newY = Math.min(fixedY,mouseY);

                double newWidth = Math.abs(mouseX - fixedX);
                double newHeight = Math.abs(mouseY - fixedY);

                shape.setDimensionX(newWidth);
                shape.setDimensionY(newHeight);
                shape.setAnchorX(newX+newWidth/2);
                shape.setAnchorY(newY+newHeight/2);
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

}
