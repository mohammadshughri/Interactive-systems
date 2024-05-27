package fontselect;

/* Fonts:
 * http://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html#typefont
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class FontSelect extends Application {
    private final GaussianBlur fontBlur = new GaussianBlur(0);
    private String fontName = "Arial";
    private double fontSize = 24;
    private FontWeight fontWeight = FontWeight.NORMAL;
    private FontPosture fontPosture = FontPosture.REGULAR;
    private Color fontColor = Color.BLACK;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        //////////////////////////////////////////
        // structure
        // Create an ObservableList of all available font families
        ObservableList<String> familiesAll = FXCollections.observableList(Font.getFamilies());

        // Create a ListView to display the font families
        ListView<String> familiesListView = new ListView<>();

        // Create a FilteredList to filter the font families based on search text
        FilteredList<String> families = familiesAll.filtered(item -> true);
        familiesListView.setItems(families);

        // Create a label to display the percentage of fonts remaining
        Label percentageView = new Label();
        percentageView.setPrefWidth(50);
        double percentage = (double) families.size() / Font.getFamilies().size() * 100;
        percentageView.setText(String.format("%.2f%%", percentage));

        // Create a label to display the count of fonts remaining
        Label countView = new Label();
        countView.setText(Integer.toString(families.size()));
        countView.setPrefWidth(50);

        // Create a search field to filter the font families
        TextField searchField = new TextField();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            families.setPredicate(item -> item.toLowerCase().contains(searchText));
            countView.setText(Integer.toString(families.size()));
            percentageView.setText(String.format("%.2f%%", (double) families.size() / Font.getFamilies().size() * 100));
        });

        // Create a Text object to display the selected font
        Text text = new Text();
        Font font = Font.font(fontName, fontWeight, fontPosture, fontSize);
        text.setFont(font);
        text.setWrappingWidth(600);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText("The quick brown fox jumps over the lazy dog");

        // Update the font when a font family is selected
        familiesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fontName = newValue;
            Font newFont = Font.font(fontName, fontWeight, fontPosture, fontSize);
            text.setFont(newFont);
        });

        // Create a StackPane to hold the text
        StackPane textPane = new StackPane(text);
        textPane.setPrefHeight(100);
        textPane.setMinHeight(100);

        // Create a Slider to modify the font size
        Slider sizeSlider = new Slider(8, 48, fontSize);
        sizeSlider.setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(sizeSlider, Priority.ALWAYS);
        sizeSlider.setMaxWidth(Double.MAX_VALUE);
        HBox sizeBox = new HBox(10, new Label("Size:"), sizeSlider);

        // Update the font size when the sizeSlider is modified
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            fontSize = newValue.doubleValue();
            Font newFont = Font.font(fontName, fontWeight, fontPosture, fontSize);
            text.setFont(newFont);
        });

        // Create a Slider to modify the font blurring
        Slider blurSlider = new Slider(0, 10, fontBlur.getRadius());
        blurSlider.setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(blurSlider, Priority.ALWAYS);
        blurSlider.setMaxWidth(Double.MAX_VALUE);
        HBox blurBox = new HBox(10, new Label("Blur:"), blurSlider);

        // Update the font blurring when the blurSlider is modified
        blurSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            fontBlur.setRadius(newValue.doubleValue());
            text.setEffect(fontBlur);
        });

        // Create a CheckBox to toggle underline
        CheckBox underlineCheck = new CheckBox("Underline");
        underlineCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            text.setUnderline(newValue);
        });

        // Create a CheckBox to toggle strikethrough
        CheckBox strikethroughCheck = new CheckBox("Strikethrough");
        strikethroughCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            text.setStrikethrough(newValue);
        });

        // Create RadioButtons to select font color
        ToggleGroup colorGroup = new ToggleGroup();
        RadioButton blackButton = new RadioButton("Black");
        blackButton.setUserData(Color.BLACK);
        blackButton.setToggleGroup(colorGroup);
        blackButton.setSelected(true);
        RadioButton redButton = new RadioButton("Red");
        redButton.setUserData(Color.RED);
        redButton.setToggleGroup(colorGroup);
        RadioButton blueButton = new RadioButton("Blue");
        blueButton.setUserData(Color.BLUE);
        blueButton.setToggleGroup(colorGroup);

        // Update the font color when a radio button is selected
        colorGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fontColor = (Color) newValue.getUserData();
                text.setFill(fontColor);
            }
        });

        // Create RadioButtons to select font weight
        ToggleGroup weightGroup = new ToggleGroup();
        RadioButton normalButton = new RadioButton("Normal");
        normalButton.setUserData(FontWeight.NORMAL);
        normalButton.setToggleGroup(weightGroup);
        normalButton.setSelected(true);
        RadioButton boldButton = new RadioButton("Bold");
        boldButton.setUserData(FontWeight.BOLD);
        boldButton.setToggleGroup(weightGroup);

        // Update the font weight when a radio button is selected
        weightGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fontWeight = (FontWeight) newValue.getUserData();
                Font newFont = Font.font(fontName, fontWeight, fontPosture, fontSize);
                text.setFont(newFont);
            }
        });

        // Create RadioButtons to select font posture
        ToggleGroup postureGroup = new ToggleGroup();
        RadioButton regularButton = new RadioButton("Regular");
        regularButton.setUserData(FontPosture.REGULAR);
        regularButton.setToggleGroup(postureGroup);
        regularButton.setSelected(true);
        RadioButton italicButton = new RadioButton("Italic");
        italicButton.setUserData(FontPosture.ITALIC);
        italicButton.setToggleGroup(postureGroup);

        // Update the font posture when a radio button is selected
        postureGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fontPosture = (FontPosture) newValue.getUserData();
                Font newFont = Font.font(fontName, fontWeight, fontPosture, fontSize);
                text.setFont(newFont);
            }
        });

        // Create a VBox to hold the controls
        VBox controlsBox = new VBox(10, sizeBox, blurBox, underlineCheck, strikethroughCheck,
                new Label("Color:"), blackButton, redButton, blueButton,
                new Label("Weight:"), normalButton, boldButton,
                new Label("Posture:"), regularButton, italicButton);
        controlsBox.setPadding(new Insets(10));
        controlsBox.setPrefWidth(300);

        // Set the VBox to grow vertically
        VBox.setVgrow(familiesListView, Priority.ALWAYS);

        // Create a VBox to hold the font names and controls
        VBox fontNamesBox = new VBox(10, searchField, familiesListView, countView, percentageView);

        // Create an HBox to hold the font names and controls
        HBox upperPane = new HBox(10, fontNamesBox, controlsBox);

        // Create a VBox to hold the upperPane and textPane
        VBox root = new VBox(10, upperPane, textPane);

        // Create a Scene with the root VBox
        Scene scene = new Scene(root, 700, 500);

        // Set the title and scene of the stage
        stage.setTitle("Font Select");
        stage.setScene(scene);
        stage.show();

        //////////////////////////////////////////
        // behavior

        // set font to focused list item (Done)

        // filter list as text is entered in search field
        // (ignore case, show all fonts whose name contains the search text) (Done)

        // update the countView to show the number of fonts remaining in the list (Done)

        // update the percentageView to show the percentage of fonts remaining) (Done)

        // update font size as sizeSlider is modified (Done)

        // update underline through checkbox (Done)

        // update strikethrough through checkbox (Done)

        // update font color with radio buttons (Done)

        // update font weight with radio buttons (Done)

        // update font posture with radio buttons (Done)

        // update font blurring as slider is modified (Done)

    }

}
