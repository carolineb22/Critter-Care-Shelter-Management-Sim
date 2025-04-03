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
    boolean fedStatus;  // Changed to boolean
    boolean vaccinatedStatus;
    String healthStatus;
    String playStatus;
    String groomStatus;
    String trainingStatus;

    // Updated Constructor
    public Animal(String name, String type, String imageFile, int happiness, int appearance, int obedience, int adoptability,
                  String age, boolean fedStatus, boolean vaccinatedStatus, String healthStatus,
                  String playStatus, String groomStatus, String trainingStatus) {
        this.name = name;
        this.type = type;
        this.imageFile = imageFile;
        this.age = age;
        this.fedStatus = fedStatus;
        this.vaccinatedStatus = vaccinatedStatus;
        this.healthStatus = healthStatus;
        this.playStatus = playStatus;
        this.groomStatus = groomStatus;
        this.trainingStatus = trainingStatus;

        // Calculate stats based on statuses
        this.happiness = calculateHappiness();
        this.appearance = calculateAppearance();
        this.obedience = calculateObedience();
        this.adoptability = calculateAdoptability();
    }

    // Calculate Happiness based on fedStatus, healthStatus, and playStatus
    private int calculateHappiness() {
        int play = convertStatusToInt(playStatus);
        int fed = fedStatus ? 5 : 1; // Assume fed = true adds more happiness
        int health = convertStatusToInt(healthStatus);

        return (play + fed + health) / 3; // Average value
    }


    // Calculate Appearance based on fedStatus, healthStatus, and groomStatus
    private int calculateAppearance() {
        int fed = fedStatus ? 5 : 1; // Assume fed = true adds to appearance
        int health = convertStatusToInt(healthStatus);
        int groom = convertStatusToInt(groomStatus);

        return (fed + health + groom) / 3; // Average value
    }

    // Calculate Obedience based on trainingStatus
    private int calculateObedience() {
        return convertStatusToInt(trainingStatus);
    }

    // Calculate Adoptability based on happiness, appearance, and obedience
    private int calculateAdoptability() {
        return (happiness + appearance + obedience) / 3; // Average value
    }

    // Convert status string to integer value
    private int convertStatusToInt(String status) {
        switch (status) {
            case "Terrible": return 1;
            case "Poor": return 2;
            case "Average": return 3;
            case "Good": return 4;
            case "Great": return 5;
            default: return 0;
        }
    }

    // Update toString() to save all attributes properly
    @Override
    public String toString() {
        return name + "," + type + "," + imageFile + "," + happiness + "," + appearance + "," + obedience + "," +
                adoptability + "," + age + "," + fedStatus + "," + vaccinatedStatus + "," + healthStatus + "," +
                playStatus + "," + groomStatus + "," + trainingStatus;
    }

    // Add getters (if needed for accessing values elsewhere)
    public String getName() { return name; }
    public String getType() { return type; }
    public String getImageFile() { return imageFile; }
    public int getHappiness() { return happiness; }
    public int getAppearance() { return appearance; }
    public int getObedience() { return obedience; }
    public int getAdoptability() { return adoptability; }
    public String getAge() { return age; }
    public boolean isFedStatus() { return fedStatus; }
    public boolean isVaccinatedStatus() { return vaccinatedStatus; }
    public String getHealthStatus() { return healthStatus; }
    public String getPlayStatus() { return playStatus; }
    public String getGroomStatus() { return groomStatus; }
    public String getTrainingStatus() { return trainingStatus; }



    public void setName(String name) { this.name = name; }
}
