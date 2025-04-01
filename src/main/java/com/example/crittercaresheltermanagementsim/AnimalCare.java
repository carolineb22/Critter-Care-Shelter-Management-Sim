package com.example.crittercaresheltermanagementsim;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnimalCare{

    private Stage primaryStage;
    private VBox mainLayout;  // Main layout of the primary stage
    MainGame mainGame;


    void openAnimalDetailsScene(Animal animal) {
        VBox animalDetailsBox = new VBox(10);
        animalDetailsBox.setAlignment(Pos.CENTER);
        animalDetailsBox.setPadding(new Insets(20));

        Text nameText = new Text("Name: " + animal.getName());
        Text typeText = new Text("Type: " + animal.getType());
        Text happinessText = new Text("Happiness: " + animal.getHappiness());
        Text appearanceText = new Text("Appearance: " + animal.getAppearance());
        Text obedienceText = new Text("Obedience: " + animal.getObedience());
        Text adoptabilityText = new Text("Adoptability: " + animal.getAdoptability());

        ImageView animalImageView = mainGame.getAnimalImage(animal.getImageFile());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(mainGame.getMainGameScene()));

        animalDetailsBox.getChildren().addAll(
                animalImageView,
                nameText, typeText, happinessText, appearanceText, obedienceText, adoptabilityText,
                backButton
        );

        Scene animalDetailsScene = new Scene(animalDetailsBox, 400, 300);
        primaryStage.setScene(animalDetailsScene);
    }


    public void showAnimalDetails(Animal animal) {
        // Clear the main layout for new content
        mainLayout.getChildren().clear();

        // Left side (Image, Name, Stats)
        VBox leftBox = new VBox(10);
        leftBox.setPrefWidth(400);

        // Animal Image
        ImageView animalImage = new ImageView(new Image("file:images/" + animal.getImageFile()));  // Change path as needed
        animalImage.setFitWidth(150);
        animalImage.setFitHeight(150);
        leftBox.getChildren().add(animalImage);

        // Animal Name (with edit button)
        HBox nameBox = new HBox(10);
        Label nameLabel = new Label(animal.getName());
        Button editNameButton = new Button("✏️");
        editNameButton.setOnAction(e -> {
            // Handle name editing (e.g., show a text field)
            TextInputDialog nameDialog = new TextInputDialog(animal.getName());
            nameDialog.setHeaderText("Edit Animal Name");
            nameDialog.showAndWait().ifPresent(newName -> nameLabel.setText(newName));
        });
        nameBox.getChildren().addAll(nameLabel, editNameButton);
        leftBox.getChildren().add(nameBox);

        // Animal Stats (Happiness, Obedience, Appearance, Adoptability)
        HBox statsBox = new HBox(10);
        statsBox.getChildren().addAll(
                new Label("Happiness: " + animal.getHappiness() + "/10"),
                new Label("Obedience: " + animal.getObedience() + "/10"),
                new Label("Appearance: " + animal.getAppearance() + "/10"),
                new Label("Adoptability: " + animal.getAdoptability() + "/10")
        );
        leftBox.getChildren().add(statsBox);

        // Right side (Description Box, Actions)
        VBox rightBox = new VBox(10);
        rightBox.setPrefWidth(400);

        // Description Box
        VBox descriptionBox = new VBox(10);
        descriptionBox.getChildren().add(new Label("Description"));
        descriptionBox.getChildren().add(new Label("Name: " + animal.getName()));
        descriptionBox.getChildren().add(new Label("Type: " + animal.getType()));
        descriptionBox.getChildren().add(new Label("Age: " + animal.getAge()));
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Write a description for the animal...");
        descriptionBox.getChildren().add(descriptionArea);

        // Animal Actions
        VBox actionsBox = new VBox(10);
        HBox actionBox = new HBox(10);
        actionBox.getChildren().add(new Button("Status: " + getAnimalStatus(animal.getHappiness())));
        actionsBox.getChildren().add(actionBox);
        rightBox.getChildren().addAll(descriptionBox, actionsBox);

        // Add both left and right sections to the main layout
        HBox detailsBox = new HBox(20, leftBox, rightBox);
        mainLayout.getChildren().add(detailsBox);
    }

    private String getAnimalStatus(int happiness) {
        if (happiness >= 8) {
            return "Great";
        } else if (happiness >= 5) {
            return "Good";
        } else {
            return "Terrible";
        }
    }
}
