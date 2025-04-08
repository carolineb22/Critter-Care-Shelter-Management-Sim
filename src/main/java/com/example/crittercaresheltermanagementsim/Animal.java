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
    private boolean hasFedToday;
    private boolean hasVisitedVetToday;
    private boolean hasPlayedToday;
    private boolean hasBeenGroomedToday;
    private boolean hasTrainedToday;
    private int lastFedDay;
    private int lastVetDay;
    private int lastPlayDay;
    private int lastGroomDay;
    private int lastTrainDay;



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

    public void setPlayStatus(String playStatus) { this.playStatus = playStatus; }

    public void setGroomStatus(String groomStatus) { this.groomStatus = groomStatus; }

    public void setTrainingStatus(String trainingStatus) { this.trainingStatus = trainingStatus; }


    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }


    public void setVaccinatedStatus(boolean vaccinatedStatus) { this.vaccinatedStatus = vaccinatedStatus; }


    public void setFedStatus(boolean fedStatus) { this.fedStatus = fedStatus;}

    // Fed flag
    public boolean hasFedToday() {
        return hasFedToday;
    }
    public void setHasFedToday(boolean hasFedToday) {
        this.hasFedToday = hasFedToday;
    }

    // Vet visit flag
    public boolean hasVisitedVetToday() {
        return hasVisitedVetToday;
    }
    public void setHasVisitedVetToday(boolean hasVisitedVetToday) {
        this.hasVisitedVetToday = hasVisitedVetToday;
    }

    // Play flag
    public boolean hasPlayedToday() {
        return hasPlayedToday;
    }
    public void setHasPlayedToday(boolean hasPlayedToday) {
        this.hasPlayedToday = hasPlayedToday;
    }

    // Groom flag
    public boolean hasBeenGroomedToday() {
        return hasBeenGroomedToday;
    }
    public void setHasBeenGroomedToday(boolean hasBeenGroomedToday) {
        this.hasBeenGroomedToday = hasBeenGroomedToday;
    }

    // Train flag
    public boolean hasTrainedToday() {
        return hasTrainedToday;
    }
    public void setHasTrainedToday(boolean hasTrainedToday) {
        this.hasTrainedToday = hasTrainedToday;
    }

    // Reset the flags at the start of a new day
    public void resetDailyFlags() {
        this.hasFedToday = false;
        this.hasVisitedVetToday = false;
        this.hasPlayedToday = false;
        this.hasBeenGroomedToday = false;
        this.hasTrainedToday = false;
    }

}
