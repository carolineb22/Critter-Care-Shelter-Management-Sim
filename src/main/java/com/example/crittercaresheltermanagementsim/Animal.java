package com.example.crittercaresheltermanagementsim;


import java.util.Objects;

public class Animal {
    private String name;
    private String type;
    private String imageFile;
    private int happiness;
    private int obedience;
    private int appearance;
    private int adoptability;
    private String description;
    private int age;

    // Original constructor (7 parameters)
    public Animal(String name, String type, String imageFile, int happiness, int obedience, int appearance, int adoptability) {
        this.name = name;
        this.type = type;
        this.imageFile = imageFile;
        this.happiness = happiness;
        this.obedience = obedience;
        this.appearance = appearance;
        this.adoptability = adoptability;
        this.age = 0;  // Default value for age
        this.description = "";  // Default description
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return happiness == animal.happiness &&
                appearance == animal.appearance &&
                obedience == animal.obedience &&
                adoptability == animal.adoptability &&
                name.equals(animal.name) &&
                type.equals(animal.type) &&
                imageFile.equals(animal.imageFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, imageFile, happiness, appearance, obedience, adoptability);
    }


    public String getName() { return name; }
    public String getType() { return type; }
    public String getImageFile() { return imageFile; }
    public int getHappiness() { return happiness; }
    public int getObedience() { return obedience; }
    public int getAppearance() { return appearance; }
    public int getAdoptability() { return adoptability; }
    public int getAge() { return age; }
    public String getDescription() { return description; }


    // Optional: Override toString() for easy saving to file
    @Override
    public String toString() {
        return name + "," + type + "," + imageFile + "," + happiness + "," + appearance + "," + obedience + "," + adoptability + "," + age + "," + description;
    }

}

