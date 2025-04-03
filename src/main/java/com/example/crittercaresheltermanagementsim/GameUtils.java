package com.example.crittercaresheltermanagementsim;

import java.io.File;
import java.io.IOException;

public class GameUtils {
    // Method to ensure the save file exists
    public static void ensureSaveFileExists() {
        // Create a File object for the save file
        File saveFile = new File("CritterCareSave.txt");
        System.out.println("Save file will be created at: " + saveFile.getAbsolutePath());

        // Check if the file already exists
        if (!saveFile.exists()) {
            try {
                // Attempt to create the new file
                if (saveFile.createNewFile()) {
                    System.out.println("CritterCareSave Save file created.");
                } else {
                    System.out.println("Save file already exists.");
                }
            } catch (IOException e) {
                // Handle any IO exceptions that may occur
                System.out.println("An error occurred while creating the save file.");
                e.printStackTrace();
            }
        }
    }
}
