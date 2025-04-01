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
import java.util.*;

public class AnimalIntakes {
    private final MainGame mainGame;
    private final Stage primaryStage;
    private List<HBox> animalEntries = new ArrayList<>();
    private long lastUpdateTime = 0;
    VBox animalList = new VBox(10);
    Map<String, Animal> acceptedAnimals = new HashMap<>();
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
        String name = getRandomName();
        String[] animalType = {"Dog", "Bunny", "Cat"};
        String[] statuses = {"Terrible", "Poor", "Average", "Good", "Great"};

        String type = animalType[random.nextInt(animalType.length)];
        String status = statuses[random.nextInt(statuses.length)];

        // Stat Ranges
        int min, max;
        switch (status) {
            case "Terrible":
                min = 1; max = 2;
                break;
            case "Poor":
                min = 2; max = 4;
                break;
            case "Average":
                min = 4; max = 6;
                break;
            case "Good":
                min = 5; max = 7;
                break;
            case "Great":
                min = 6; max = 9;
                break;
            default:
                min = 4; max = 6; // Default case for safety
        }

        // Generate stats
        int happiness = random.nextInt(max - min + 1) + min;
        int appearance = random.nextInt(max - min + 1) + min;
        int obedience = random.nextInt(max - min + 1) + min;
        int adoptability = (happiness + appearance + obedience) / 3;

        String age = random.nextInt(12) + 1 + " months";

        // Generate a random image based on type
        int imageNum = random.nextInt(3) + 1; // Assuming you have 3 images per type
        String imageFile = type.toLowerCase() + imageNum + ".png";

        Text nameText = new Text(name);
        Text typeText = new Text("Type: " + type);
        Text statusText = new Text("Status: " + status);
        Text ageText = new Text("Age: " + age);
        Text happinessText = new Text("Happiness: " + happiness);
        Text appearanceText = new Text("Appearance: " + appearance);
        Text obedienceText = new Text("Obedience: " + obedience);
        Text adoptabilityText = new Text("Adoptability: " + adoptability);

        // Set color based on status
        switch (status) {
            case "Terrible": statusText.setFill(javafx.scene.paint.Color.RED); break;
            case "Poor": statusText.setFill(javafx.scene.paint.Color.ORANGE); break;
            case "Good": statusText.setFill(javafx.scene.paint.Color.YELLOW); break;
            case "Great": statusText.setFill(javafx.scene.paint.Color.GREEN); break;
        }

        Button acceptButton = new Button("Accept");
        acceptButton.setFont(Font.font("Comic Sans MS", 14));

        HBox animalEntry = new HBox(15, nameText, typeText, statusText, ageText,
                happinessText, appearanceText, obedienceText, adoptabilityText, acceptButton);
        animalEntry.setPadding(new Insets(10));
        animalEntry.setAlignment(Pos.CENTER_LEFT);

        acceptButton.setOnAction(e -> {
            if (mainGame.hasAvailableSlots()) {
                Animal animal = new Animal(name, type, imageFile, happiness, appearance, obedience, adoptability);

                // Check if the animal is already accepted before adding
                if (!acceptedAnimals.containsKey(animal.getName())) {
                    acceptedAnimals.put(animal.getName(), animal);  // Store the animal in memory

                    mainGame.addAnimalToShelter(animal);  // Add to shelter UI

                    animalEntries.remove(animalEntry);  // Remove from intake list
                    updateAnimalList();  // Update display

                    saveAnimalToFile(animal); // Save info to file
                } else {
                    System.out.println("This animal has already been accepted.");
                }
            } else {
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

    public void saveAnimalToFile(Animal animal) {
        File file = new File("AcceptedAnimals.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {  // 'true' to append to the file
            // Write animal details only if it's not already in the file
            String animalData = animal.getName() + "," +
                    animal.getType() + "," +
                    animal.getImageFile() + "," +
                    animal.getHappiness() + "," +
                    animal.getAppearance() + "," +
                    animal.getObedience() + "," +
                    animal.getAdoptability();

            // Only write the animal if it's not already in the file
            if (!isAnimalInFile(animal)) {
                bw.write(animalData);
                bw.newLine(); // New line after each animal
            }
            mainGame.removeDuplicatesAndSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to check if an animal is already in the file
    public boolean isAnimalInFile(Animal animal) {
        File file = new File("AcceptedAnimals.txt");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] animalData = line.split(",");
                if (animalData[0].equals(animal.getName())) {
                    return true; // Animal already in the file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
