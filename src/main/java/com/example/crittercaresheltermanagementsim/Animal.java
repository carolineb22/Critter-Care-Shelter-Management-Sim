package com.example.crittercaresheltermanagementsim;

class Animal {
    String name;
    String type;
    String imageFile;
    int happiness;
    int appearance;
    int obedience;
    int adoptability;
    String age;

    public Animal(String name, String type, String imageFile, int happiness, int appearance, int obedience, int adoptability, String age) {
        this.name = name;
        this.type = type;
        this.imageFile = imageFile;
        this.happiness = happiness;
        this.appearance = appearance;
        this.obedience = obedience;
        this.adoptability = adoptability;
        this.age = age;
    }

    // Optional: Override toString() for easy saving to file
    @Override
    public String toString() {
        return name + "," + type + "," + imageFile + "," + happiness + "," + appearance + "," + obedience + "," + adoptability;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImageFile() {
        return imageFile;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getAppearance() {
        return appearance;
    }

    public int getObedience() {
        return obedience;
    }

    public int getAdoptability() {
        return adoptability;
    }

    public String getAge() {
        return age;
    }

    public void setName(String newName) {
    }

    public void setDescription(String newType) {}
}

