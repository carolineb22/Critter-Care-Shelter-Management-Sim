package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGame {
    private GridPane gridPane;
    private Scene mainGameScene;  // Store the scene reference
    private Stage primaryStage;   // Store the primaryStage reference
    private String shelterName;
    private Text titleText;
    AnimalIntakes animalIntakes;
    int currentRating = 2;
    int currentFunds = 5000;
    private List<Button> animalButtons = new ArrayList<>();

    public void mainScene(Stage primaryStage, String shelterName) {
        this.primaryStage = primaryStage;
        this.shelterName = shelterName; // Store name in instance variable
        loadAcceptedAnimals();

        AnimalIntakes animalIntakes = new AnimalIntakes(this, primaryStage);

        gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 800, 600);

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        // Define column and row constraints
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
        titleBox.setPrefHeight(45);
        titleBox.setMinHeight(45);
        titleBox.setMaxHeight(45);

        // Shelter Name Text
        titleText = new Text(shelterName);
        titleText.getStyleClass().add("title-text");

        // Edit Button (Pencil Icon)
        Button editButton = new Button();
        editButton.getStyleClass().add("edit-button");
        ImageView pencilIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        pencilIcon.setFitWidth(20);
        pencilIcon.setFitHeight(20);
        editButton.setGraphic(pencilIcon);
        editButton.setOnAction(e -> openRenameDialog());

        // Flexible space to push button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

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

        // Save & Return to Main Menu button
        Button saveAndReturnButton = new Button("↩ Menu");
        saveAndReturnButton.getStyleClass().add("small-menu-button");
        saveAndReturnButton.setOnAction(e -> new MainMenu().start(primaryStage));

        HBox menuBox = new HBox(saveAndReturnButton);
        menuBox.setAlignment(Pos.TOP_LEFT);
        menuBox.setPadding(new Insets(-10));
        gridPane.add(menuBox, 0, 0);

        // Feature Buttons
        Button upgradeButton = new Button("Upgrades");
        Button staffButton = new Button("Staff");
        Button adoptionApplicationsButton = new Button("Adoption\nApplications");
        Button animalIntakeButton = new Button("Animal\nIntakes");

        animalIntakeButton.setOnAction(e -> animalIntakes.animalIntakeScene());

        upgradeButton.getStyleClass().add("menu-button");
        staffButton.getStyleClass().add("menu-button");
        adoptionApplicationsButton.getStyleClass().add("menu-button");
        animalIntakeButton.getStyleClass().add("menu-button");

        // Add elements to the grid
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

        // Generate vacant animal slots
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 2; col++) {
                Button animalButton = new Button("VACANT"); // Create a new button for each slot
                animalButton.getStyleClass().add("animal-button");
                animalButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                gridPane.add(animalButton, 5 + col, row);
                animalButtons.add(animalButton); // ✅ Store the button in the list
            }
        }

        this.mainGameScene = scene;  // Store scene reference
        // Set scene properties
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Maximize window properly
        primaryStage.setMaximized(true);
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

    public Scene getMainGameScene() {
        return this.mainGameScene;
    }

    public void addAcceptedAnimal(String name) {
        gridPane = new GridPane();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button animalButton = (Button) node;
                if (animalButton.getText().equals("VACANT")) {
                    animalButton.setText(name);
                    return; // Stop after updating one slot
                }
            }
        }
    }

    private void loadAcceptedAnimals() {
        File file = new File("AcceptedAnimals.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    addAcceptedAnimal(data[0]); // Use only the name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add animal to first available button
    public void addAnimalToShelter(String name, String type) {
        for (Button button : animalButtons) {
            if (button.getText().equals("VACANT")) {
                button.setText(name); // Set the button text to the animal's name
                button.setGraphic(getAnimalImage(type)); // Set the button graphic (image)
                return;
            }
        }
    }

    // Get random image for the animal type
    private ImageView getAnimalImage(String type) {
        Random random = new Random();
        int imgNum = random.nextInt(3) + 1; // Picks 1, 2, or 3
        String imagePath = "/" + type.toLowerCase() + imgNum + ".png"; // Example: "/dog2.png"

        // Load the image (Ensure images are in 'resources' folder)
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);

        // Resize image for button
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        return imageView;
    }


}
