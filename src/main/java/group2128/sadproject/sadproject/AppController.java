package group2128.sadproject.sadproject;

import group2128.sadproject.sadproject.commands.*;
import group2128.sadproject.sadproject.factory.*;
import group2128.sadproject.sadproject.memento.DrawingCanvasMemento;
import group2128.sadproject.sadproject.strategy.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    /**
     * Default value for the width.
     */
    private static final double DEFAULT_WIDTH_VALUE = 100;

    /**
     * Default value for the height.
     */
    private static final double DEFAULT_HEIGHT_VALUE = 100;

    @FXML
    public MenuItem stretchBtn;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane drawingPane;

    @FXML
    private Button segmentBtn;

    @FXML
    private Button ellipseBtn;

    @FXML
    private Button rectangleBtn;

    @FXML
    private ColorPicker strokeColorSelector;

    private DrawingContext drawingContext;

    private DrawingStrategy drawingStrategy;

    private Command command;

    private Command copyCommand;

    private SelectableShape copiedShape;

    private Command pasteCommand;

    @FXML
    private ColorPicker fillColorSelector;
    @FXML
    private ContextMenu contextMenu;

    @FXML
    private TextField widthTxt;
    @FXML
    private TextField heightTxt;
    @FXML
    private MenuItem undoBtn;
    @FXML
    private MenuItem deleteBtn;
    @FXML
    private MenuItem copyBtn;
    @FXML
    private MenuItem cutBtn;

    private Point2D lastClickedPoint;
    @FXML
    private TextField textShapeTxt;
    @FXML
    private Button textBtn;
    @FXML
    private Spinner<Double> fontSizeMenu;
    @FXML
    private MenuItem resizeBtn;
    @FXML
    private MenuItem pasteBtn;

    ObjectProperty<SelectableShape> copiedShapeProperty;
    @FXML
    private Button polygonBtn;
    @FXML
    private MenuItem foregroundBtn;
    @FXML
    private MenuItem backgroundBtn;

    @FXML
    private Slider zoomSlider;
    @FXML
    private Label zoomLbl;

    @FXML
    private CheckBox gridCB;

    private List<Integer> gridBlockSizes;

    private int currentGridSizeIndex = 0;
    @FXML
    private Slider gridBlockSizeSlider;
    @FXML
    private AnchorPane gridPane;
    @FXML
    private MenuItem flipHorizontalBtn;
    @FXML
    private MenuItem flipVerticalBtn;
    @FXML
    private TextField angleTxt;


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up initial states, listeners,
     * and binds GUI components with their respective actions.
     */
    @FXML
    private void initialize() {
        // Initialize the application context
        drawingContext = new DrawingContext(drawingPane);
        drawingStrategy = new IdleStrategy();
        drawingContext.setStrategyMode(drawingStrategy);

        // Ensure the scroll pane is anchored to all sides of the drawing pane
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);

        // Setting default values for the color pickers
        strokeColorSelector.setValue(Color.DEEPSKYBLUE);
        fillColorSelector.setValue(Color.WHITE);
        Bindings.bindBidirectional(drawingContext.getDrawingParams().colorEdgeProperty(), strokeColorSelector.valueProperty());
        Bindings.bindBidirectional(drawingContext.getDrawingParams().colorFillProperty(), fillColorSelector.valueProperty());
        drawingContext.getDrawingParams().setFillColor(fillColorSelector.getValue());
        drawingContext.getDrawingParams().setEdgeColor(strokeColorSelector.getValue());

        //Setting of contextMenu
        contextMenu.setOpacity(0);

        // Setting the binding with the DoubleProperty for Height e Width
        widthTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
        heightTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));

        //BINDINGS BETWEEN THE widthTxt/heightTxt FIELD AND THE height AND width PARAMS
        Bindings.bindBidirectional(widthTxt.textProperty(), drawingContext.getDrawingParams().getWidthValuePropertyProperty(), new NumberStringConverter(java.util.Locale.US));
        Bindings.bindBidirectional(heightTxt.textProperty(), drawingContext.getDrawingParams().getHeightValuePropertyProperty(), new NumberStringConverter(java.util.Locale.US));
        widthTxt.setText(Double.toString(DEFAULT_WIDTH_VALUE));
        heightTxt.setText(Double.toString(DEFAULT_HEIGHT_VALUE));

        //BINDINGS BETWEEN THE CommandHistory PARAM AND THE Undo BUTTON
        drawingContext.getDrawingParams().setCommandHistory(new CommandHistory());
        undoBtn.disableProperty().bind(drawingContext.getDrawingParams().getCommandHistory().emptyBinding());
        copyCommand = new CopyCommand();
        pasteCommand = new PasteCommand();

        drawingPane.setOnMousePressed(mouseEvent -> {
            lastClickedPoint = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
        });

        //BINDINGS BETWEEN THE TextField FOR THE TEXT INSERT AND THE textBtn AND THE text PARAM
        Bindings.bindBidirectional(textShapeTxt.textProperty(), drawingContext.getDrawingParams().textProperty());
        textBtn.disableProperty().bind(
                textShapeTxt.textProperty().isEmpty()
        );

        //BINDING BETWEEN FONT SIZE SPINNER AND FontSize ATTRIBUTE IN THE DrawingParams
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(8, 72, 8);
        fontSizeMenu.setValueFactory(valueFactory);
        DoubleProperty modelFontSize = drawingContext.getDrawingParams().fontSizeProperty();
        ObjectProperty<Double> spinnerValue = valueFactory.valueProperty();
        spinnerValue.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(modelFontSize.get())) {
                modelFontSize.set(newVal);
            }
        });

        modelFontSize.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(spinnerValue.get())) {
                spinnerValue.set(newVal.doubleValue());
            }
        });
        TextField editor = fontSizeMenu.getEditor();

        editor.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        modelFontSize.set(spinnerValue.get());


        //BINDING THE pasteBtn WITH THE copiedShape
        copiedShapeProperty = new SimpleObjectProperty<>();
        copiedShapeProperty.set(this.copiedShape);
        pasteBtn.disableProperty().bind(copiedShapeProperty.isNull());

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double scale = newVal.doubleValue();

            zoomLbl.setText(String.valueOf((int) scale * 10) + " %");

            drawingPane.setScaleX(scale);
            drawingPane.setScaleY(scale);

        });

        //INITIALIZATION OF THE GRID
        gridBlockSizes = new ArrayList<>();
        gridBlockSizes.add(5);
        gridBlockSizes.add(10);
        gridBlockSizes.add(20);
        gridBlockSizes.add(50);
        gridBlockSizes.add(100);

        gridCB.selectedProperty().addListener((obs, oldVal, newVal) -> {
            showHideGrid(null);
        });

        gridBlockSizeSlider.disableProperty().bind(gridCB.selectedProperty().not());

        gridBlockSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentGridSizeIndex = newVal.intValue();
            updateGridStyle();
        });

        fontSizeMenu.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                FontSizeCommand cmd = new FontSizeCommand();
                cmd.setDrawingCanvas(drawingPane);
                cmd.saveBackup();
                cmd.setSelectedShape(drawingContext.getSelectedShape());
                cmd.setFontSize(newVal);
                cmd.execute();
                drawingContext.getDrawingParams().getCommandHistory().push(cmd);
            }
        });

        angleTxt.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        angleTxt.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = angleTxt.getText().trim();
                if (text.isEmpty()) {
                    angleTxt.setText("0");
                }
            }
        });

        Bindings.bindBidirectional(angleTxt.textProperty(), drawingContext.getDrawingParams().getRotationValueProperty(), new NumberStringConverter(java.util.Locale.US));
        drawingContext.getDrawingParams().setRotationValueProperty(Double.parseDouble(angleTxt.getText()));
        angleTxt.setText("0");
    }

    /**
     * Handles the creation of a new drawing pane.
     * <p>
     * If the current drawing pane is not empty, it should first prompt the user
     * to save their work (this functionality is not yet implemented).
     * Then it clears all existing elements from the drawing pane,
     * effectively starting with a blank canvas.
     *
     * @param actionEvent the event triggered by the user action (e.g., clicking "New")
     */
    @FXML
    public void createNewPane(ActionEvent actionEvent) {
        command = new NewDrawingCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
    }

    /**
     * Handles the selection of the "Ellipse" drawing tool.
     *
     * @param actionEvent the event triggered by clicking the ellipse tool button
     */
    @FXML
    public void onEllipseButtonAction(ActionEvent actionEvent) {
        resetSelectorsStyle();
        drawingStrategy.onExit(drawingContext.getDrawingParams());
        if (drawingStrategy.getClass().equals(EllipseDrawingStrategy.class)) {
            drawingStrategy = new IdleStrategy();
            drawingContext.setStrategyMode(drawingStrategy);
        } else {
            ellipseBtn.setStyle("-fx-background-color: rgba(153, 203, 255, 0.5);");
            drawingStrategy = new EllipseDrawingStrategy();
            drawingContext.setStrategyMode(drawingStrategy);
        }
    }

    /**
     * Handles the selection of the "Rectangle" drawing tool.
     *
     * @param actionEvent the event triggered by clicking the rectangle tool button
     */
    @FXML
    public void onRectangleButtonAction(ActionEvent actionEvent) {
        resetSelectorsStyle();
        drawingStrategy.onExit(drawingContext.getDrawingParams());
        if (drawingStrategy.getClass().equals(RectangleDrawingStrategy.class)) {
            drawingStrategy = new IdleStrategy();
        } else {
            rectangleBtn.setStyle("-fx-background-color: rgba(153, 203, 255, 0.5);");
            drawingStrategy = new RectangleDrawingStrategy();
        }
        drawingContext.setStrategyMode(drawingStrategy);
    }

    /**
     * Handles the selection of the "Segment" drawing tool.
     *
     * @param actionEvent the event triggered by clicking the segment tool button
     */
    @FXML
    public void onSegmentButtonAction(ActionEvent actionEvent) {
        resetSelectorsStyle();
        if(drawingStrategy.getClass().equals(SegmentDrawingStrategy.class)) {
            drawingStrategy.onExit(drawingContext.getDrawingParams());
            drawingStrategy = new IdleStrategy();
            drawingContext.setStrategyMode(drawingStrategy);
        }else{
            segmentBtn.setStyle("-fx-background-color: rgba(153, 203, 255, 0.5);");
            drawingStrategy = new SegmentDrawingStrategy();
            drawingContext.setStrategyMode(drawingStrategy);
        }
    }

    @FXML
    public void onTextButtonAction(ActionEvent actionEvent) {
        resetSelectorsStyle();
        drawingStrategy.onExit(drawingContext.getDrawingParams());
        if (drawingStrategy.getClass().equals(TextDrawingStrategy.class)) {
            drawingStrategy = new IdleStrategy();
        } else {
            textBtn.setStyle("-fx-background-color: rgba(153, 203, 255, 0.5);");
            drawingStrategy = new TextDrawingStrategy();
        }
        drawingContext.setStrategyMode(drawingStrategy);
    }

    /**
     * Resets the visual style of the shape selector buttons to their default background color.
     * <p>
     * This method sets a semi-transparent light gray background color for the
     * segment, rectangle, and ellipse buttons, giving them a consistent unselected appearance.
     */
    private void resetSelectorsStyle() {
        segmentBtn.setStyle("-fx-background-color: rgba(245, 245, 245, 0.5);");
        rectangleBtn.setStyle("-fx-background-color: rgba(245, 245, 245, 0.5);");
        ellipseBtn.setStyle("-fx-background-color: rgba(245, 245, 245, 0.5);");
        textBtn.setStyle("-fx-background-color: rgba(245, 245, 245, 0.5);");
        polygonBtn.setStyle("-fx-background-color: rgba(245, 245, 245, 0.5);");
    }

    /**
     * Handles mouse click events on the drawing pane.
     * <p>
     * This method is triggered when the user clicks on the drawing area.
     * It passes the X and Y coordinates of the mouse click to the drawing context
     * for further processing.
     * </p>
     *
     * @param event the mouse event containing the click coordinates.
     */
    @FXML
    public void onDrawingPaneClicked(MouseEvent event) {
        SelectableShape shape = drawingContext.getSelectedShape();
        if(shape != null) {
            shape.initDrag();
        }
        contextMenu.setOpacity(0);
        if (event.getButton() == MouseButton.PRIMARY) {

            if(event.getClickCount() == 2){
                drawingContext.completeShape();
            }else {
                drawingContext.handleClick(event.getX(), event.getY());
            }
        }else if(event.getButton() == MouseButton.SECONDARY && drawingContext.getSelectedShape() != null) {
            contextMenu.setOpacity(1);
            changeVisibilityContextMenu(true);
        }else if(event.getButton() == MouseButton.SECONDARY && drawingContext.getSelectedShape() == null){
            contextMenu.setOpacity(1);
            changeVisibilityContextMenu(false);
        }
    }

    /**
     * Sets the visibility of various context menu buttons based on the provided flag.
     * <p>
     * This method enables or disables the visibility of common editing buttons such as delete,
     * copy, resize, stretch, cut, and color options in the context menu.
     * It is typically used to show or hide the menu based on the selection state of a shape or user interaction.
     *
     * @param bool {@code true} to make the buttons visible, {@code false} to hide them
     */
    private void changeVisibilityContextMenu(boolean bool){
        deleteBtn.setVisible(bool);
        copyBtn.setVisible(bool);
        resizeBtn.setVisible(bool);
        stretchBtn.setVisible(bool);
        cutBtn.setVisible(bool);
        foregroundBtn.setVisible(bool);
        backgroundBtn.setVisible(bool);
        flipHorizontalBtn.setVisible(bool);
        flipVerticalBtn.setVisible(bool);
    }

    /**
     * Returns the current active drawing strategy used by the application.
     * <p>
     * The drawing strategy defines the behavior for handling user interactions
     * with the drawing pane, such as creating ellipses, rectangles, or segments.
     * This method can be used, for example, in unit tests to verify which strategy
     * is currently set in the controller.
     *
     * @return the currently active {@link DrawingStrategy} instance
     */
    public DrawingStrategy getDrawingStrategy() {
        return this.drawingStrategy;
    }

    /**
     * Returns the current {@link DrawingContext} used by the controller.
     * <p>
     * The drawing context holds the state and configuration related to the drawing canvas,
     * including active strategy and parameters like stroke and fill color.
     *
     * @return the current DrawingContext instance
     */
    public DrawingContext getDrawingContext() {
        return this.drawingContext;
    }

    /**
     * Handles the color selection event from the stroke color picker.
     * <p>
     * This method updates the stroke (edge) color in the {@link DrawingContext}'s drawing parameters
     * based on the color currently selected in the {@link ColorPicker}.
     *
     * @param actionEvent the event triggered when a new color is selected
     */
    @FXML
    public void onStrokeColorPicker(ActionEvent actionEvent) {
        drawingContext.getDrawingParams().setEdgeColor(strokeColorSelector.getValue());
        if (drawingContext.getSelectedShape() != null) {
            SelectableShape shape = drawingContext.getSelectedShape();
            command = new ChangeEdgeColorCommand();
            command.setDrawingCanvas(this.drawingPane);
            command.saveBackup();
            ((ChangeEdgeColorCommand) command).setSelectedShape(shape);
            ((ChangeEdgeColorCommand) command).setSelectedColor(strokeColorSelector.getValue());
            command.execute();
            drawingContext.getDrawingParams().getCommandHistory().push(command);
        }
    }


    /**
     * Handles the save action triggered by the user in the UI.
     * <p>
     * This method initializes a {@link SaveCommand}, sets the current drawing canvas
     * and stage, and executes the command to perform the save operation.
     * The current canvas content is exported to a JSON file selected by the user.
     * </p>
     *
     * @param actionEvent the event that triggered the action, typically from a menu item or button
     */
    @FXML
    public void savePaint(ActionEvent actionEvent) {
        command = new SaveCommand();
        SaveCommand saveCommand = (SaveCommand) command;
        saveCommand.setDrawingCanvas(drawingPane);
        saveCommand.setStage((Stage) drawingPane.getScene().getWindow());
        saveCommand.execute();
    }

    /**
     * Handles the loading of a saved drawing onto the canvas.
     * <p>
     * This method first clears the current drawing pane by executing a {@link NewDrawingCommand},
     * then it loads a previously saved drawing from a JSON file using the {@link LoadCommand}.
     * The {@code LoadCommand} displays a file chooser dialog to select the JSON file and
     * adds the corresponding shapes to the drawing pane.
     *
     * @param actionEvent the event triggered by the user's interaction (e.g., clicking the "Load" button)
     */
    @FXML
    public void loadPaint(ActionEvent actionEvent) {
        createNewPane(null);
        command = new LoadCommand();
        LoadCommand loadCommand = (LoadCommand) command;
        loadCommand.setDrawingCanvas(drawingPane);
        loadCommand.setCommandHistory(drawingContext.getDrawingParams().getCommandHistory());
        loadCommand.setStage((Stage) drawingPane.getScene().getWindow());
        loadCommand.execute();
    }

    /**
     * Handles the action event triggered when the user selects a new fill color
     * from the {@code ColorPicker} UI component.
     *
     * <p>This method updates the fill color parameter in the {@code DrawingContext}
     * to match the newly selected color from {@code fillColorSelector}.</p>
     *
     * @param actionEvent the {@link ActionEvent} triggered by interacting with the fill color picker
     */
    @FXML
    public void onFillColorPicker(ActionEvent actionEvent) {
        drawingContext.getDrawingParams().setFillColor(fillColorSelector.getValue());
        if (drawingContext.getSelectedShape() != null) {
            SelectableShape shape = drawingContext.getSelectedShape();
            command = new ChangeFillColorCommand();
            command.setDrawingCanvas(this.drawingPane);
            command.saveBackup();
            ((ChangeFillColorCommand) command).setSelectedShape(shape);
            ((ChangeFillColorCommand) command).setSelectedColor(fillColorSelector.getValue());
            command.execute();
            drawingContext.getDrawingParams().getCommandHistory().push(command);
        }
    }

    /**
     * Handles the action triggered by clicking the "Resize" button.
     * <p>
     * This method checks whether a shape is currently selected in the drawing context.
     * If a shape is selected and a corresponding resize command can be created,
     * the command is configured with the drawing canvas and executed.
     * </p>
     *
     * @param actionEvent the event triggered by clicking the resize button
     */
    @FXML
    public void onResizeButtonAction(ActionEvent actionEvent) {
        SelectableShape shape = drawingContext.getSelectedShape();
        if (shape == null) {
            return;
        }

        command = createResizeCommand(shape);
        if (command != null) {

            if (command instanceof ResizeCommand) {
                ResizeCommand resizeCommand = (ResizeCommand) command;
                resizeCommand.setDrawingCanvas(drawingPane);
                resizeCommand.setShape(shape);
                resizeCommand.execute();
            }
        }
    }

    /**
     * Creates an appropriate {@link Command} instance for resizing the given shape.
     * <p>
     * This method determines the type of the selected shape and returns the
     * corresponding resize command implementation:
     * <ul>
     *     <li>{@link ResizeEllipseCommand} for {@link EllipseShape}</li>
     *     <li>{@link ResizeRectangleCommand} for {@link RectangleShape}</li>
     *     <li>{@link ResizeSegmentCommand} for {@link SegmentShape}</li>
     *     <li>{@link ResizePolygonCommand} for {@link PolygonShape}</li>
     *     <li>{@link ResizeTextCommand} for {@link TextShape}</li>
     * </ul>
     * If the shape type is unrecognized, it returns {@code null}.
     * </p>
     *
     * @param shape the selected shape to resize
     * @return a resize command appropriate for the shape type, or {@code null} if unsupported
     */
    private Command createResizeCommand(SelectableShape shape) {
        if (shape instanceof EllipseShape) {
            return new ResizeEllipseCommand();
        } else if (shape instanceof RectangleShape) {
            return new ResizeRectangleCommand();
        } else if (shape instanceof SegmentShape) {
            return new ResizeSegmentCommand();
        } else if (shape instanceof TextShape) {
            return new ResizeTextCommand();
        } else if (shape instanceof PolygonShape) {
            return new ResizePolygonCommand();
        }
        return null;
    }


    /**
     * Handles the deletion of the currently selected shape from the drawing pane.
     * <p>
     * This method is triggered when the user clicks the "Delete" button in the UI.
     * It initializes a {@link DeleteCommand}, sets the drawing canvas and the selected shape,
     * and executes the command to remove the shape from the canvas.
     *
     * @param actionEvent the event triggered by clicking the delete button
     */
    @FXML
    public void onDeleteButtonAction(ActionEvent actionEvent) {
        command = new DeleteCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        ((DeleteCommand) command).setSelectedShape(drawingContext.getSelectedShape());
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
        drawingContext.setSelectedShape(null);
    }

    /**
     * Handles the action triggered by the "Undo" button.
     *
     * <p>This method retrieves the last executed {@link Command} from the {@link CommandHistory},
     * obtains its associated {@link AnchorPane} representing the previous drawing state,
     * and restores it to the current drawing pane.</p>
     *
     * @param actionEvent the {@link ActionEvent} triggered by clicking the Undo button.
     */
    @FXML
    public void onUndoButton(ActionEvent actionEvent) {
        Command previousCommand = drawingContext.getDrawingParams().getCommandHistory().pop();
        previousCommand.undo();
        DrawingCanvasMemento backup = previousCommand.getDrawingCanvasMemento();
        AnchorPane previousCanvas = backup.getDrawingCanvas();
        List<Node> nodesCopy = new ArrayList<>(previousCanvas.getChildren());
        addInteractionListener(nodesCopy);
    }

    /**
     * Adds an interaction listener to a list of {@link Node} elements that are expected to be instances of {@link SelectableShape}.
     * <p>
     * For each shape, a listener is added to its {@code interactionProperty}. When this property becomes {@code true},
     * an {@link InteractionCommand} is executed on a cloned version of the drawing pane.
     * The command is then pushed to the command history for undo/redo support.
     * After execution, the {@code interactionProperty} is reset to {@code false} to avoid repeated triggering.
     * </p>
     *
     * @param nodes a list of {@link Node} objects that represent selectable shapes in the drawing pane
     * @throws ClassCastException if any of the nodes is not an instance of {@link SelectableShape}
     */
    private void addInteractionListener(List<Node> nodes) {
        for (Node node : nodes) {
            SelectableShape shape = (SelectableShape) node;
            shape.interactionPropertyProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    InteractionCommand intCommand = new InteractionCommand();
                    intCommand.setDrawingCanvas(drawingPane);
                    intCommand.saveBackup();
                    intCommand.execute();
                    drawingContext.getDrawingParams().getCommandHistory().push(intCommand);
                    shape.setInteractionProperty(false);
                }
            });
        }
    }

    /**
     * Handles the action triggered when the "Copy" button is pressed.
     * <p>
     * This method creates a new {@link CopyCommand}, assigns the current drawing canvas
     * and the selected shape from the {@code drawingContext}, and then executes the command.
     * The command is responsible for copying the selected shape.
     * </p>
     *
     * @param actionEvent the event triggered by pressing the copy button
     */
    @FXML
    public void onCopyButtonAction(ActionEvent actionEvent) {
        copyCommand.setDrawingCanvas(drawingPane);
        ((CopyCommand) copyCommand).setSelectedShape(drawingContext.getSelectedShape());
        copiedShapeProperty.set(drawingContext.getSelectedShape());
        copyCommand.execute();
        this.copiedShape = ((CopyCommand) copyCommand).getCopiedShape();
    }

    /**
     * Handles the "Cut" action triggered by the user.
     * This method copies the currently selected shape and then deletes it.
     * It is functionally equivalent to calling {@code onCopyButtonAction} followed by {@code onDeleteButtonAction}.
     *
     * @param actionEvent the event triggered by clicking the "Cut" button or menu item
     */
    @FXML
    public void onCutButtonAction(ActionEvent actionEvent) {
        onCopyButtonAction(null);
        onDeleteButtonAction(null);
    }

    /**
     * Returns the current {@code copyCommand} instance.
     * This command is responsible for copying the selected shapes or objects.
     *
     * @return the {@code Command} used to perform a copy operation
     */
    public Command getCopyCommand(){
        return copyCommand;
    }

    /**
     * Handles the action triggered when the paste button is clicked.
     * <p>
     * If a shape has been previously copied (i.e., {@code copiedShape} is not null),
     * this method creates a new {@link PasteCommand}, clones the current drawing pane,
     * sets the necessary context for the command (including the copied shape and
     * paste coordinates), and executes the paste operation at the location of the
     * last mouse click.
     * <p>
     * The command is also pushed onto the command history stack to support undo/redo functionality.
     *
     * @param actionEvent the event object representing the user's action
     */
    @FXML
    public void onPasteButtonAction(ActionEvent actionEvent) {
        if(this.copiedShape != null){
            //CREATING THE PARAMS FOR THE PASTE
            DrawingParams params = new DrawingParams();
            params.setCommandHistory(drawingContext.getDrawingParams().getCommandHistory());
            params.setWidthValueProperty(this.copiedShape.getDimensionX());
            params.setHeightValueProperty(this.copiedShape.getDimensionY());
            params.setEdgeColor(this.copiedShape.getEdgeColor());
            params.setFillColor(this.copiedShape.getFillColor());
            params.setDrawingCanvas(drawingPane);
            params.setScaleX(((Node)this.copiedShape).getScaleX());
            params.setScaleY(((Node)this.copiedShape).getScaleY());
            params.setRotationValueProperty(this.copiedShape.getRotation());

            if (this.copiedShape instanceof TextShape) {
                params.setText(((TextShape) this.copiedShape).getText());
                params.setFontSize(((TextShape) this.copiedShape).getFontSize());
            }

            //EXECUTING THE PASTE
            pasteCommand.setDrawingCanvas(drawingPane);
            ((PasteCommand) pasteCommand).setDrawingParams(params);
            ((PasteCommand) pasteCommand).setPasteX(lastClickedPoint.getX());
            ((PasteCommand) pasteCommand).setPasteY(lastClickedPoint.getY());
            ((PasteCommand) pasteCommand).setShape(this.copiedShape);
            pasteCommand.execute();
        }
    }

    /**
     * Returns the currently copied shape, if any.
     * <p>
     * This shape may be used for paste operations or duplication within the drawing canvas.
     * </p>
     *
     * @return the {@link SelectableShape} that was most recently copied, or {@code null} if none
     */
    public SelectableShape getCopiedShape() {
        return copiedShape;
    }

    /**
     * Handles the action triggered when the polygon tool button is clicked.
     * <p>
     * This method toggles the current drawing strategy between {@link PolygonDrawingStrategy}
     * and {@link IdleStrategy}. If the polygon tool is already active, clicking the button again
     * will deactivate it and switch to idle mode. Otherwise, it activates the polygon drawing mode
     * and visually updates the button's style to indicate its active state.
     * </p>
     *
     * @param actionEvent the {@link ActionEvent} triggered by the button click
     */
    @FXML
    public void onPolygonButtonAction(ActionEvent actionEvent) {
        resetSelectorsStyle();
        drawingStrategy.onExit(drawingContext.getDrawingParams());
        if (drawingStrategy.getClass().equals(PolygonDrawingStrategy.class)) {
            drawingStrategy = new IdleStrategy();
        } else {
            polygonBtn.setStyle("-fx-background-color: rgba(153, 203, 255, 0.5);");
            drawingStrategy = new PolygonDrawingStrategy();
        }
        drawingContext.setStrategyMode(drawingStrategy);
    }

    /**
     * Handles the action triggered when the "Send to Background" button is clicked.
     * <p>
     * This method creates and configures a {@link BackgroundCommand} that moves the selected shape
     * to the back of the drawing canvas. It first clones the current drawing pane for history tracking,
     * sets the selected shape in the command, and pushes the command onto the command history stack
     * for undo/redo functionality. Finally, it applies the command to the actual drawing pane.
     *
     * @param actionEvent the event triggered by the button click
     */
    @FXML
    public void onBackgroundButtonAction(ActionEvent actionEvent) {
        command = new BackgroundCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        ((BackgroundCommand)command).setSelectedShape(drawingContext.getSelectedShape());
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
    }

    /**
     * Handles the action triggered when the "Bring to Foreground" button is clicked.
     * <p>
     * This method creates and configures a {@link ForegroundCommand} that brings the selected shape
     * to the front of the drawing canvas. It clones the current drawing pane for history tracking,
     * sets the selected shape in the command, and stores the command in the command history stack.
     * Then it executes the command on the actual drawing pane.
     *
     * @param actionEvent the event triggered by the button click
     */
    @FXML
    public void onForegroundButtonAction(ActionEvent actionEvent) {
        command = new ForegroundCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        ((ForegroundCommand)command).setSelectedShape(drawingContext.getSelectedShape());
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
    }

    /**
     * Toggles the visibility of the background grid on the grid pane.
     * <p>
     * If a grid-related CSS class (e.g., "grid-10") is currently applied to the pane,
     * the method will remove it to hide the grid. If no such class is present,
     * the method will reapply the CSS class corresponding to the currently selected grid size,
     * thus making the grid visible again.
     *
     * @param actionEvent the event that triggered the grid visibility toggle (e.g., button click)
     */
    private void showHideGrid(ActionEvent actionEvent) {
        boolean isGridVisible = gridPane.getStyleClass().stream()
                .anyMatch(c -> c.startsWith("grid-"));

        if (isGridVisible) {
            gridPane.getStyleClass().removeIf(c -> c.startsWith("grid-"));
        } else {
            updateGridStyle();
        }
    }

    /**
     * Applies the appropriate CSS style class to the grid pane based on the current grid size.
     * <p>
     * First removes any existing "grid-*" classes, then adds the one that corresponds
     * to the currently selected grid size, such as "grid-10" or "grid-50".
     */
    private void updateGridStyle() {
        gridPane.getStyleClass().removeIf(c -> c.startsWith("grid-"));
        String newStyleClass = "grid-" + gridBlockSizes.get(currentGridSizeIndex);
        gridPane.getStyleClass().add(newStyleClass);
    }



    @FXML
    public void onStretchButtonAction(ActionEvent actionEvent) {
        SelectableShape shape = drawingContext.getSelectedShape();
        if (shape == null) return;

        command = createStretchCommand(shape);
        if (command != null) {

            if (command instanceof StretchCommand) {
                StretchCommand stretchCommand = (StretchCommand) command;
                stretchCommand.setDrawingCanvas(drawingPane);
                stretchCommand.setShape(shape);
                stretchCommand.execute();
            }

            if (shape instanceof SegmentShape){
                ResizeSegmentCommand resizeSegmentCommand = (ResizeSegmentCommand) command;
                resizeSegmentCommand.setDrawingCanvas(drawingPane);
                resizeSegmentCommand.setShape(shape);
                resizeSegmentCommand.execute();
            }
        }
    }

    /**
     * Creates an appropriate {@link Command} instance for resizing the given shape.
     * <p>
     * This method determines the type of the selected shape and returns the
     * corresponding resize command implementation:
     * <ul>
     *     <li>{@link StretchRectangleCommand} for {@link RectangleShape}</li>
     *     <li>{@link StretchEllipseCommand} for {@link EllipseShape}</li>
     *     <li>{@link ResizeSegmentCommand} for {@link SegmentShape}</li>
     *     <li>{@link StretchPolygonCommand} for {@link PolygonShape}</li>
     *     <li>{@link StretchTextCommand} for {@link TextShape}</li>
     * </ul>
     * If the shape type is unrecognized, it returns {@code null}.
     * </p>
     *
     * @param shape the selected shape to resize
     * @return a resize command appropriate for the shape type, or {@code null} if unsupported
     */
    private Command createStretchCommand(SelectableShape shape) {
        if (shape instanceof EllipseShape) {
            return new StretchEllipseCommand();
        } else if (shape instanceof RectangleShape) {
            return new StretchRectangleCommand();
        } else if (shape instanceof SegmentShape) {
            return new ResizeSegmentCommand();
        } else if (shape instanceof TextShape) {
            return new StretchTextCommand();
        } else if (shape instanceof PolygonShape) {
            return new StretchPolygonCommand();
        }
        return null;
    }

    /**
     * Handles the action when the Flip Horizontal button is pressed.
     * <p>
     * This method saves the current state of the drawing pane to enable undo functionality,
     * then applies a horizontal flip transformation to the currently selected shape.
     * </p>
     *
     * @param actionEvent the event triggered by pressing the Flip Horizontal button
     */
    @FXML
    public void onFlipHorizontalButtonAction(ActionEvent actionEvent) {
        command = new FlipHorizontalCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        ((FlipHorizontalCommand) command).setSelectedShape(drawingContext.getSelectedShape());
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
    }

    /**
     * Handles the action when the Flip Vertical button is pressed.
     * <p>
     * This method saves the current state of the drawing pane to enable undo functionality,
     * then applies a vertical flip transformation to the currently selected shape.
     * </p>
     *
     * @param actionEvent the event triggered by pressing the Flip Vertical button
     */
    @FXML
    public void onFlipVerticalButtonAction(ActionEvent actionEvent) {
        command = new FlipVerticalCommand();
        command.setDrawingCanvas(drawingPane);
        command.saveBackup();
        ((FlipVerticalCommand) command).setSelectedShape(drawingContext.getSelectedShape());
        command.execute();
        drawingContext.getDrawingParams().getCommandHistory().push(command);
    }

    /**
     * Handles the action triggered when the user inputs a new rotation angle and confirms it.
     * <p>
     * This method retrieves the angle from the text field and, if it is within the valid range (0, 180),
     * creates a {@link RotateCommand} to save the current state of the selected shape before rotation.
     * The current state is cloned and stored in the command history for undo/redo functionality.
     * Then, a new {@link RotateCommand} is configured with the selected shape and desired angle,
     * and executed to apply the rotation.
     * </p>
     *
     * @param actionEvent the action event triggered by the UI (e.g., clicking a "Rotate" button)
     */
    @FXML
    public void onAngleAction(ActionEvent actionEvent) {

        if (drawingContext.getSelectedShape() != null && !angleTxt.getText().isEmpty()) {
            double angle = Double.parseDouble(angleTxt.getText());
            command = new RotateCommand();
            command.setDrawingCanvas(drawingPane);
            command.saveBackup();
            ((RotateCommand)command).setSelectedShape(drawingContext.getSelectedShape());
            ((RotateCommand)command).setAngle(angle);
            command.execute();
            drawingContext.getDrawingParams().getCommandHistory().push(command);
        }
    }
}