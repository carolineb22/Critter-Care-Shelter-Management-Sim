package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class AnimalCare {
    public static Scene createAnimalDetailsScene(Stage primaryStage, Scene previousScene, Animal animal) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("background");
        root.setPadding(new Insets(10));

        // Top bar with Back Button
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5));

        // Back Button
        Button backButton = new Button("← Menu");
        backButton.setFont(Font.font("Comic Sans MS", 14));
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        topBar.getChildren().addAll(backButton);
        root.setTop(topBar);

        // Left Side: Image and Basic Info
        VBox leftBox = new VBox(10);
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.setPadding(new Insets(10));

        // Image
        String imagePath = "/images/" + animal.getImageFile(); // e.g., "dog1.png"
        Image image = new Image(AnimalCare.class.getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        imageView.getStyleClass().add("animal-image");

        HBox nameBox = new HBox();
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setSpacing(10);
        nameBox.setPadding(new Insets(5));
        nameBox.setStyle("-fx-background-color: white; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: lightgray;");

        TextField nameField = new TextField(animal.getName());
        nameField.setEditable(false);
        nameField.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-font-size: 16px; -fx-font-weight: bold;");

        ImageView pencilIcon1 = new ImageView(new Image(AnimalCare.class.getResource("/pencil.png").toExternalForm()));
        pencilIcon1.setFitWidth(20);
        pencilIcon1.setFitHeight(20);

        Button editNameButton = new Button();
        editNameButton.setGraphic(pencilIcon1);
        editNameButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        editNameButton.setCursor(Cursor.HAND);

        HBox.setHgrow(nameField, Priority.ALWAYS); // Make the text field expand

        nameBox.getChildren().addAll(nameField, editNameButton);

        System.out.println("Name box children: " + nameBox.getChildren().size());

        editNameButton.setOnAction(e -> openRenameDialog(animal, nameField));

        VBox statsBox = new VBox(5);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.getChildren().addAll(
                createStatLabel("Happiness: " + animal.getHappiness() + "/10"), // Use actual animal stats
                createStatLabel("Obedience: " + animal.getObedience() + "/10"),
                createStatLabel("Appearance: " + animal.getAppearance() + "/10"),
                createStatLabel("Adoptability: " + animal.getAdoptability() + "/10")
        );

        leftBox.getChildren().addAll(imageView, nameBox, statsBox);
        root.setLeft(leftBox);

        // Right section (Description)
        VBox rightBox = new VBox(20);
        rightBox.setPadding(new Insets(10));

        Label descLabel = new Label("Description");
        descLabel.setFont(Font.font(18));
        VBox.setMargin(descLabel, new Insets(0, 0, 0, 152));
        descLabel.getStyleClass().add("section-label");

        TextArea descriptionArea = new TextArea(
                "Name: " + animal.getName() + "\n" +
                        "Age: " + animal.getAge() + "\n" + // Assuming the Animal class has an `getAge()` method
                        "Breed: " + animal.getType() + "\n"
        );
        descriptionArea.setEditable(false);
        descriptionArea.setPrefHeight(120);

        VBox descriptionContainer = new VBox(5);
        descriptionContainer.setStyle(
                "-fx-background-color: #E9C46A; " +   // Set the background color to yellow
                        "-fx-background-radius: 10px; " +     // Set rounded corners
                        "-fx-padding: 10px;"                  // Add padding to give space inside the box
        );


        descriptionArea.getStyleClass().add("description-area");
        HBox.setMargin(descriptionArea, new Insets(0, 0, 0, 150)); // Move it slightly to the right

        HBox descBox = new HBox(5, descriptionArea);
        root.setCenter(rightBox);

        // Bottom section (Actions)
        VBox actionsBox = new VBox(5);
        actionsBox.setPadding(new Insets(10));
        actionsBox.setSpacing(5);

        // Add action boxes
        actionsBox.getChildren().addAll(
                createStatusActionRow("Status: Good", "FEED"),
                createStatusActionRow("Status: False", "VACCINATE"),
                createStatusActionRow("Status: Poor", "VET"),
                createStatusActionRow("Status: Terrible", "PLAY"),
                createStatusActionRow("Status: Great", "GROOM"),
                createStatusActionRow("Status: In training", "TRAIN")
        );

        rightBox.getChildren().addAll(descLabel, descBox, actionsBox);
        root.setCenter(rightBox);


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(AnimalCare.class.getResource("/style.css").toExternalForm());
        return scene;
    }

    private static HBox createStatusActionRow(String status, String action) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(5));
        row.getStyleClass().add("status-row");

        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().add("status-label");
        Button actionButton = new Button(action);
        actionButton.getStyleClass().add("action-button");

        row.getChildren().addAll(statusLabel, actionButton);
        return row;
    }

    private static Label createStatLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("stat-label");
        return label;
    }

    static Animal getAnimalFromSaveFileByIndex(int index) {
        File file = new File("AcceptedAnimals.txt"); // Ensure correct path if needed

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int currentLine = 0;

            while ((line = br.readLine()) != null) {
                if (currentLine == index) { // When the line matches the index
                    String[] parts = line.split(",");
                    if (parts.length == 14) { // Expecting 14 values now (including new attributes)

                        // Parse the data from the split line
                        String name = parts[0];
                        String type = parts[1];
                        String imageFile = parts[2];
                        int happiness = Integer.parseInt(parts[3]);
                        int appearance = Integer.parseInt(parts[4]);
                        int obedience = Integer.parseInt(parts[5]);
                        int adoptability = Integer.parseInt(parts[6]);
                        String age = parts[7];
                        boolean fedStatus = Boolean.parseBoolean(parts[8]); // Parse as boolean
                        boolean vaccinatedStatus = Boolean.parseBoolean(parts[9]); // Parse as boolean
                        String healthStatus = parts[10];
                        String playStatus = parts[11];
                        String groomStatus = parts[12];
                        String trainingStatus = parts[13];

                        System.out.println("Successfully retrieved: " + name + " from file!");

                        return new Animal(name, type, imageFile, happiness, appearance, obedience, adoptability,
                                age, fedStatus, vaccinatedStatus, healthStatus, playStatus, groomStatus, trainingStatus);

                    } else {
                        System.out.println("ERROR: Incorrect number of attributes in line: " + line);
                    }
                }
                currentLine++; // Increment line number
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("WARNING: No animal found at index " + index);
        return null; // Return null if no match found
    }

    private static void openRenameDialog(Animal animal, TextField nameField) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Rename " + animal.getName());

        VBox dialogVBox = new VBox();
        dialogVBox.getStyleClass().add("rename-dialog");

        Text dialogText = new Text("Enter a new name for this animal:");
        dialogText.getStyleClass().add("dialog-text");

        TextField nameInputField = new TextField(animal.getName());
        nameInputField.getStyleClass().add("dialog-textfield");
        nameInputField.setPromptText("New Animal Name");

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("dialog-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("dialog-button");

        confirmButton.setOnAction(e -> {
            String newName = nameInputField.getText().trim();
            if (!newName.isEmpty() && newName.length() <= 40) {
                animal.setName(newName);
                nameField.setText(newName);

                // Save the updated name back to the file
                updateAnimalNameInFile(animal);

                dialogStage.close();
            } else if (newName.length() > 40) {
                animal.setName(newName.substring(0, 40));
                nameField.setText(newName.substring(0, 40));

                // Save the updated name back to the file
                updateAnimalNameInFile(animal);

                dialogStage.close();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        dialogVBox.getChildren().addAll(dialogText, nameInputField, buttonBox);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(15);
        dialogVBox.setPadding(new Insets(20));

        Scene dialogScene = new Scene(dialogVBox, 350, 200);
        dialogScene.getStylesheets().add(AnimalCare.class.getResource("/style.css").toExternalForm());
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private static void updateAnimalNameInFile(Animal animal) {
        File file = new File("AcceptedAnimals.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;  // To track if we find the animal to update
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8 && parts[0].equals(animal.getName())) {
                    // Update the animal's name in the corresponding line
                    line = line.replace(parts[0], animal.getName());
                    updated = true;
                }
                updatedContent.append(line).append("\n");
            }

            // Check if the name was updated
            if (updated) {
                // Write the updated content back to the file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(updatedContent.toString());
                    System.out.println("File updated successfully.");
                }
            } else {
                System.out.println("No matching animal found in the file to update.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
