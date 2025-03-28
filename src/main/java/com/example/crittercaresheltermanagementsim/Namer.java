package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;

public class Namer {
    MainGame mainGame = new MainGame();

    public void nameShelter(Stage primaryStage) {

        // Main text
        Text textbox = new Text("Congratulations! You are the new owner of the Pawsville local animal shelter." +
                "Take care of the town's stray animals while upgrading the shelter and keeping it afloat!");
        textbox.getStyleClass().add("textbox");
        textbox.setWrappingWidth(600); // Keep text width limited for better formatting

        Text details = new Text("First, choose a name for the shelter!");
        details.getStyleClass().add("details");

        // Text field for entering shelter name
        TextField textField = new TextField();
        textField.getStyleClass().add("textField");
        textField.setPrefWidth(300); // Adjust width to look better
        textField.setPrefHeight(20);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submitButton");

        submitButton.setOnAction(e -> {
            String shelterName = textField.getText().trim();
            if (shelterName.isEmpty()) {
                shelterName = "Pawsville Animal Shelter"; // Default name if empty
            } else if (shelterName.length() > 40) {
                shelterName = shelterName.substring(0, 40); // Limit to 20 characters
            }
            saveShelterName(shelterName);
            mainGame.mainScene(primaryStage, shelterName);
        });

        // Layout: Proper vertical centering
        VBox layout = new VBox(20, textbox, details, textField, submitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefHeight(primaryStage.getHeight()); // Make sure VBox fills the screen

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Get the full screen size
        double screenWidth = javafx.stage.Screen.getPrimary().getBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getBounds().getHeight();

        // Set the window size and min size BEFORE showing it
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setMinWidth(screenWidth);
        primaryStage.setMinHeight(screenHeight);
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Ensure full screen
        primaryStage.show();
    }

    private void saveShelterName(String shelterName) {
        try (FileOutputStream outputStream = new FileOutputStream("CritterCareSave.txt")) {
            outputStream.write(shelterName.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
