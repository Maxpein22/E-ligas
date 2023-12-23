package com.example.app_e_ligas;

public class User {
    public String userLastName;
    public String userMiddleName;
    public String userFirstName;
    public String userPhoneNumber;
    public String userEmail;
    public String userPassword;
    public String userConfirmPassword;
    public String civilStatus;
    public String age;
    public String birthday;
    public String birthPlace;
    public String emergencyContactPerson;
    public String address;


    public User() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword, String userConfirmPassword, String civilStatus, String age, String birthday, String emergencyContactPerson, String birthPlace, String address) {
        this.birthPlace = birthPlace;
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userConfirmPassword = userConfirmPassword;
        this.civilStatus = civilStatus;
        this.age = age;
        this.birthday = birthday;
        this.emergencyContactPerson = emergencyContactPerson;
    }

    public String getFullName(){
        return userFirstName + " " + userMiddleName + " " + userLastName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    // Corrected constructor to include the 'role' parameter
    public User(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword) {
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMiddleName() {
        return userMiddleName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserConfirmPassword() {
        return userConfirmPassword;
    }

    public void setUserConfirmPassword(String userConfirmPassword) {
        this.userConfirmPassword = userConfirmPassword;

    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmergencyContactPerson() {
        return emergencyContactPerson;
    }

    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }
}
