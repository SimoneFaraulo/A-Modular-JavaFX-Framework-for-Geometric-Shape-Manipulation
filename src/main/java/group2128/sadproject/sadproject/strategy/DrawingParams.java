package group2128.sadproject.sadproject.strategy;

import group2128.sadproject.sadproject.commands.CommandHistory;
import javafx.beans.property.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * A data container that holds parameters used for drawing operations.
 * <p>
 * This class encapsulates drawing parameters such as edge color, fill color,
 * canvas reference, dimensions, and command history to support interactive
 * drawing and undo functionality.
 * </p>
 */
public class DrawingParams {

    /**
     * Property representing the color used for the edges (strokes) of shapes.
     */
    private final ObjectProperty<Color> colorEdge;

    /**
     * Property representing the color used to fill the interior of shapes.
     */
    private final ObjectProperty<Color> colorFill;

    /**
     * Property holding the text content to be rendered (e.g., by a text tool).
     */
    private final StringProperty text;

    /**
     * Property holding the font size to be applied when rendering text.
     */
    private final DoubleProperty fontSize;

    /**
     * Property holding the scale factor x for the shape text
     */
    private double scaleX = 1.0;

    /**
     * Property holding the scale factor Y for the shape text
     */
    private double scaleY = 1.0;

    /**
     * The canvas where the shapes are rendered.
     */
    private AnchorPane drawingCanvas;

    /**
     * Property representing the width value of a shape.
     */
    private final DoubleProperty widthValueProperty;

    /**
     * Property representing the height value of a shape.
     */
    private final DoubleProperty heightValueProperty;

    /**
     * Property representing the rotation angle (in degrees) applied to the shape.
     */
    private final DoubleProperty rotationValueProperty;

    /**
     * Object that keeps track of the executed commands to support undo/redo operations.
     */
    private CommandHistory commandHistory;

    /**
     * Constructs an empty {@code DrawingParams} instance with default properties.
     */
    public DrawingParams() {
        this.colorEdge = new SimpleObjectProperty<>();
        this.colorFill = new SimpleObjectProperty<>();
        this.heightValueProperty = new SimpleDoubleProperty();
        this.widthValueProperty = new SimpleDoubleProperty();
        this.text = new SimpleStringProperty();
        this.fontSize = new SimpleDoubleProperty();
        this.rotationValueProperty = new SimpleDoubleProperty();
        this.rotationValueProperty.set(0.0);
    }

    /**
     * Retrieves the current text associated with these drawing parameters.
     *
     * @return the text content, or {@code null} if no text has been set
     */
    public String getText() {
        return text.get();
    }

    /**
     * Exposes the {@link StringProperty} that stores the text content so that callers can
     * observe or bind to its value.
     *
     * @return the text {@link StringProperty}
     */
    public StringProperty textProperty() {
        return text;
    }

    /**
     * Updates the text to be used during drawing operations.
     *
     * @param text the new text value to set; providing {@code null} clears the text
     */
    public void setText(String text) {
        this.text.set(text);
    }

    /**
     * Retrieves the font size that should be applied when rendering text.
     *
     * @return the font size value, or {@code null} if no size has been specified
     */
    public Double getFontSize() {
        return fontSize.get();
    }

    /**
     * Exposes the {@link DoubleProperty} storing the font size so that callers can observe
     * or bind to its value.
     *
     * @return the font size {@link DoubleProperty}
     */
    public DoubleProperty fontSizeProperty() {
        return fontSize;
    }

    /**
     * Sets the font size to be used when rendering text.
     *
     * @param fontSize the font size to apply; passing {@code null} clears the value
     */
    public void setFontSize(Double fontSize) {
        this.fontSize.set(fontSize);
    }

    /**
     * Returns the {@link CommandHistory} instance used to store executed commands.
     *
     * @return the current {@code CommandHistory}
     */
    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    /**
     * Sets the {@link CommandHistory} instance to associate with this object.
     *
     * @param commandHistory the command history to set
     */
    public void setCommandHistory(CommandHistory commandHistory) {
        this.commandHistory = commandHistory;
    }

    /**
     * Returns the property representing the shape's width value.
     *
     * @return the width {@link DoubleProperty}
     */
    public DoubleProperty getWidthValuePropertyProperty() {
        return widthValueProperty;
    }

    /**
     * Sets the shape's width value.
     *
     * @param widthValueProperty the width value to set
     */
    public void setWidthValueProperty(double widthValueProperty) {
        this.widthValueProperty.set(widthValueProperty);
    }

    /**
     * Returns the property representing the shape's height value.
     *
     * @return the height {@link DoubleProperty}
     */
    public DoubleProperty getHeightValuePropertyProperty() {
        return heightValueProperty;
    }

    /**
     * Sets the shape's height value.
     *
     * @param heightValueProperty the height value to set
     */
    public void setHeightValueProperty(double heightValueProperty) {
        this.heightValueProperty.set(heightValueProperty);
    }

    /**
     * Returns the color used for the edges of the shapes.
     *
     * @return the edge color
     */
    public Color getEdgeColor() {
        return colorEdge.getValue();
    }

    /**
     * Sets the color to be used for the edges of the shapes.
     *
     * @param colorEdge the edge color to set
     */
    public void setEdgeColor(Color colorEdge) {
        this.colorEdge.setValue(colorEdge);
    }

    /**
     * Returns the color used to fill the shapes.
     *
     * @return the fill color
     */
    public Color getFillColor() {
        return colorFill.getValue();
    }

    /**
     * Sets the color to fill the shapes.
     *
     * @param colorFill the fill color to set
     */
    public void setFillColor(Color colorFill) {
        this.colorFill.setValue(colorFill);
    }

    /**
     * Returns the property for edge color.
     *
     * @return the edge color {@link ObjectProperty}
     */
    public ObjectProperty<Color> colorEdgeProperty() {
        return colorEdge;
    }

    /**
     * Returns the property for fill color.
     *
     * @return the fill color {@link ObjectProperty}
     */
    public ObjectProperty<Color> colorFillProperty() {
        return colorFill;
    }

    /**
     * Returns the canvas on which drawing operations are performed.
     *
     * @return the {@link AnchorPane} used as the drawing canvas
     */
    public AnchorPane getDrawingCanvas() {
        return drawingCanvas;
    }

    /**
     * Sets the canvas to be used for drawing operations.
     *
     * @param drawingCanvas the {@link AnchorPane} to set as the canvas
     */
    public void setDrawingCanvas(AnchorPane drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
    }

    /**
     * Returns the scale factor x for the shape text
     * @return the scale factor x
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * Sets the scale factor x for the shape text
     * @param scaleX the scale factor x
     */
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * Returns the scale factor y for the shape text
     * @return the scale factor y
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * Sets the scale factor y for the shape text
     * @param scaleY the scale factor y
     */
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }


    /**
     * Sets the current value of the rotation property.
     *
     * @param rotationValueProperty the new rotation angle (in degrees) to assign to the property
     */
    public void setRotationValueProperty(double rotationValueProperty) {
        this.rotationValueProperty.set(rotationValueProperty);
    }

    /**
     * Returns the {@link DoubleProperty} associated with the shape's rotation angle.
     *
     * @return the {@code DoubleProperty} representing the rotation angle
     */
    public DoubleProperty getRotationValueProperty() {
        return rotationValueProperty;
    }

}