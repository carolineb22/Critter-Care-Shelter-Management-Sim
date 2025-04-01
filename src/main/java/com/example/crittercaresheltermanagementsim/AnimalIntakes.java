package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalIntakes {
    private final MainGame mainGame;
    private final Stage primaryStage;
    private List<HBox> animalEntries = new ArrayList<>();
    private long lastUpdateTime = 0;
    VBox animalList = new VBox(10);
    private List<String> acceptedAnimals = new ArrayList<>();
    private List<String> animalNames;
    static String animalData;

    public AnimalIntakes(MainGame mainGame, Stage primaryStage) {
        this.mainGame = mainGame;
        this.primaryStage = primaryStage;
        animalNames = new ArrayList<>();
        loadAnimalNames(); // Call the method to load names from the file
    }

    public void animalIntakeScene() {
        animalList.getChildren().clear();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #A7D3E8;");

        // Create the components for the Animal Intake Scene
        // For example, create and add labels, buttons, etc.
        Text title = new Text("Animal Intakes");
        title.setFont(Font.font("Comic Sans MS", 30));
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.setMaxHeight(50);

        // Update animals if necessary (refresh every 10 minutes)
        long currentTime = System.currentTimeMillis();
        if (animalEntries.isEmpty() || (currentTime - lastUpdateTime) >= 10 * 60 * 1000) {
            generateNewAnimals();
            lastUpdateTime = currentTime;
        }


        // Set up the animal list layout
        animalList.setPadding(new Insets(20));
        animalList.setAlignment(Pos.TOP_CENTER);

        // Add the animal entries to the list
        animalList.getChildren().addAll(animalEntries);

        // Create a scrollable pane for the animal list
        ScrollPane scrollPane = new ScrollPane(animalList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        // Back button to return to the main game scene
        Button backButton = new Button("↩ Back");
        backButton.setFont(Font.font("Comic Sans MS", 14));
        backButton.setOnAction(e -> primaryStage.setScene(mainGame.getMainGameScene()));

        // Layout for the back button
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(10));

        // Set up the layout structure of the root pane
        root.setTop(titleBox);
        root.setCenter(scrollPane);
        root.setLeft(backBox);

        // Create the scene and show it
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadAnimalNames() {
        try (BufferedReader reader = new BufferedReader(new FileReader("animal_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                animalNames.add(line.trim()); // Add each name to the list
            }
        } catch (IOException e) {
            System.out.println("Error loading animal names from file: " + e.getMessage());
        }
    }

    private String getRandomName() {
        Random random = new Random();
        return animalNames.get(random.nextInt(animalNames.size())); // Get a random name from the list
    }

    private void generateNewAnimals() {
        animalEntries.clear();
        for (int i = 0; i < 10; i++) {
            animalEntries.add(createAnimalEntry());
        }
    }

    private HBox createAnimalEntry() {
        Random random = new Random();
        String name = getRandomName(); // Get a random name from the list
        String[] animalType = {"Dog", "Bunny", "Cat"};
        String[] statuses = {"Terrible", "Poor", "Average", "Good", "Great"};

        String type = animalType[random.nextInt(animalType.length)];
        String status = statuses[random.nextInt(statuses.length)];
        String age = random.nextInt(12) + " months";

        // Generate a random image based on type
        int imageNum = random.nextInt(3) + 1; // Assuming you have 3 images per type
        String imageFile = type.toLowerCase() + imageNum + ".png"; // Example: "cat3.png"

        Text nameText = new Text(name);
        Text breedText = new Text("Breed: " + type);
        Text statusText = new Text("Status: " + status);
        switch (status) {
            case "Terrible":
                statusText.setFill(javafx.scene.paint.Color.RED);
                break;
            case "Poor":
                statusText.setFill(javafx.scene.paint.Color.ORANGE);
                break;
            case "Good":
                statusText.setFill(javafx.scene.paint.Color.YELLOW);
                break;
            case "Great":
                statusText.setFill(javafx.scene.paint.Color.GREEN);
                break;
        }
        Text ageText = new Text("Age: " + age);

        Button acceptButton = new Button("Accept");
        acceptButton.setFont(Font.font("Comic Sans MS", 14));

        HBox animalEntry = new HBox(15, nameText, breedText, statusText, ageText, acceptButton);
        animalEntry.setPadding(new Insets(10));
        animalEntry.setAlignment(Pos.CENTER_LEFT);

        acceptButton.setOnAction(e -> {
            if (mainGame.hasAvailableSlots())
            {
                acceptedAnimals.add(animalData);  // Store data in memory
                mainGame.addAnimalToShelter(name, type, imageFile);  // Add to shelter UI
                animalEntries.remove(animalEntry);  // Remove from intake list
                updateAnimalList();  // Update animal intake display
            }
            else {
                System.out.println("No available slots");
                slotsFullWarning();
            }

        });

        return animalEntry;
    }

    void slotsFullWarning() {
        Stage tertiaryStage = new Stage();
        tertiaryStage.setTitle("SHELTER SLOTS FULL");

        tertiaryStage.setWidth(400);
        tertiaryStage.setHeight(250);
        tertiaryStage.setMaxWidth(400);
        tertiaryStage.setMaxHeight(250);
        tertiaryStage.setResizable(false);

        Text warning = new Text("Shelter slots are full!");
        Text warning2 = new Text("Upgrade the shelter or adopt out animal to free up spots!");

        warning.setWrappingWidth(350);
        warning2.setWrappingWidth(250);

        Button okButton = new Button("OK");

        warning.getStyleClass().add("warning-text");
        warning2.getStyleClass().add("secondary-text");

        okButton.setOnAction(e -> tertiaryStage.close());

        VBox layout = new VBox(15, warning, warning2, okButton);
        layout.getStyleClass().add("warning-window");
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        Scene secondaryScene = new Scene(root, 600, 400);
        secondaryScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        tertiaryStage.setScene(secondaryScene);
        tertiaryStage.show();
    }

    private void updateAnimalList() {
        // Clear and update the list dynamically
        animalList.getChildren().clear();
        animalList.getChildren().addAll(animalEntries);
    }

    // Save accepted animals to file
    void saveAnimalToFile(String name, String type, String imageFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AcceptedAnimals.txt", true))) {
            writer.write(name + "," + type + "," + imageFile);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving accepted animal: " + e.getMessage());
        }
    }


}
