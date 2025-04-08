package com.example.crittercaresheltermanagementsim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

import static com.example.crittercaresheltermanagementsim.AnimalCare.updateAnimalStatusInFile;

public class GameTimeManager {
    private int hour = 6; // Start at 6:00 AM
    private int minute = 0;
    private boolean isPaused = false;
    private int currentDay;
    private MainGame mainGame;  // Ensure this is always initialized
    Animal animal;

    private Label timeLabel;
    private Timeline timeline;

    // Constructor: Requires MainGame instance
    public GameTimeManager(MainGame mainGame, Label timeLabel) {
        this.mainGame = mainGame;
        this.timeLabel = timeLabel;
        this.currentDay = mainGame.getDayCount(); // Load the saved day
        startTimer();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        if (!isPaused) {
            minute++;
            if (minute == 60) {
                minute = 0;
                hour++;
            }

            updateTimeLabel();

            if (hour == 9) { // 9:00 PM triggers end-of-day event
                timeline.pause();
                if (mainGame != null) {
                    showEndOfDayPopup(mainGame.getShelterName());
                } else {
                    System.out.println("ERROR: mainGame is null! Cannot get shelter name.");
                }
            }
        }
    }

    private void updateTimeLabel() {
        String amPm = (hour < 12) ? "AM" : "PM";
        int displayHour = (hour > 12) ? hour - 12 : hour;
        if (displayHour == 0) displayHour = 12;
        String timeString = String.format("%02d:%02d %s", displayHour, minute, amPm);

        Platform.runLater(() -> timeLabel.setText("Time: " + timeString));
    }

    private void showEndOfDayPopup(String shelterName) {
        Stage endOfDayStage = new Stage();
        endOfDayStage.setTitle("END OF DAY");

        endOfDayStage.setWidth(400);
        endOfDayStage.setHeight(250);
        endOfDayStage.setMaxWidth(400);
        endOfDayStage.setMaxHeight(250);
        endOfDayStage.setResizable(false);

        Text header = new Text("The day has ended!");
        Text prompt = new Text("Are you ready to continue to the next day?");

        header.setWrappingWidth(350);
        prompt.setWrappingWidth(250);

        header.getStyleClass().add("warning-text");
        prompt.getStyleClass().add("secondary-text");

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            currentDay++; // Increment the day count
            if (mainGame != null) {
                mainGame.saveGameData(shelterName, currentDay); // Save updated day count
                resetFedStatusForNewDay();
                if (this.animal != null) {
                    this.animal.resetDailyFlags();
//                    dayLabel.setText("Day: " + currentDay);
                } else {
                    System.out.println("Animal object is null.");
                }
            } else {
                System.out.println("ERROR: mainGame is null! Cannot save data.");
            }
            hour = 6;  // Reset to 6:00 AM
            minute = 0;
            updateTimeLabel();
            timeline.play();
            endOfDayStage.close();
        });

        VBox layout = new VBox(15, header, prompt, continueButton);
        layout.getStyleClass().add("day-end-window");
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        Scene endOfDayScene = new Scene(root, 600, 400);
        endOfDayScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        endOfDayStage.setScene(endOfDayScene);
        endOfDayStage.show();
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    public HBox createPauseButton() {
        Button pauseButton = new Button("Pause");
        Label dayLabel = new Label("Day: " + currentDay);
        HBox timeBox = new HBox(20, timeLabel, dayLabel, pauseButton);

        pauseButton.setMinWidth(70);
        pauseButton.setPrefWidth(70);
        pauseButton.setMaxWidth(70);
        timeBox.setAlignment(Pos.CENTER_RIGHT);
        timeBox.setPrefWidth(400);
        timeBox.setMinWidth(1000);
        timeBox.setStyle("-fx-padding: -100 -100 0 0;");
        timeLabel.setMaxWidth(Double.MAX_VALUE);

        pauseButton.setStyle(
                "-fx-background-color: #21867A; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-color: black; " +
                        " -fx-border-width: 2px; " +
                        "-fx-font-family: \"Comic Sans MS\";" +
                        "-fx-font-size: 14px;"
        );

        timeLabel.setStyle(
                "-fx-font-weight: bold; " +
                        "-fx-font-family: \"Comic Sans MS\";"
        );

        dayLabel.setStyle(
                "-fx-font-weight: bold; " +
                        "-fx-font-family: \"Comic Sans MS\";"
        );

        pauseButton.setOnAction(e -> {
            togglePause();
            pauseButton.setText(isPaused ? "Resume" : "Pause");
        });

        return timeBox;
    }

    private static void resetFedStatusForNewDay() {
        File file = new File("AcceptedAnimals.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 14) {
                    // Reset fedStatus to false at the start of the new day
//                    parts[8] = "false"; // Assuming the 9th element is fedStatus (index 8)
                    line = line.replace(parts[8], "false");
                }
                updatedContent.append(String.join(",", parts)).append("\n");
            }

            // Write updated data back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(updatedContent.toString());
                System.out.println("All animals' fedStatus reset for the new day.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
