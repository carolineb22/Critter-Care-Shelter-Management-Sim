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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalIntakes {
    private final MainGame mainGame;
    private final Stage primaryStage;
    private List<HBox> animalEntries = new ArrayList<>();
    private long lastUpdateTime = 0;

    public AnimalIntakes(MainGame mainGame, Stage primaryStage) {
        this.mainGame = mainGame;
        this.primaryStage = primaryStage;
    }

    public void animalIntakeScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #A7D3E8;");

        Text title = new Text("Animal Intakes");
        title.setFont(Font.font("Comic Sans MS", 30));
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.setMaxHeight(50);

        long currentTime = System.currentTimeMillis();
        if (animalEntries.isEmpty() || (currentTime - lastUpdateTime) >= 10 * 60 * 1000) {
            generateNewAnimals();
            lastUpdateTime = currentTime;
        }

        VBox animalList = new VBox(10);
        animalList.setPadding(new Insets(20));
        animalList.setAlignment(Pos.TOP_CENTER);
        animalList.getChildren().addAll(animalEntries);

        ScrollPane scrollPane = new ScrollPane(animalList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        Button backButton = new Button("↩ Back");
        backButton.setFont(Font.font("Comic Sans MS", 14));
        backButton.setOnAction(e -> primaryStage.setScene(mainGame.getMainGameScene()));

        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(10));

        root.setTop(titleBox);
        root.setCenter(scrollPane);
        root.setLeft(backBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateNewAnimals() {
        animalEntries.clear();
        for (int i = 0; i < 10; i++) {
            animalEntries.add(createAnimalEntry());
        }
    }

    private HBox createAnimalEntry() {
        Random random = new Random();
        String[] names = {"Bubbles", "Midnight", "Tucker", "Raspberry", "Cocoa", "Snowball", "Bella", "Duke", "Ash", "Benny"};
        String[] animalType = {"Dog", "Bunny", "Cat"};
        String[] statuses = {"Terrible", "Poor", "Average", "Good", "Great"};

        String name = names[random.nextInt(names.length)];
        String type = animalType[random.nextInt(animalType.length)];
        String status = statuses[random.nextInt(statuses.length)];
        String age = random.nextInt(12) + " months";

        Text nameText = new Text(name);
        Text breedText = new Text("Breed: " + type);
        Text statusText = new Text("Status: " + status);
        Text ageText = new Text("Age: " + age);

        Button acceptButton = new Button("Accept");
        acceptButton.setFont(Font.font("Comic Sans MS", 14));

        HBox animalEntry = new HBox(15, nameText, breedText, statusText, ageText, acceptButton);
        animalEntry.setPadding(new Insets(10));
        animalEntry.setAlignment(Pos.CENTER_LEFT);

        acceptButton.setOnAction(e -> {
            mainGame.addAnimalToShelter(name, type);
            animalEntries.remove(animalEntry);
            animalIntakeScene();
        });

        return animalEntry;
    }
}
