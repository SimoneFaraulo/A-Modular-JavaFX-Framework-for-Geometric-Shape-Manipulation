package group2128.sadproject.sadproject;

import group2128.sadproject.sadproject.commands.CopyCommand;
import group2128.sadproject.sadproject.commands.PasteCommand;
import group2128.sadproject.sadproject.factory.*;
import group2128.sadproject.sadproject.strategy.*;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
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
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the AppController class.
 * These tests simulate user interactions and verify UI behavior and strategy changes.
 */
@ExtendWith(ApplicationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppControllerTest {

    private AppController controller;

    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/group2128/sadproject/sadproject/hello-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void resetAppState(FxRobot robot) {
        robot.interact(() -> controller.createNewPane(null));
    }

    /**
     * Tests that clicking the ellipse button toggles between EllipseDrawingStrategy and IdleStrategy.
     */
    @Test
    void testEllipseButtonTogglesStrategy(FxRobot robot) {
        Button ellipseBtn = robot.lookup("#ellipseBtn").queryButton();
        assertThat(ellipseBtn).isNotNull();

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests that clicking the segment button activates SegmentDrawingStrategy.
     */
    @Test
    void testSegmentButtonChangesStrategy(FxRobot robot) {
        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests that clicking the polygon button activates PolygonDrawingStrategy.
     */
    @Test
    void testPolygonButtonTogglesStrategy(FxRobot robot){
        Button polygonBtn = robot.lookup("#polygonBtn").queryButton();
        assertThat(polygonBtn).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests that clicking the rectangle button activates RectangleDrawingStrategy.
     */
    @Test
    void testRectangleButtonChangesStrategy(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests transition from Ellipse -> Rectangle -> Segment -> Polygon ->Idle.
     */
    @Test
    void testEllipseToRectangleToPolygon(FxRobot robot) {
        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests transition Polygon -> Rectangle -> Ellipse -> Segment.
     */
    @Test
    void testPolygonToEllipseToSegment(FxRobot robot) {

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);
    }


    /**
     * Tests transition Text -> Rectangle -> Text -> Segment -> Text -> Segment ->Text.
     */
    @Test
    void testTextToEllipseToRectangleToEllipse(FxRobot robot) {
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        robot.interact(() -> {
            textField.setText("testTextToEllipseToRectangleToEllipse");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
    }

    /**
     * Tests clicking each button and returning to Idle by clicking again.
     */
    @Test
    void testEachButtonToggleToIdle(FxRobot robot) {
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);
        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);
        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);
        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.interact(() -> {
            textField.setText("testEachButtonToggleToIdle");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);
        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests rapid sequence: Ellipse -> Ellipse (Idle) -> Segment -> Rectangle -> Rectangle -> Text ->(Idle)
     */
    @Test
    void testRapidToggleAndSwitch(FxRobot robot) {
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);

        robot.interact(() -> {
            textField.setText("testEachButtonToggleToIdle");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);
        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);


        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
    }

    /**
     * Tests that selecting a stroke color using the ColorPicker updates the drawing context's edge color.
     * <p>
     * This test simulates setting the ColorPicker to red and ensures that:
     * <ul>
     *   <li>The controller is in IdleStrategy mode (default state).</li>
     *   <li>The selected edge color in the drawing context is correctly set to red.</li>
     * </ul>
     *
     * @param robot TestFX robot used to simulate user interaction
     */
    @Test
    void testStrokeColorPickerUpdatesEdgeColor(FxRobot robot) {
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.RED);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getDrawingParams().getEdgeColor()).isEqualTo(javafx.scene.paint.Color.RED);
    }

    /**
     * Tests that changing the stroke color multiple times correctly updates the internal drawing state.
     * <p>
     * This test sets the ColorPicker to blue, verifies the state, then changes it to green
     * and ensures the new color is reflected in the drawing context.
     *
     * @param robot TestFX robot used to simulate user interaction
     */
    @Test
    void testChangingStrokeColorReflectsInState(FxRobot robot) {
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(strokePicker).isNotNull();

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.BLUE);
            controller.onStrokeColorPicker(new ActionEvent());
        });
        assertThat(controller.getDrawingContext().getDrawingParams().getEdgeColor()).isEqualTo(javafx.scene.paint.Color.BLUE);

        robot.interact(() -> {
            strokePicker.setValue(javafx.scene.paint.Color.GREEN);
            controller.onStrokeColorPicker(new ActionEvent());
        });
        assertThat(controller.getDrawingContext().getDrawingParams().getEdgeColor()).isEqualTo(javafx.scene.paint.Color.GREEN);
    }

    /**
     * Tests that selecting a new fill color using the {@code ColorPicker}
     * updates the fill color in the controller's drawing context.
     *
     * <p>This test verifies that when a user interacts with the fill color picker
     * and selects a new color, the controller correctly updates the fill color
     * in the drawing parameters. It also ensures that the current drawing
     * strategy is {@link IdleStrategy}.</p>
     *
     * @param robot the {@link FxRobot} instance used to simulate UI interaction
     */
    @Test
    void testFillColorPickerUpdatesFillColor(FxRobot robot) {
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
            
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getDrawingParams().getFillColor()).isEqualTo(Color.YELLOW);
    }

    /**
     * Tests that multiple changes to the fill color using the {@code ColorPicker}
     * are correctly reflected in the drawing context state.
     *
     * <p>This test simulates a user changing the fill color twice, checking
     * after each change that the selected color is correctly propagated to
     * the controller's drawing parameters.</p>
     *
     * @param robot the {@link FxRobot} instance used to simulate UI interaction
     */
    @Test
    void testChangingFillColorReflectsInState(FxRobot robot) {
        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
             
            controller.onFillColorPicker(new ActionEvent());
        });
        assertThat(controller.getDrawingContext().getDrawingParams().getFillColor()).isEqualTo(Color.GREEN);

        robot.interact(() -> {
            fillPicker.setValue(Color.PURPLE);
             
            controller.onFillColorPicker(new ActionEvent());
        });
        assertThat(controller.getDrawingContext().getDrawingParams().getFillColor()).isEqualTo(Color.PURPLE);
    }

    /**
     * Verifies that a shape can be selected when in Idle mode
     * by clicking on its center after drawing.
     */
    @Test
    void testShapeSelectionInIdleMode(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.clickOn("#rectangleBtn");

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();
    }

    /**
     * Verifies that a polygon can be selected when in Idle mode
     * by clicking on its center after drawing.
     */
    @Test
    void testPolygonSelectionInIdleMode(FxRobot robot) {
        robot.clickOn("#polygonBtn");

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);
        robot.clickOn("#polygonBtn");
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();
    }

    /**
     * Verifies that clicking outside any shape in Idle mode
     * does not select any shape.
     */
    @Test
    void testNoSelectionWhenClickingOutsideShapes(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.clickOn("#rectangleBtn");

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double clickX = bounds.getMaxX() - 10;
        double clickY = bounds.getMaxY() - 10;
        robot.moveTo(new Point2D(clickX, clickY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingContext().getSelectedShape()).isNull();
    }

    /**
     * Verifies that selection does not occur when in Rectangle drawing mode,
     * even if clicking on an existing shape.
     */
    @Test
    void testNoSelectionWhenInRectangleMode(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isNotInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNull();
    }

    /**
     * Verifies that selection does not occur when in Ellipse drawing mode,
     * even if clicking on an existing shape.
     */
    @Test
    void testNoSelectionWhenInEllipseMode(FxRobot robot) {
        robot.clickOn("#ellipseBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isNotInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNull();
    }

    /**
     * Verifies that selection does not occur when in Segment drawing mode,
     * even if clicking on an existing shape.
     */
    @Test
    void testNoSelectionWhenInSegmentMode(FxRobot robot) {
        robot.clickOn("#segmentBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isNotInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNull();
    }

    /**
     * Verifies that selection does not occur when in Polygon drawing mode,
     * even if clicking on an existing shape.
     */
    @Test
    void testNoSelectionWhenInPolygonMode(FxRobot robot) {
        robot.clickOn("#polygonBtn");


        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);


        assertThat(controller.getDrawingStrategy()).isNotInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNull();
    }

    /**
     * Tests that the TextField with id "widthTxt" accepts only valid numeric input.
     * This test simulates user input of invalid characters and checks that only
     * the numeric portion is accepted. Specifically, it verifies that:
     * <ul>
     *   <li>Initial non-numeric input ("abc") is ignored.</li>
     *   <li>Partially valid input with sign and invalid characters ("-12a3") is filtered to "123".</li>
     *   <li>Valid decimal input ("456.78") is accepted correctly.</li>
     * </ul>
     * @param robot the FxRobot instance used to simulate user interaction with the UI
     */
    @Test
    void testWidthTextFieldAcceptsOnlyNumericInput(FxRobot robot) {
        robot.clickOn("#widthTxt");
        robot.write("abc");
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);

        robot.eraseText(5);
        robot.write("-12a3");
        assertThat(widthField.getText()).isEqualTo("123");

        robot.eraseText(5);
        robot.write("456.78");
        assertThat(widthField.getText()).isEqualTo("456.78");
    }

    /**
     * Tests that the TextField with id "heightTxt" accepts only valid numeric input.
     * This test simulates user input of invalid characters and checks that only
     * the numeric portion is accepted. Specifically, it verifies that:
     * <ul>
     *   <li>Initial non-numeric input ("xyz") is ignored.</li>
     *   <li>Partially valid input with sign and invalid characters ("-98x") is filtered to "98".</li>
     *   <li>Partially valid decimal input ("@321.00") is accepted correctly.</li>
     * </ul>
     * @param robot the FxRobot instance used to simulate user interaction with the UI
     */
    @Test
    void testHeightTextFieldAcceptsOnlyNumericInput(FxRobot robot) {
        robot.clickOn("#heightTxt");
        robot.write("xyz");
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);

        robot.eraseText(5);
        robot.write("-98x");
        assertThat(heightField.getText()).isEqualTo("98");

        robot.eraseText(5);
        robot.write("@321.00");
        assertThat(heightField.getText()).isEqualTo("321.00");
    }

    /**
     * Tests that the width and height properties in the drawing context are initialized
     * and correctly bound to the corresponding TextFields.
     * <p>
     * The test verifies that these properties are not null and that updating the TextField's
     * text updates the bound DoubleProperty accordingly.
     * </p>
     *
     * @param robot the FxRobot instance used to simulate user input
     */
    @Test
    void testDrawingParamsPropertiesAreSetAndBound(FxRobot robot) {
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);

        DoubleProperty widthProperty = controller.getDrawingContext().getDrawingParams().getWidthValuePropertyProperty();
        DoubleProperty heightProperty = controller.getDrawingContext().getDrawingParams().getHeightValuePropertyProperty();

        assertThat(widthProperty).isNotNull();
        assertThat(heightProperty).isNotNull();

        robot.clickOn("#widthTxt");
        robot.eraseText(5);
        robot.write("123.45");
        assertThat(widthProperty.get()).isEqualTo(123.45);


        robot.clickOn(heightField);
        robot.eraseText(5);
        robot.write("678.90");
        assertThat(heightProperty.get()).isEqualTo(678.90);

        robot.interact(() -> widthProperty.set(111.22));
        assertThat(widthField.getText()).isEqualTo("111.22");

        robot.interact(() -> heightProperty.set(333.44));
        assertThat(heightField.getText()).isEqualTo("333.44");
    }

    /**
     * Tests that the TextField with id "heightTxt" accepts only valid numeric input.
     * This test simulates user input of invalid characters and checks that only
     * the numeric portion is accepted. Specifically, it verifies that:
     * <ul>
     *   <li>Initial non-numeric input ("xyz") is ignored.</li>
     *   <li>Partially valid input with sign and invalid characters ("-98x") is filtered to "98".</li>
     *   <li>Partially valid decimal input ("@321.00") is accepted correctly.</li>
     * </ul>
     * @param robot the FxRobot instance used to simulate user interaction with the UI
     */
    @Test
    void testTextShapeFieldAndBind(FxRobot robot) {
        robot.clickOn("#textShapeTxt");
        robot.write("1235454seefdfsdfwcvSWDdncsnbwuehe@°°°###é?12*ùù+");
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        assertThat(textField.getText()).isEqualTo("1235454seefdfsdfwcvSWDdncsnbwuehe@°°°###é?12*ùù+");
        controller.getDrawingContext().getDrawingParams().getText().equals("1235454seefdfsdfwcvSWDdncsnbwuehe@°°°###é?12*ùù+");

    }

    /**
     * Tests that the "Delete" menu item in the context menu properly removes the selected shape
     * from the drawing pane.
     *
     * <p>Test steps:
     * <ol>
     *   <li>Draw a rectangle on the drawing pane by clicking the rectangle button and then clicking on the pane.</li>
     *   <li>Exit drawing mode and select the previously drawn shape by clicking on it.</li>
     *   <li>Open the context menu by right-clicking on the selected shape.</li>
     *   <li>Click on the "Delete" menu item to remove the selected shape.</li>
     *   <li>Assert that the selected shape is no longer present in the drawing pane's children.</li>
     * </ol>
     * </p>
     *
     * @param robot the FxRobot instance used to simulate user interactions in the UI
     */
    @Test
    void testDeleteButtonRemovesSelectedShape(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();

        robot.moveTo(center).clickOn(MouseButton.SECONDARY);

        robot.clickOn("#deleteBtn");

        assertThat(controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()).doesNotContain((Node) selectedShape);
    }

    /**
     * Tests that the "Delete" menu item in the context menu properly removes the selected polygon shape
     * from the drawing pane.
     *
     * <p>Test steps:
     * <ol>
     *   <li>Draw a polygon on the drawing pane</li>
     *   <li>Exit drawing mode and select the previously drawn shape by clicking on it.</li>
     *   <li>Open the context menu by right-clicking on the selected shape.</li>
     *   <li>Click on the "Delete" menu item to remove the selected shape.</li>
     *   <li>Assert that the selected shape is no longer present in the drawing pane's children.</li>
     * </ol>
     * </p>
     *
     * @param robot the FxRobot instance used to simulate user interactions in the UI
     */
    @Test
    void testDeleteButtonRemovesSelectedPolygonShape(FxRobot robot) {

        robot.clickOn("#polygonBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        robot.clickOn("#polygonBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();

        robot.moveTo(center).clickOn(MouseButton.SECONDARY);

        robot.clickOn("#deleteBtn");
        assertThat(controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()).doesNotContain((Node) selectedShape);
    }

    /**
     * Tests the copy functionality triggered by the copy button.
     * <p>
     * This test simulates the following user interactions:
     * <ol>
     *   <li>Clicks the ellipse drawing button to enter drawing mode.</li>
     *   <li>Draws an ellipse shape at the center of the drawing pane.</li>
     *   <li>Exits drawing mode by clicking the ellipse button again.</li>
     *   <li>Selects the drawn ellipse by clicking on it.</li>
     *   <li>Verifies the shape is selected.</li>
     *   <li>Right-clicks on the selected shape to open the context menu.</li>
     *   <li>Clicks the copy button to perform the copy command.</li>
     *   <li>Checks that the copied shape is not null and is a distinct instance from the original.</li>
     *   <li>If the shape is an EllipseShape, verifies that all relevant properties
     *       (position, radii, fill color, edge color) are copied correctly.</li>
     * </ol>
     *
     * @param robot the FxRobot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testCopyButtonCopiesSelectedShape(FxRobot robot) {
        robot.clickOn("#ellipseBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.clickOn("#ellipseBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();
        robot.moveTo(center).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#copyBtn");

        CopyCommand copyCommand = (CopyCommand) controller.getCopyCommand();
        SelectableShape copiedShape = copyCommand.getCopiedShape();

        assertThat(copiedShape).isNotNull();
        assertThat(copiedShape).isNotSameAs(selectedShape);

        if (copiedShape instanceof EllipseShape && selectedShape instanceof EllipseShape) {
            EllipseShape copyEllipse = (EllipseShape) copiedShape;
            EllipseShape origEllipse = (EllipseShape) selectedShape;

            assertThat(copyEllipse.getAnchorX()).isEqualTo(origEllipse.getAnchorX());
            assertThat(copyEllipse.getAnchorY()).isEqualTo(origEllipse.getAnchorY());
            assertThat(copyEllipse.getRadiusX()).isEqualTo(origEllipse.getRadiusX());
            assertThat(copyEllipse.getRadiusY()).isEqualTo(origEllipse.getRadiusY());
            assertThat(copyEllipse.getFillColor()).isEqualTo(origEllipse.getFillColor());
            assertThat(copyEllipse.getEdgeColor()).isEqualTo(origEllipse.getEdgeColor());
            assertThat(copyEllipse.getScaleX()).isEqualTo(origEllipse.getScaleX());
            assertThat(copyEllipse.getScaleY()).isEqualTo(origEllipse.getScaleY());
            assertThat(copyEllipse.getRotation()).isEqualTo(origEllipse.getRotation());
        }
    }

    /**
     * Tests the copy functionality triggered by the copy button.
     * <p>
     * This test simulates the following user interactions:
     * <ol>
     *   <li>Clicks the polygon drawing button to enter drawing mode.</li>
     *   <li>Draws a polygon shape.</li>
     *   <li>Exits drawing mode by clicking the polygon button again.</li>
     *   <li>Selects the drawn polygon by clicking on it.</li>
     *   <li>Verifies the shape is selected.</li>
     *   <li>Right-clicks on the selected shape to open the context menu.</li>
     *   <li>Clicks the copy button to perform the copy command.</li>
     *   <li>Checks that the copied shape is not null and is a distinct instance from the original.</li>
     *   <li>If the shape is a PolygonShape, verifies all properties</li>
     * </ol>
     *
     * @param robot the FxRobot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testCopyButtonCopiesSelectedPolygonShape(FxRobot robot) {
        robot.clickOn("#polygonBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        robot.clickOn("#polygonBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();
        robot.moveTo(center).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#copyBtn");

        CopyCommand copyCommand = (CopyCommand) controller.getCopyCommand();
        SelectableShape copiedShape = copyCommand.getCopiedShape();

        assertThat(copiedShape).isNotNull();
        assertThat(copiedShape).isNotSameAs(selectedShape);

        if (copiedShape instanceof PolygonShape && selectedShape instanceof PolygonShape) {
            PolygonShape copyPolygon = (PolygonShape) copiedShape;
            PolygonShape origPolygon = (PolygonShape) selectedShape;

            assertThat(copyPolygon.getFillColor()).isEqualTo(origPolygon.getFillColor());
            assertThat(copyPolygon.getEdgeColor()).isEqualTo(origPolygon.getEdgeColor());
            assertThat(copyPolygon.getPoints()).isEqualTo(origPolygon.getPoints());
            assertThat(copyPolygon.getScaleX()).isEqualTo(origPolygon.getScaleX());
            assertThat(copyPolygon.getScaleY()).isEqualTo(origPolygon.getScaleY());
            assertThat(copyPolygon.getRotation()).isEqualTo(origPolygon.getRotation());
        }
    }
    /**
     * Tests the copy functionality triggered by the copy button.
     * <p>
     * This test simulates the following user interactions:
     * <ol>
     *   <li>Clicks the text drawing button to enter drawing mode.</li>
     *   <li>Draws an text shape at the center of the drawing pane.</li>
     *   <li>Exits drawing mode by clicking the text button again.</li>
     *   <li>Selects the drawn text by clicking on it.</li>
     *   <li>Verifies the shape is selected.</li>
     *   <li>Right-clicks on the selected shape to open the context menu.</li>
     *   <li>Clicks the copy button to perform the copy command.</li>
     *   <li>Checks that the copied shape is not null and is a distinct instance from the original.</li>
     *   <li>If the shape is a TextShape, verifies that all relevant properties
     *       are copied correctly.</li>
     * </ol>
     *
     * @param robot the FxRobot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testCopyButtonCopiesSelectedTextShape(FxRobot robot) {
        robot.clickOn("#textShapeTxt");
        robot.write("testCopyButtonCopiesSelectedTextShape");
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        robot.clickOn("#textBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.clickOn("#textBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();
        robot.moveTo(center).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#copyBtn");

        CopyCommand copyCommand = (CopyCommand) controller.getCopyCommand();
        SelectableShape copiedShape = copyCommand.getCopiedShape();

        assertThat(copiedShape).isNotNull();
        assertThat(copiedShape).isNotSameAs(selectedShape);

        if (copiedShape instanceof TextShape && selectedShape instanceof TextShape) {
            TextShape copyText = (TextShape) copiedShape;
            TextShape origText = (TextShape) selectedShape;

            assertThat(copyText.getAnchorX()).isEqualTo(origText.getAnchorX());
            assertThat(copyText.getAnchorY()).isEqualTo(origText.getAnchorY());
            assertThat(copyText.getDimensionX()).isEqualTo(origText.getDimensionX());
            assertThat(copyText.getDimensionY()).isEqualTo(origText.getDimensionY());
            assertThat(copyText.getFillColor()).isEqualTo(origText.getFillColor());
            assertThat(copyText.getEdgeColor()).isEqualTo(origText.getEdgeColor());
            assertThat(copyText.getText()).isEqualTo(origText.getText());
            assertThat(copyText.getFontSize()).isEqualTo(origText.getFontSize());
            assertThat(copyText.getRotation()).isEqualTo(origText.getRotation());
        }
    }

    /**
     * Tests the cut functionality triggered by the cut button.
     * <p>
     * This test simulates the following user interactions:
     * <ol>
     *   <li>Clicks the text drawing button to enter drawing mode.</li>
     *   <li>Draws an text shape at the center of the drawing pane.</li>
     *   <li>Exits drawing mode by clicking the text button again.</li>
     *   <li>Selects the drawn text by clicking on it.</li>
     *   <li>Verifies the shape is selected.</li>
     *   <li>Right-clicks on the selected shape to open the context menu.</li>
     *   <li>Clicks the copy button to perform the copy command.</li>
     *   <li>Checks that the copied shape is not null and is a distinct instance from the original.</li>
     *   <li>If the shape is an EllipseShape, verifies that all relevant properties
     *       are copied correctly.
     *   <li>Checks that the original shape has been removed from the canvas.</li>
     *       </li>
     * </ol>
     *
     * @param robot the FxRobot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testCutButtonCopiesSelectedTextShape(FxRobot robot) {
        robot.clickOn("#textShapeTxt");
        robot.write("testCutButtonCopiesSelectedTextShape");
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        robot.clickOn("#textBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.clickOn("#textBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();

        int initialShapeCount = controller.getDrawingContext()
                .getDrawingParams().getDrawingCanvas().getChildren().size();

        robot.moveTo(center).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#cutBtn");

        CopyCommand copyCommand = (CopyCommand) controller.getCopyCommand();
        SelectableShape copiedShape = copyCommand.getCopiedShape();

        assertThat(copiedShape).isNotNull();
        assertThat(copiedShape).isNotSameAs(selectedShape);

        if (copiedShape instanceof TextShape && selectedShape instanceof TextShape) {
            TextShape copyText = (TextShape) copiedShape;
            TextShape origText = (TextShape) selectedShape;

            assertThat(copyText.getAnchorX()).isEqualTo(origText.getAnchorX());
            assertThat(copyText.getAnchorY()).isEqualTo(origText.getAnchorY());
            assertThat(copyText.getDimensionX()).isEqualTo(origText.getDimensionX());
            assertThat(copyText.getDimensionY()).isEqualTo(origText.getDimensionY());
            assertThat(copyText.getFillColor()).isEqualTo(origText.getFillColor());
            assertThat(copyText.getEdgeColor()).isEqualTo(origText.getEdgeColor());
            assertThat(copyText.getText()).isEqualTo(origText.getText());
            assertThat(copyText.getFontSize()).isEqualTo(origText.getFontSize());
            assertThat(copyText.getRotation()).isEqualTo(origText.getRotation());
        }

        int updatedShapeCount = controller.getDrawingContext()
                .getDrawingParams().getDrawingCanvas().getChildren().size();
        assertThat(updatedShapeCount).isEqualTo(initialShapeCount - 1);

        assertThat(updatedShapeCount).isEqualTo(initialShapeCount - 1);
    }


    /**
     * Tests the cut functionality triggered by the cut button in the context menu.
     * <p>
     * This test simulates the following user interactions:
     * <ol>
     *   <li>Clicks the rectangle drawing button to enter drawing mode.</li>
     *   <li>Draws a rectangle shape at the center of the drawing pane.</li>
     *   <li>Exits drawing mode by clicking the rectangle button again.</li>
     *   <li>Selects the drawn rectangle by clicking on it.</li>
     *   <li>Verifies the shape is selected.</li>
     *   <li>Right-clicks on the selected shape to open the context menu.</li>
     *   <li>Clicks the cut button to perform the cut command.</li>
     *   <li>Checks that the copied shape is not null and is a distinct instance from the original.</li>
     *   <li>Checks that the original shape has been removed from the canvas.</li>
     * </ol>
     *
     * @param robot the FxRobot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testCutButtonCopiesAndDeletesSelectedShape(FxRobot robot) {
        robot.clickOn("#rectangleBtn");
        Node drawingPane = robot.lookup("#drawingPane").query();
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        SelectableShape selectedShape = controller.getDrawingContext().getSelectedShape();
        assertThat(selectedShape).isNotNull();
        assertThat(selectedShape.isSelected()).isTrue();

        int initialShapeCount = controller.getDrawingContext()
                .getDrawingParams().getDrawingCanvas().getChildren().size();

        robot.moveTo(center).clickOn(MouseButton.SECONDARY);

        robot.clickOn("#cutBtn");

        assertThat(controller.getCopiedShape()).isNotNull();

        int updatedShapeCount = controller.getDrawingContext()
                .getDrawingParams().getDrawingCanvas().getChildren().size();
        assertThat(updatedShapeCount).isEqualTo(initialShapeCount - 1);
    }

    /**
     * Tests that the fill color of an ellipse shape can be correctly changed using the fill color picker.
     * It creates one ellipse, changes the fill color, and verifies that the color is applied to the selected one.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeEllipseFillColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#ellipseBtn");

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#ellipseBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getFillColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests that the fill color of a rectangle shape can be correctly changed using the fill color picker.
     * It draws one rectangle, modifies the fill color, and checks that the selected shape reflects the new color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeRectangleFillColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#rectangleBtn");

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getFillColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests that the fill color of a text shape can be correctly changed using the fill color picker.
     * It draws one text, modifies the fill color, and checks that the selected shape reflects the new color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeTextShapeFillColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> spinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);

        robot.interact(() -> {
            textField.setText("testChangeTextShapeFillColor");
            spinner.getValueFactory().setValue(20.0);
        });

        robot.clickOn("#textBtn");

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#textBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getFillColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests the ability to change the fill color of a segment shape using the fill color picker.
     * It draws one segment, updates the fill color, and ensures the color of the selected segment is updated.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeSegmentFillColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#segmentBtn");

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D center1 = new Point2D(centerX + 50, centerY + 50);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveTo(center1).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#segmentBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
             
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getFillColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests the ability to change the fill color of a polygon shape using the fill color picker.
     * It draws one polygon, updates the fill color, and ensures the color of the selected polygon is updated.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangePolygonFillColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#polygonBtn");

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        assertThat(fillPicker).isNotNull();

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        robot.clickOn("#polygonBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.GREEN);
            controller.onFillColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getFillColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Verifies that the stroke (edge) color of an ellipse can be changed via the stroke color picker.
     * It draws one ellipse and asserts that the selected ellipse reflects the updated edge color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeEllipseEdgeColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#ellipseBtn");

        ColorPicker edgePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(edgePicker).isNotNull();

        robot.interact(() -> {
            edgePicker.setValue(Color.YELLOW);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#ellipseBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            edgePicker.setValue(Color.GREEN);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getEdgeColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Ensures that the edge color of a rectangle can be modified correctly using the stroke color picker.
     * After drawing one rectangle, the test checks that the selected rectangle displays the new edge color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeRectangleEdgeColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#rectangleBtn");

        ColorPicker edgePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(edgePicker).isNotNull();

        robot.interact(() -> {
            edgePicker.setValue(Color.YELLOW);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            edgePicker.setValue(Color.GREEN);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getEdgeColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests that the edge color of a text shape can be correctly changed using the fill color picker.
     * It draws one text, modifies the edge color, and checks that the selected shape reflects the new color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeTextShapeEdgeColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> spinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);

        robot.interact(() -> {
            textField.setText("testChangeTextShapeEdgeColor");
            spinner.getValueFactory().setValue(20.0);
        });

        robot.clickOn("#textBtn");

        ColorPicker edgePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(edgePicker).isNotNull();

        robot.interact(() -> {
            edgePicker.setValue(Color.YELLOW);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#textBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            edgePicker.setValue(Color.GREEN);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getEdgeColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Ensures that the edge color of a polygon can be modified correctly using the stroke color picker.
     * After drawing one polygon, the test checks that the selected polygon displays the new edge color.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangePolygonEdgeColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#polygonBtn");

        ColorPicker edgePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(edgePicker).isNotNull();

        robot.interact(() -> {
            edgePicker.setValue(Color.YELLOW);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 100).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        robot.clickOn("#polygonBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            edgePicker.setValue(Color.GREEN);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getEdgeColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests the undo functionality after drawing a rectangle.
     * Verifies that the canvas returns to its previous state (same number of children).
     */
    @Test
    void testUndoButtonActionRectangle(FxRobot robot){

        List<Node> beforeUndo = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren();

        robot.clickOn("#rectangleBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren();
        assertThat(beforeUndo.size()).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after drawing a text.
     * Verifies that the canvas returns to its previous state (same number of children).
     */
    @Test
    void testUndoButtonActionText(FxRobot robot){
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> spinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);

        robot.interact(() -> {
            textField.setText("testUndoButtonActionText");
            spinner.getValueFactory().setValue(20.0);
        });

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );


        robot.clickOn("#textBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
         

        robot.interact(() -> {
            controller.onUndoButton(new ActionEvent());
        });

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );
        assertThat(beforeUndo.size()).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after drawing an ellipse.
     * Ensures that the canvas is restored to its original state (same number of children).
     */
    @Test
    void testUndoButtonActionEllipse(FxRobot robot){

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        robot.clickOn("#ellipseBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);
         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );



        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndo.size()).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after drawing a line segment.
     * Asserts that undo reverts the canvas to its previous content size.
     */
    @Test
    void testUndoButtonActionSegment(FxRobot robot){

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        robot.clickOn("#segmentBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50,50).clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);
         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
            controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndo.size()).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after drawing a line segment.
     * Asserts that undo reverts the canvas to its previous content size.
     */
    @Test
    void testUndoButtonActionPolygon(FxRobot robot){

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        robot.clickOn("#polygonBtn");
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50,50).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-20,100).doubleClickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);
         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndo.size()).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after changing the fill color of a shape.
     * Verifies that the fill color reverts correctly after undo.
     */
    @Test
    void testUndoButtonActionChangeFill(FxRobot robot){

        ColorPicker fillPicker = robot.lookup("#fillColorSelector").queryAs(ColorPicker.class);
        Node drawingPane = robot.lookup("#drawingPane").query();

        robot.clickOn("#ellipseBtn");

        robot.interact(() -> {
            fillPicker.setValue(Color.YELLOW);
            controller.onFillColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#ellipseBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            fillPicker.setValue(Color.RED);
            controller.onFillColorPicker(new ActionEvent());
        });

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

         

        robot.interact(() -> {
            controller.onUndoButton(new ActionEvent());
        });

        robot.interact(() -> {
            robot.clickOn("#ellipseBtn");
        });

         

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        EllipseShape shape2 = (EllipseShape) afterUndo.get(0);

        assertThat(Color.YELLOW).isEqualTo(shape2.getFillColor());
    }

    /**
     * Tests the undo functionality after changing the stroke (edge) color of a shape.
     * Verifies that the edge color is restored after undo.
     */
    @Test
    void testUndoButtonActionChangeEdge(FxRobot robot){

        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        Node drawingPane = robot.lookup("#drawingPane").query();

        robot.clickOn("#ellipseBtn");

        robot.interact(() -> {
            strokePicker.setValue(Color.YELLOW);
            controller.onStrokeColorPicker(new ActionEvent());
        });


        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.clickOn("#ellipseBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            strokePicker.setValue(Color.RED);
            controller.onStrokeColorPicker(new ActionEvent());
        });


        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        EllipseShape shape2 = (EllipseShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        assertThat(Color.YELLOW).isEqualTo(shape2.getEdgeColor());
    }

    /**
     * Tests the undo functionality after dragging (moving) a shape.
     * Verifies that the shape's position reverts to its original coordinates after undo.
     */
    @Test
    void testUndoButtonActionDrag(FxRobot robot){

        Node drawingPane = robot.lookup("#drawingPane").query();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        RectangleShape shape = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double shapeAnchorX = shape.getAnchorX();
        double shapeAnchorY = shape.getAnchorY();

        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY)
                        .press(MouseButton.PRIMARY).moveBy(50,50).release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        RectangleShape shape2 = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        assertThat(shapeAnchorX).isEqualTo(shape2.getAnchorX());
        assertThat(shapeAnchorY).isEqualTo(shape2.getAnchorY());
    }

    /**
     * Tests the undo functionality after resizing a rectangle shape.
     * Ensures that width and height return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionResizeRectangle(FxRobot robot){

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("50");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        RectangleShape shape = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double shapeDimensionX = shape.getDimensionX();
        double shapeDimensionY = shape.getDimensionY();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialWidth = shape.getDimensionX();

        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY);
        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        RectangleShape shape2 = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        assertThat(shapeDimensionX).isEqualTo(shape2.getDimensionX());
        assertThat(shapeDimensionY).isEqualTo(shape2.getDimensionY());
    }

    /**
     * Tests the undo functionality after resizing a segment shape.
     * Ensures that dimensionX and dimensionY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionResizeSegment(FxRobot robot){

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#segmentBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(SegmentDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        robot.moveBy(50, 50).clickOn(MouseButton.PRIMARY);

        SegmentShape shape = (SegmentShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double origDimensionX = shape.getDimensionX();
        double origDimensionY = shape.getDimensionY();

        robot.clickOn("#segmentBtn");

        Point2D localEnd = new Point2D(shape.getEndPointX(), shape.getEndPointY());
        Point2D sceneEnd = shape.localToScene(localEnd);
        Point2D screenEnd = shape.getScene().getWindow().getScene().getRoot().localToScreen(sceneEnd);

        robot.moveTo(screenEnd).clickOn(MouseButton.PRIMARY).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#resizeBtn");

        double displacement = 100;
        robot.moveTo(screenEnd)
                .press(MouseButton.PRIMARY)
                .moveBy(displacement, 0)
                .release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );


        SegmentShape shape2 = (SegmentShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        double newDimensionX = shape2.getDimensionX();
        double newDimensionY = shape2.getDimensionY();

        assertThat(origDimensionX).isEqualTo(newDimensionX);
        assertThat(origDimensionY).isEqualTo(newDimensionY);
    }

    /**
     * Tests the undo functionality after resizing an ellipse shape.
     * Ensures that radiusX and radiusY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionResizeEllipse(FxRobot robot){

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
            heightField.setText("150");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        EllipseShape shape = (EllipseShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double shapeDimensionX = shape.getDimensionX();
        double shapeDimensionY = shape.getDimensionY();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 200;
        double initialRadiusX = shape.getDimensionX();

        Point2D borderPointEllipse = new Point2D(centerX + initialRadiusX/2, centerY);
        robot.moveTo(borderPointEllipse).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);
        

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );
        
        EllipseShape shape2 = (EllipseShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        assertThat(shapeDimensionX).isEqualTo(shape2.getDimensionX());
        assertThat(shapeDimensionY).isEqualTo(shape2.getDimensionY());
    }

    /**
     * Tests the undo functionality after resizing a polygon shape.
     * Ensures that anchorX and anchorY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionResizePolygon(FxRobot robot){

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(100, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-50,-70).doubleClickOn(MouseButton.PRIMARY);

        PolygonShape shape = (PolygonShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double origPointX = shape.getAnchorX();
        double origPointY = shape.getAnchorY();

        robot.clickOn("#polygonBtn");

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX,centerY).press(MouseButton.PRIMARY);
        robot.moveBy(-100, 0);
        robot.release(MouseButton.PRIMARY);
        
        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        PolygonShape shape2 = (PolygonShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        double newPointX = shape2.getAnchorX();
        double newPointY = shape2.getAnchorY();

        assertThat(origPointX).isEqualTo(newPointX);
        assertThat(origPointY).isEqualTo(newPointY);
    }

    /**
     * Tests the undo functionality after resizing a text shape.
     * Ensures that dimensionX and dimensionY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionResizeText(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
            textField.setText("testUndoButtonActionResizeText");
            widthField.setText("200");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn("#textBtn");

        TextShape shape = (TextShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double origDimensionX = shape.getDimensionX();
        double origDimensionY = shape.getDimensionY();


        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        Point2D resizePoint = new Point2D(centerX + origDimensionY / 2, centerY);
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        TextShape shape2 = (TextShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        double newDimensionX = shape2.getDimensionX();
        double newDimensionY = shape2.getDimensionY();

        assertThat(newDimensionX).isEqualTo(origDimensionX);
        assertThat(newDimensionY).isEqualTo(origDimensionY);
    }


    /**
     * Tests the undo functionality after stretching a RectangleShape.
     * Ensures that width and height return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionStretchRectangle(FxRobot robot){

        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("300");
            heightField.setText("340");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        RectangleShape shape = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double shapeDimensionX = shape.getDimensionX();
        double shapeDimensionY = shape.getDimensionY();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialHeight = shape.getDimensionY();

        Point2D borderPointRectangle = new Point2D(centerX, centerY + initialHeight/2);
        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        RectangleShape shape2 = (RectangleShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        assertThat(shapeDimensionX).isEqualTo(shape2.getDimensionX());
        assertThat(shapeDimensionY).isEqualTo(shape2.getDimensionY());
    }

    /**
     * Tests the undo functionality after stretching a ellipse shape.
     * Ensures that radiusX and radiusY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionStretchEllipse(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("200");
            heightField.setText("140");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        EllipseShape shape = (EllipseShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double shapeDimensionX = shape.getDimensionX();
        double shapeDimensionY = shape.getDimensionY();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double radiusX = shape.getDimensionX();

        Point2D borderPointRectangle = new Point2D(centerX + radiusX, centerY);
        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(-displacement, 0);
        robot.release(MouseButton.PRIMARY);

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

         

        EllipseShape shape2 = (EllipseShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        assertThat(shapeDimensionX).isEqualTo(shape2.getDimensionX());
        assertThat(shapeDimensionY).isEqualTo(shape2.getDimensionY());
    }

    /**
     * Tests the undo functionality after stretching a polygon shape.
     * Ensures that the point moved return to the initial position after undo.
     */
    @Test
    void testUndoButtonActionStretchPolygon(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        robot.clickOn("#polygonBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(PolygonDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        robot.moveBy(120, 0).clickOn(MouseButton.PRIMARY);
        robot.moveBy(-60,-70).doubleClickOn(MouseButton.PRIMARY);

        PolygonShape shape = (PolygonShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double origPointX = shape.getAnchorX();
        double origPointY = shape.getAnchorY();

        robot.clickOn("#polygonBtn");

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        robot.moveTo(centerX,centerY).press(MouseButton.PRIMARY);
        robot.moveBy(100, -10);
        robot.release(MouseButton.PRIMARY);

         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

         

        PolygonShape shape2 = (PolygonShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        double newPointX = shape2.getAnchorX();
        double newPointY = shape2.getAnchorY();

        assertThat(origPointX).isEqualTo(newPointX);
        assertThat(origPointY).isEqualTo(newPointY);
    }

    /**
     * Tests the undo functionality after stretching a text shape.
     * Ensures that scaleX and scaleY return to their initial values after undo.
     */
    @Test
    void testUndoButtonActionStretchText(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();

        robot.interact(() -> {
            TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
            textField.setText("testUndoButtonActionResizeText");
            widthField.setText("200");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn("#textBtn");

        TextShape shape = (TextShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        double origDimensionY = shape.getDimensionY();
        double origScaleX = shape.getScaleX();
        double origScaleY = shape.getScaleY();


        robot.moveTo(centerX, centerY).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#stretchBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        Point2D resizePoint = new Point2D(centerX + origDimensionY / 2, centerY);
        robot.moveTo(resizePoint).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

         

        TextShape shape2 = (TextShape) controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        double newScaleX = shape2.getScaleX();
        double newScaleY = shape2.getScaleY();

        assertThat(newScaleX).isEqualTo(origScaleX);
        assertThat(newScaleY).isEqualTo(origScaleY);
    }


    /**
     * Tests the undo functionality after pasting a shape using a paste command.
     * Verifies that the pasted shape is removed after undo.
     */
    @Test
    void testUndoButtonActionPaste(FxRobot robot){

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        double beforeUndoSize = beforeUndo.size();

        PasteCommand pasteCommand = new PasteCommand();
        DrawingParams params = controller.getDrawingContext().getDrawingParams();
        SegmentShape segment = new SegmentShape(
                params.getFillColor(), params.getEdgeColor(),
                50, 50, 150, 150
        );

        pasteCommand.setShape(segment);
        pasteCommand.setPasteX(300.0);
        pasteCommand.setPasteY(200.0);
        pasteCommand.setDrawingParams(params);

        robot.interact(pasteCommand::execute);
         
        robot.interact(()->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndoSize).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after deleting a shape.
     * Verifies that the deleted shape is restored after undo.
     */
    @Test
    void testUndoButtonActionDelete(FxRobot robot){

        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        double beforeUndoSize = beforeUndo.size();

        robot.clickOn("#ellipseBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.clickOn("#deleteBtn");

         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
               controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndoSize).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the undo functionality after creating a new pane.
     * Verifies that the canvas content is restored to the previous pane after undo.
     */
    @Test
    void testUndoButtonActionNewPane(FxRobot robot){

        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#ellipseBtn");

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        double beforeUndoSize = beforeUndo.size();

        robot.interact(() ->
                controller.createNewPane(new ActionEvent())
        );

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(beforeUndoSize).isEqualTo(afterUndo.size());
    }

    /**
     * Tests the behavior of the Undo button after two insert and an Undo.
     * <p>
     * Verify if the Undo performs in the correct way after a drag operation
     * of a {@link Shape} of a cloned Pane after a first undo
     * </p>
     * @param robot the TestFX {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testUndoButtonActionMoveAfterUndo(FxRobot robot){
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#ellipseBtn");

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        robot.clickOn("#ellipseBtn");
        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D newPoint = new Point2D(centerX+200, centerY+200);

        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#ellipseBtn");

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        EllipseShape before = (EllipseShape) beforeUndo.get(0);

        double beforeAnchorX = before.getAnchorX();
        double beforeAnchorY = before.getAnchorY();

        robot.interact(() -> {
            robot.moveTo(center).clickOn(MouseButton.PRIMARY);
            robot.moveTo(center).press(MouseButton.PRIMARY).moveTo(newPoint);
            robot.release(MouseButton.PRIMARY);

        });

         

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        EllipseShape after = (EllipseShape) afterUndo.get(0);

        assertThat(beforeAnchorX).isEqualTo(after.getAnchorX());
        assertThat(beforeAnchorY).isEqualTo(after.getAnchorY());

    }

    /**
     * Tests the behavior of the Undo button after two insert and an Undo.
     * <p>
     * Verify if the Undo performs in the correct way after a strokeColor and Resize operation
     * of a {@link Shape} of a cloned Pane after a first undo
     * </p>
     * @param robot the TestFX {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testUndoButtonActionOperationsAfterFirstUndo(FxRobot robot){
        ColorPicker strokePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#rectangleBtn");

        robot.interact(() -> {
            strokePicker.setValue(Color.BLACK);
            controller.onStrokeColorPicker(new ActionEvent());
        });

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");
        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D newPoint = new Point2D(centerX+200, centerY+200);

        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#rectangleBtn");

        robot.interact(() ->
                controller.onUndoButton(new ActionEvent())
        );

        List<Node> beforeUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        RectangleShape before = (RectangleShape) beforeUndo.get(0);
        double beforeDimensionX = before.getDimensionX();
        double beforeDimensionY = before.getDimensionY();
        Color beforeEdgeColor = before.getEdgeColor();

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            strokePicker.setValue(Color.RED);
            controller.onStrokeColorPicker(new ActionEvent());
        });


        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#resizeBtn").clickOn(MouseButton.PRIMARY);

        double displacement = 100;
        double initialWidth = before.getDimensionX();
        Point2D borderPointRectangle = new Point2D(centerX + initialWidth/2, centerY);

        robot.moveTo(borderPointRectangle).press(MouseButton.PRIMARY);
        robot.moveBy(displacement, 0);
        robot.release(MouseButton.PRIMARY);

        robot.interact(() ->{
            controller.onUndoButton(new ActionEvent());
            controller.onUndoButton(new ActionEvent());
        });

        List<Node> afterUndo = new ArrayList<>(
                controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        RectangleShape after = (RectangleShape) afterUndo.get(0);

        assertThat(beforeDimensionY).isEqualTo(after.getDimensionY());
        assertThat(beforeDimensionX).isEqualTo(after.getDimensionX());
        assertThat(beforeEdgeColor).isEqualTo(after.getEdgeColor());
        assertThat(after.getEdgeColor()).isEqualTo(Color.BLACK);
    }


    /**
     * Tests the functionality of changing the stroke color of a segment shape.
     * It draws one segment and verifies that the updated stroke color is applied to the selected segment.
     *
     * @param robot the TestFX robot used to simulate user interactions
     */
    @Test
    void testChangeSegmentEdgeColor(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();
        robot.clickOn("#segmentBtn");

        ColorPicker edgePicker = robot.lookup("#strokeColorSelector").queryAs(ColorPicker.class);
        assertThat(edgePicker).isNotNull();

        robot.interact(() -> {
            edgePicker.setValue(Color.YELLOW);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D center1 = new Point2D(centerX + 50, centerY + 50);

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        robot.moveTo(center1).clickOn(MouseButton.PRIMARY);

        robot.clickOn("#segmentBtn");

        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.interact(() -> {
            edgePicker.setValue(Color.GREEN);
             
            controller.onStrokeColorPicker(new ActionEvent());
        });

        assertThat((controller.getDrawingContext().getSelectedShape().getEdgeColor())).isEqualTo(Color.GREEN);
    }

    /**
     * Tests the {@link AppController#onForegroundButtonAction(ActionEvent)} method.
     * <p>
     * This test verifies that when a shape is selected and the "Bring to Front" action is triggered,
     * the shape is moved to the front (i.e., the last position) of the canvas's children list.
     * </p>
     *
     * @param robot the TestFX robot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testOnForegroundButtonAction(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D newPoint = new Point2D(centerX + 60, centerY + 60);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);
        Node back = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);
        robot.moveTo(newPoint).clickOn(MouseButton.SECONDARY);

        robot.clickOn("#foregroundBtn");

        Node front = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(1);
        assertThat(front).isNotNull();
        assertThat(front).isEqualTo(back);
    }

    /**
     * Tests the {@link AppController#onBackgroundButtonAction(ActionEvent)} method.
     * <p>
     * This test verifies that when a shape is selected and the "Send to Back" action is triggered,
     * the shape is moved to the back (i.e., the first position) of the canvas's children list.
     * </p>
     *
     * @param robot the TestFX robot used to simulate user interactions with the JavaFX UI
     */
    @Test
    void testOnBackgroundButtonAction(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D newPoint = new Point2D(centerX + 50, centerY + 50);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);

        Node front = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(1);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);
        robot.moveTo(newPoint)
                .clickOn(MouseButton.SECONDARY);

        robot.clickOn("#backgroundBtn");

        Node back = controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren().get(0);
        assertThat(back).isNotNull();
        assertThat(back).isEqualTo(front);
    }

    /**
     * Tests the undo functionality after executing the onBackgroundButtonAction method.
     * Verifies that the shape is restored to its original position in the drawing order after undo.
     */
    @Test
    void testUndoButtonActionBackground(FxRobot robot) {
        Node drawingPane = robot.lookup("#drawingPane").query();

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        Point2D center = new Point2D(centerX, centerY);
        Point2D newPoint = new Point2D(centerX + 50, centerY + 50);

        robot.clickOn("#rectangleBtn");
        robot.moveTo(center).clickOn(MouseButton.PRIMARY);

        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);

        List<Node> beforeUndo = new ArrayList<>(
                        controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        List<Double> beforeLayoutX = new ArrayList<>();
        List<Double> beforeLayoutY = new ArrayList<>();
        List<Bounds> beforeBounds = new ArrayList<>();

        for (Node node : beforeUndo) {
            beforeLayoutX.add(node.getLayoutX());
            beforeLayoutY.add(node.getLayoutY());
            beforeBounds.add(node.getBoundsInLocal());
        }

        robot.clickOn("#rectangleBtn");
        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);
        robot.moveTo(newPoint)
                .clickOn(MouseButton.SECONDARY);

        robot.clickOn("#backgroundBtn");

        robot.moveTo(newPoint).clickOn(MouseButton.PRIMARY);
        robot.moveTo(newPoint)
                .clickOn(MouseButton.SECONDARY);

        robot.clickOn("#foregroundBtn");

         
        robot.interact(
                () -> {
                    controller.onUndoButton(new ActionEvent());
                    controller.onUndoButton(new ActionEvent());
                });

        List<Node> afterUndo = new ArrayList<>(
                        controller.getDrawingContext().getDrawingParams().getDrawingCanvas().getChildren()
        );

        assertThat(afterUndo.size()).isEqualTo(beforeUndo.size());
        for (int i = 0; i < beforeUndo.size(); i++) {
            Node before = beforeUndo.get(i);
            Node after = afterUndo.get(i);

            assertThat(after.getClass()).isEqualTo(before.getClass());
            assertThat(after.getLayoutX()).isEqualTo(beforeLayoutX.get(i));
            assertThat(after.getLayoutY()).isEqualTo(beforeLayoutY.get(i));
            assertThat(after.getBoundsInLocal()).isEqualTo(beforeBounds.get(i));
        }
    }

    /**
     * Tests the toggle functionality of showing and hiding the background grid on the grid pane.
     * <p>
     * The test simulates a user clicking on the grid checkbox (with ID "#gridCB") to first enable
     * the grid and then disable it. It verifies that a CSS class starting with "grid-" is added
     * when the grid is shown, and removed when the grid is hidden.
     *
     * @param robot the TestFX robot used to interact with the UI components
     */
    @Test
    void testShowHideGrid(FxRobot robot) {
        Node gridPane = robot.lookup("#gridPane").query();

        robot.clickOn("#gridCB");

        assertThat(gridPane.getStyleClass().stream()
                .anyMatch(c -> c.startsWith("grid-"))).isTrue();

        

        robot.clickOn("#gridCB");

        assertThat(gridPane.getStyleClass().stream()
                .anyMatch(c -> c.startsWith("grid-"))).isFalse();
    }

    /**
     * Tests the grid block size changes using the slider on the grid pane.
     * <p>
     * The test first enables the grid, then sets the slider (with ID "#gridBlockSizeSlider") to increasing
     * values corresponding to predefined grid block sizes. After each change, it checks that the
     * correct "grid-X" CSS class is applied to reflect the new grid size.
     * <p>
     * Grid sizes tested: 5, 10, 20, 50, 100 pixels.
     *
     * @param robot the TestFX robot used to interact with the UI components
     */
    @Test
    void testIncreaseGridBlockSize(FxRobot robot) {
        Node gridPane = robot.lookup("#gridPane").query();

        robot.clickOn("#gridCB");

        

        List<Integer> gridBlockSizes = new ArrayList<>();

        gridBlockSizes.add(5);
        gridBlockSizes.add(10);
        gridBlockSizes.add(20);
        gridBlockSizes.add(50);
        gridBlockSizes.add(100);

        Slider slider = robot.lookup("#gridBlockSizeSlider").queryAs(Slider.class);

        for (int size : gridBlockSizes) {
            slider.setValue(gridBlockSizes.indexOf(size));

             

            assertThat(gridPane.getStyleClass().stream()
                    .anyMatch(c -> c.startsWith("grid-" + String.valueOf(size)))).isTrue();
        }
    }

    /**
     * Tests the grid block size changes using the slider in reverse order on the grid pane.
     * <p>
     * The test first enables the grid and then sets the slider (with ID "#gridBlockSizeSlider") to decreasing
     * values corresponding to predefined grid block sizes in reverse. After each change, it verifies that the
     * correct "grid-X" CSS class is applied to reflect the updated grid size.
     * <p>
     * Grid sizes tested in reverse: 100, 50, 20, 10, 5 pixels.
     *
     * @param robot the TestFX robot used to interact with the UI components
     */
    @Test
    void testDecreaseGridBlockSize(FxRobot robot) {
        Node gridPane = robot.lookup("#gridPane").query();

        robot.clickOn("#gridCB");

         

        List<Integer> gridBlockSizes = new ArrayList<>();

        gridBlockSizes.add(100);
        gridBlockSizes.add(50);
        gridBlockSizes.add(20);
        gridBlockSizes.add(10);
        gridBlockSizes.add(5);

        Slider slider = robot.lookup("#gridBlockSizeSlider").queryAs(Slider.class);

        for (int size : gridBlockSizes) {
            slider.setValue(gridBlockSizes.size() - gridBlockSizes.indexOf(size) - 1);

             

            assertThat(gridPane.getStyleClass().stream()
                    .anyMatch(c -> c.startsWith("grid-" + String.valueOf(size)))).isTrue();
        }
    }

    /**
     * Tests the undo functionality after rotating a {@link TextShape}.
     * <p>
     * This test simulates the following sequence:
     * <ul>
     *     <li>Creates a text shape with an initial rotation angle of 10 degrees</li>
     *     <li>Selects the shape and applies a new rotation of 30 degrees</li>
     *     <li>Performs an undo operation</li>
     *     <li>Verifies that the shape's rotation angle is reverted to the original value (10 degrees)</li>
     * </ul>
     *
     * @param robot the {@link FxRobot} used to simulate user interactions with the UI
     */
    @Test
    void testUndoRotateShape(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        TextField angleField = robot.lookup("#angleTxt").queryAs(TextField.class);

        robot.interact(() -> {
            textField.setText("testTextShapeFontIsResized");
            angleField.setText("10");
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof TextShape);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);
        assertThat(text).isNotNull();
        assertThat(10.0).isEqualTo(text.getRotation());

        robot.clickOn("#textBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(text);
        assertThat(text.isSelected()).isTrue();

        robot.interact(() -> {
            angleField.setText("30");
            controller.onAngleAction(new ActionEvent());
        });

        assertThat(30.0).isEqualTo(text.getRotation());

        robot.interact(()->{
           controller.onUndoButton(new ActionEvent());
        });

        TextShape text1 = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);


        assertThat(10.0).isEqualTo(text1.getRotation());
    }

    /**
     * Tests the undo functionality after flipping an {@link EllipseShape} horizontally.
     * <p>
     * This test verifies that the shape's horizontal scale (scaleX) is correctly restored
     * to its original value after an undo operation following a horizontal flip.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */
    @Test
    void testUndoFlipHorizontalShape(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#ellipseBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(EllipseDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof EllipseShape);

        EllipseShape drawn = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#ellipseBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#flipHorizontalBtn").clickOn(MouseButton.PRIMARY);
        assertThat(drawn.getScaleX()).isEqualTo(-1.0);

        robot.interact(()->{
            controller.onUndoButton(new ActionEvent());
        });

        EllipseShape ellipse1 = (EllipseShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof EllipseShape)
                .findFirst()
                .orElse(null);


        assertThat(1.0).isEqualTo(ellipse1.getScaleX());
    }

    /**
     * Tests the undo functionality after flipping a {@link RectangleShape} vertically.
     * <p>
     * This test ensures that the shape's vertical scale (scaleY) is reverted back to its
     * original value after performing a vertical flip followed by an undo action.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */

    @Test
    void testUndoFlipVerticalShape(FxRobot robot) {
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();

        TextField widthField = robot.lookup("#widthTxt").queryAs(TextField.class);
        TextField heightField = robot.lookup("#heightTxt").queryAs(TextField.class);
        assertThat(widthField).isNotNull();
        assertThat(heightField).isNotNull();

        robot.interact(() -> {
            widthField.setText("100");
            heightField.setText("60");
        });

        robot.clickOn("#rectangleBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(RectangleDrawingStrategy.class);

        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);
        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof RectangleShape);

        RectangleShape drawn = (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);
        assertThat(drawn).isNotNull();

        robot.clickOn("#rectangleBtn");

        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;

        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);
        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isNotNull();
        assertThat(controller.getDrawingContext().getSelectedShape().isSelected()).isTrue();

        robot.clickOn(MouseButton.SECONDARY);
        robot.moveTo("#flipVerticalBtn").clickOn(MouseButton.PRIMARY);

        assertThat(drawn.getScaleY()).isEqualTo(-1.0);

        robot.interact(()->{
            controller.onUndoButton(new ActionEvent());
        });

        RectangleShape rect1= (RectangleShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof RectangleShape)
                .findFirst()
                .orElse(null);


        assertThat(1.0).isEqualTo(rect1.getScaleY());
    }

    /**
     * Tests the undo functionality after changing the font size of a {@link TextShape}.
     * <p>
     * The test checks that the text shape's font size is updated correctly when changed
     * and that invoking undo restores the original font size.
     *
     * @param robot the {@link FxRobot} used to simulate user interactions
     */

    @Test
    void testUndoChangeFontSize(FxRobot robot){
        AnchorPane drawingPane = robot.lookup("#drawingPane").queryAs(AnchorPane.class);
        assertThat(drawingPane).isNotNull();
        TextField textField = robot.lookup("#textShapeTxt").queryAs(TextField.class);
        Spinner<Double> spinner = robot.lookup("#fontSizeMenu").queryAs(Spinner.class);

        robot.interact(() -> {
            textField.setText("testTextShapeFontIsResized");
            spinner.getValueFactory().setValue(20.0);
        });

        robot.clickOn("#textBtn");
        assertThat(controller.getDrawingStrategy()).isInstanceOf(TextDrawingStrategy.class);
        robot.moveTo("#drawingPane").clickOn(MouseButton.PRIMARY);

        assertThat(drawingPane.getChildren()).anyMatch(node -> node instanceof TextShape);

        TextShape text = (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);
        assertThat(text).isNotNull();

        robot.clickOn("#textBtn");
        Bounds bounds = drawingPane.localToScreen(drawingPane.getBoundsInLocal());
        double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2;
        robot.moveTo(new Point2D(centerX, centerY)).clickOn(MouseButton.PRIMARY);

        assertThat(controller.getDrawingStrategy()).isInstanceOf(IdleStrategy.class);
        assertThat(controller.getDrawingContext().getSelectedShape()).isEqualTo(text);
        assertThat(text.isSelected()).isTrue();

        robot.interact(() -> {
            spinner.getValueFactory().setValue(40.0);
        });

        assertThat(40.0).isEqualTo(text.getFontSize());

        robot.interact(()->{
            controller.onUndoButton(new ActionEvent());
        });

        TextShape text1= (TextShape) drawingPane.getChildren().stream()
                .filter(node -> node instanceof TextShape)
                .findFirst()
                .orElse(null);


        assertThat(20.0).isEqualTo(text1.getFontSize());
    }
}