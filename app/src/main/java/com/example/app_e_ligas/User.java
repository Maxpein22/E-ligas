package com.example.app_e_ligas;

public class User {
    public String userLastName;
    public String userMiddleName;
    public String userFirstName;
    public String userPhoneNumber;
    public String userEmail;
    public String userPassword;
    public String userConfirmPassword;


    // Corrected constructor to include the 'role' parameter
    public User(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword) {
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;

    }
}
