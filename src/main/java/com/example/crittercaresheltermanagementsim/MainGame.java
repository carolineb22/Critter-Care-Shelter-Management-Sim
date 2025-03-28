package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class MainGame {
    int currentFunds = 5000;
    int currentRating = 5;
    private String shelterName;  // Store current shelter name
    private Text titleText;      // Reference to update UI title

    public void mainScene(Stage primaryStage, String shelterName) {
        this.shelterName = shelterName; // Store name in instance variable

        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 800, 600);

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth((i >= 5) ? 15 : 10);
            column.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(column);
        }

        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(16);
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }

        // Title Box (Rounded Rectangle with Edit Button)
        HBox titleBox = new HBox();
        titleBox.getStyleClass().add("title-container");
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(10);
        titleBox.setPrefHeight(45);  // Set a fixed height to make it thinner
        titleBox.setMinHeight(45);
        titleBox.setMaxHeight(45);

        // Shelter Name Text
        titleText = new Text(shelterName);
        titleText.getStyleClass().add("title-text");

        // Edit Button (Pencil Icon)
        Button editButton = new Button();
        editButton.getStyleClass().add("edit-button");

        // Flexible space to push button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Load pencil icon image
        ImageView pencilIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        pencilIcon.setFitWidth(20);
        pencilIcon.setFitHeight(20);
        editButton.setGraphic(pencilIcon);

        editButton.setOnAction(e -> openRenameDialog());

        // Add elements to the title container
        titleBox.getChildren().addAll(titleText, spacer, editButton);
        gridPane.add(titleBox, 0, 0, 3, 1);

        // Funds Box
        VBox fundsBox = new VBox();
        fundsBox.getStyleClass().add("info-box");
        fundsBox.setAlignment(Pos.CENTER);
        Text currentFundsText = new Text("Funds: $" + currentFunds);
        currentFundsText.getStyleClass().add("funds-text");
        fundsBox.getChildren().add(currentFundsText);

        // Rating Box
        VBox ratingBox = new VBox();
        ratingBox.getStyleClass().add("info-box");
        ratingBox.setAlignment(Pos.CENTER);
        String stars = "⭐".repeat(currentRating);
        Text currentRatingText = new Text("Rating: " + currentRating + stars);
        currentRatingText.getStyleClass().add("rating-text");
        ratingBox.getChildren().add(currentRatingText);

        Button upgradeButton = new Button("Upgrades");
        Button staffButton = new Button("Staff");
        Button adoptionApplicationsButton = new Button("Adoption\nApplications");
        Button animalIntakeButton = new Button("Animal\nIntakes");

        upgradeButton.getStyleClass().add("menu-button");
        staffButton.getStyleClass().add("menu-button");
        adoptionApplicationsButton.getStyleClass().add("menu-button");
        animalIntakeButton.getStyleClass().add("menu-button");

        gridPane.add(fundsBox, 0, 1, 3, 1);
        gridPane.add(ratingBox, 2, 1, 3, 1);
        gridPane.add(upgradeButton, 0, 3, 2, 1);
        gridPane.add(staffButton, 2, 3, 2, 1);
        gridPane.add(adoptionApplicationsButton, 0, 4, 2, 1);
        gridPane.add(animalIntakeButton, 2, 4, 2, 1);

        upgradeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        staffButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        adoptionApplicationsButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        animalIntakeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 2; col++) {
                Button animalButton = new Button("VACANT");
                animalButton.getStyleClass().add("animal-button");
                animalButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(animalButton, 5 + col, row);
            }
        }

        double screenWidth = javafx.stage.Screen.getPrimary().getBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getBounds().getHeight();

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setMinWidth(screenWidth);
        primaryStage.setMinHeight(screenHeight);
        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.show();
    }

    private void openRenameDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Rename Shelter");

        VBox dialogVBox = new VBox();
        dialogVBox.getStyleClass().add("rename-dialog");

        Text dialogText = new Text("Enter a new name for your shelter:");
        dialogText.getStyleClass().add("dialog-text");

        TextField nameField = new TextField(shelterName);
        nameField.getStyleClass().add("dialog-textfield");
        nameField.setPromptText("New Shelter Name");

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("dialog-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("dialog-button");

        confirmButton.setOnAction(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty() && newName.length() <= 40) {
                shelterName = newName;
            } else if (newName.length() > 40) {
                shelterName = newName.substring(0, 40);
            } else {
                shelterName = "Pawsville Animal Shelter";
            }
            titleText.setText(shelterName);
            saveShelterName(shelterName);
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        dialogVBox.getChildren().addAll(dialogText, nameField, buttonBox);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(15);
        dialogVBox.setPadding(new Insets(20));

        Scene dialogScene = new Scene(dialogVBox, 350, 200);
        dialogScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }


    private void saveShelterName(String shelterName) {
        try (FileOutputStream outputStream = new FileOutputStream("CritterCareSave.txt")) {
            outputStream.write(shelterName.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
