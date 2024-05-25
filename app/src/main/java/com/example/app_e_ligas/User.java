package com.example.app_e_ligas;

public class User {
    public static String userLastName;
    public static String userMiddleName;
    public static String userFirstName;
    public static String userPhoneNumber;
    public static String userEmail;
    public static String userPassword;
    public static String civilStatus;
    public static String age;
    public static String birthday;
    public static String birthPlace;
    public static String emergencyContactPerson;
    public static String emergencyContactPersonNo;
    public static String address;
    public static String validIDUrl; // Added field for valid ID URL
    public static String userProfileImage; // Added field for user profile image

    String fcmToken;


    public User() {

    }

    public static boolean validated; // Add a new field for validation status


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword, String civilStatus, String age, String birthday, String emergencyContactPerson, String emergencyContactPersonNo, String birthPlace, String address, String validIDUrl, boolean validated, String fcmToken) {
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.civilStatus = civilStatus;
        this.age = age;
        this.birthday = birthday;
        this.emergencyContactPerson = emergencyContactPerson;
        this.emergencyContactPersonNo = emergencyContactPersonNo;
        this.birthPlace = birthPlace;
        this.address = address;
        this.validIDUrl = validIDUrl;
        this.userProfileImage = userProfileImage;
        this.validated = validated; // Initialize the validated field
        this.fcmToken = fcmToken;
    }

    public static void clearUserData() {
        // Set all fields to default values or null
        userLastName = null;
        userMiddleName = null;
        userFirstName = null;
        userPhoneNumber = null;
        userEmail = null;
        userPassword = null;
        civilStatus = null;
        age = null;
        birthday = null;
        birthPlace = null;
        emergencyContactPerson = null;
        emergencyContactPersonNo = null;
        address = null;
        validIDUrl = null;
        userProfileImage = null;
        validated = false;
    }


    public String getFcmToken(){
        return fcmToken;
    }

    public void setFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    // Add getter and setter methods for the validated field
    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
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
    public String getEmergencyContactPersonNo() {
        return emergencyContactPersonNo;
    }


    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }
    public void setEmergencyContactPersonNo(String emergencyContactPersonNo) {
        this.emergencyContactPersonNo = emergencyContactPersonNo;
    }

    public String getValidIDUrl() {
        return validIDUrl;
    }

    public void setValidIDUrl(String validIDUrl) {
        this.validIDUrl = validIDUrl;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }
}
