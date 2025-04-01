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
import java.util.*;

public class MainGame {
    private GridPane gridPane;
    private Scene mainGameScene;  // Store the scene reference
    private Stage primaryStage;   // Store the primaryStage reference
    private String shelterName;
    private Text titleText;
    AnimalIntakes animalIntakes = new AnimalIntakes(this, primaryStage);
    int currentRating = 2;
    int currentFunds = 5000;
    private List<Button> animalButtons = new ArrayList<>();
    private List<String> acceptedAnimals = new ArrayList<>();
    private int availableSlots = 12;

    public void mainScene(Stage primaryStage, String shelterName) {
        this.primaryStage = primaryStage;
        this.shelterName = shelterName; // Store name in instance variable

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

        loadAcceptedAnimals();

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

    public void addAcceptedAnimal(String name, String type, String imageFile) {
        // Add animal to the UI
        for (Button button : animalButtons) {
            if (button.getText().equals("VACANT")) {
                button.setText(name);
                button.setGraphic(getAnimalImage(imageFile));
                break; // Stop after updating one slot
            }
        }

        // Save animal to the "AcceptedAnimals.txt" file
        saveAcceptedAnimals();
    }

    private void saveAcceptedAnimals() {
        List<String> allAnimals = new ArrayList<>();
        for (Button button : animalButtons) {
            if (!button.getText().equals("VACANT")) {
                String animalName = button.getText();
                String imageFile = getAnimalImageFile(button); // Assuming you have a method to get the image file name
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AcceptedAnimals.txt"))) {
            for (String animal : allAnimals) {
                writer.write(animal);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving accepted animals: " + e.getMessage());
        }
    }

    public void loadAcceptedAnimals() {
        File file = new File("AcceptedAnimals.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String[]> savedAnimals = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] animalData = line.split(",");
                if (animalData.length == 5) { // Ensure all fields exist
                    savedAnimals.add(animalData);
                }
            }

            // Load saved animals into the shelter buttons
            for (int i = 0; i < savedAnimals.size(); i++) {
                if (i < animalButtons.size()) {
                    String name = savedAnimals.get(i)[0];
                    String imageFile = savedAnimals.get(i)[4];

                    animalButtons.get(i).setText(name);
                    animalButtons.get(i).setGraphic(getAnimalImage(imageFile));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getAnimalImageFile(Button button) {
        // Add your logic here to return the correct image filename based on button's graphic.
        // You can adjust this as per your image naming logic.
        return "dog1.png"; // Example, you'll need to implement dynamic file retrieval.
    }

    // Add animal to first available button
    public void addAnimalToShelter(String name, String type, String imageFile) {
        if (hasAvailableSlots()) {
            availableSlots--;

            for (Button button : animalButtons) {
                if (button.getText().equals("VACANT")) {
                    // Update UI
                    button.setText(name);
                    button.setGraphic(getAnimalImage(imageFile));

                    // Save immediately to file
                    animalIntakes.saveAnimalToFile(name, type, imageFile);

                    return;
                }
            }
        } else {
            System.out.println("Can't add animal to shelter.");
        }
    }


    // Get random image for the animal type
    private ImageView getAnimalImage(String imageFile) {
        String imagePath = "/images/" + imageFile;

        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            System.err.println("Image not found: " + imagePath);
            return new ImageView();
        }

        Image image = new Image(imageStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        return imageView;
    }

    public void resetGameData() {
        File file = new File("AcceptedAnimals.txt");
        File file2 = new File("CritterCareSave.txt");

        // Delete file1 if it exists
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Game reset: AcceptedAnimals.txt deleted.");
            } else {
                System.err.println("Failed to delete AcceptedAnimals.txt.");
            }
        }
        // Delete file2 if it exists
        if (file2.exists()) {
            if (file2.delete()) {
                System.out.println("Game reset: AcceptedAnimals.txt deleted.");
            } else {
                System.err.println("Failed to delete AcceptedAnimals.txt.");
            }
        }

        // Reset animal buttons
        for (Button button : animalButtons) {
            button.setText("VACANT");
            button.setGraphic(null);
        }
    }

    public boolean hasAvailableSlots() {
        return availableSlots > 0;
    }

    public void resetAvailableSlots() {
        availableSlots = 12; // Reset to full slots if needed
    }

}
