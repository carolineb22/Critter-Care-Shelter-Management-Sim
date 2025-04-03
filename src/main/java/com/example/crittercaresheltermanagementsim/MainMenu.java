package com.example.crittercaresheltermanagementsim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MainMenu extends Application {
    MainGame mainGame = new MainGame();
    public Stage primaryStage = new Stage();


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Critter Care: Shelter Management Sim");
        System.out.println(getClass().getResource("app_icon.png"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/app_icon.png")));

        // Get the full screen size
        double screenWidth = javafx.stage.Screen.getPrimary().getBounds().getWidth();
        double screenHeight = javafx.stage.Screen.getPrimary().getBounds().getHeight();

        // Set the window size and min size BEFORE showing it
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setMinWidth(screenWidth);
        primaryStage.setMinHeight(screenHeight);
        Platform.runLater(() -> {
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        });


        Text title = new Text("CRITTER CARE");
        title.getStyleClass().add("title");

        Text subtitle = new Text("shelter management sim");
        subtitle.getStyleClass().add("subtitle");


        // Buttons
        Button continueButton = new Button("LOAD SAVE");
        Button newGameButton = new Button("NEW GAME");

        continueButton.getStyleClass().add("menu-button");
        newGameButton.getStyleClass().add("menu-button");

        // Button Actions
        continueButton.setOnAction(e -> checkForSaveFiles());
        newGameButton.setOnAction(e -> newGameWarning());

        VBox menuLayout = new VBox(10, title, subtitle, continueButton, newGameButton);
        menuLayout.setAlignment(Pos.CENTER); // Ensures all elements are centered
        title.setTextAlignment(TextAlignment.CENTER); // Ensures the title itself is centered
        subtitle.setTextAlignment(TextAlignment.CENTER);

        StackPane root = new StackPane(menuLayout);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        Platform.runLater(() -> {
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void newGameWarning() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("NEW GAME WARNING");

        secondaryStage.setWidth(400);
        secondaryStage.setHeight(250);
        secondaryStage.setMaxWidth(400);
        secondaryStage.setMaxHeight(250);
        secondaryStage.setResizable(false);

        Text warning = new Text("Starting a new game will PERMANENTLY DELETE all previous saved progress");
        Text warning2 = new Text("Are you sure you want to start a new game?");

        warning.setWrappingWidth(350);
        warning2.setWrappingWidth(250);

        Button newGameButton = new Button("YES - NEW GAME");
        Button cancelButton = new Button("CANCEL");

        warning.getStyleClass().add("warning-text");
        warning2.getStyleClass().add("secondary-text");

        newGameButton.setOnAction(e -> {
            File file = new File("AcceptedAnimals.txt"); // Change filename as needed

            File file1 = new File("CritterCareSave.txt");
            if (!file1.exists()) {
                try {
                    file1.createNewFile();  // This ensures the file exists before reading it.
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            mainGame.mainScene(primaryStage, "Pawsville Animal Shelter");
            secondaryStage.close();
            mainGame.resetGameData(); // Clear saved data
            mainGame.resetAvailableSlots();
            primaryStage.setScene(mainGame.getMainGameScene()); // Start fresh
            Platform.runLater(() -> {
                primaryStage.setMaximized(false);
                primaryStage.setMaximized(true);
            });
        });
        cancelButton.setOnAction(e -> {
            secondaryStage.close();
        });

        VBox layout = new VBox(15, warning, warning2, newGameButton, cancelButton);
        layout.getStyleClass().add("warning-window");
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        Scene secondaryScene = new Scene(root, 600, 400);
        secondaryScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        secondaryStage.setScene(secondaryScene);

        GameUtils.ensureSaveFileExists();
        secondaryStage.show();
    }

    void checkForSaveFiles() {
        File f = new File("CritterCareSave.txt");
        if (f.isFile() && !f.isDirectory()) { //if files exist
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String shelterName = br.readLine();
                System.out.println("Loaded shelter name: " + shelterName);
                mainGame.mainScene(primaryStage, shelterName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { //if files don't exist
            mainGame.mainScene(primaryStage, "Pawsville Animal Shelter");
        }
    }


}
