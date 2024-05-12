package com.example.app_e_ligas;

public class BarangayOfficial implements Comparable<BarangayOfficial> {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String position;
    private String profileImage;
    private String address;
    private String civilStatus;
    private String email;
    private String gender;
    private String phoneNumber;
    private String startYear; // New field for start year
    private String endYear; // New field for end year

    public BarangayOfficial() {
        // Default constructor required for Firebase
    }

    public BarangayOfficial(String id, String firstName, String lastName, String middleName, String position, String profileImage, String address, String civilStatus, String email, String gender, String phoneNumber, String startYear, String endYear) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.position = position;
        this.profileImage = profileImage;
        this.address = address;
        this.civilStatus = civilStatus;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    @Override
    public int compareTo(BarangayOfficial otherOfficial) {
        // Define the order of positions
        String[] positionsOrder = {
                "Barangay Captain",
                "Kagawad - Committee on Human Rights",
                "Kagawad - Committee on Disaster Risk",
                "Kagawad - Appropriation",
                "Kagawad- Committee on Protection of Children",
                "Kagawad- Committee on Nutrition",
                "Kagawad- Committee on Peace And Order",
                "Kagawad- Committee on Ecological Solid Waste",
                "SK Chairman",
                "Barangay Secretary",
                "Barangay Treasurer",
                "IT Staff"
        };

        // Get the index of the position for this official
        int thisIndex = getPositionIndex(this.position, positionsOrder);

        // Get the index of the position for the other official
        int otherIndex = getPositionIndex(otherOfficial.getPosition(), positionsOrder);

        // Compare the positions based on their indices
        return thisIndex - otherIndex;
    }

    private int getPositionIndex(String position, String[] positionsOrder) {
        for (int i = 0; i < positionsOrder.length; i++) {
            if (position.equals(positionsOrder[i])) {
                return i;
            }
        }
        return -1; // Position not found
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
}
