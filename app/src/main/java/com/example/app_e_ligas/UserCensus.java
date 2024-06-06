package com.example.app_e_ligas;

import java.util.HashMap;
import java.util.Map;

public class UserCensus {
    public String userLastName;
    public String userMiddleName;
    public String userFirstName;
    public String userPhoneNumber;
    public String userEmail;
    public String userPassword;
    public String civilStatus;
    public String birthday;
    public String birthPlace;
    public String emergencyContactPerson;
    public String emergencyContactPersonNo;
    public String validIDUrl;
    public String userProfileImage;
    public String fcmToken;
    public String email;

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean validated; // Moved from static to instance field
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userLastName", userLastName);
        map.put("userMiddleName", userMiddleName);
        map.put("userFirstName", userFirstName);
        map.put("userPhoneNumber", userPhoneNumber);
        map.put("userEmail", userEmail);
        map.put("userPassword", userPassword);
        map.put("civilStatus", civilStatus);
        map.put("birthday", birthday);
        map.put("birthPlace", birthPlace);
        map.put("emergencyContactPerson", emergencyContactPerson);
        map.put("emergencyContactPersonNo", emergencyContactPersonNo);
        map.put("validIDUrl", validIDUrl);
        map.put("userProfileImage", userProfileImage);
        map.put("fcmToken", fcmToken);
        return map;
    }
    public UserCensus() {
    }

    public UserCensus(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber,
                String userEmail, String userPassword, String emergencyContactPerson, String emergencyContactPersonNo,
                String validIDUrl, boolean validated, String fcmToken) {
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.emergencyContactPerson = emergencyContactPerson;
        this.emergencyContactPersonNo = emergencyContactPersonNo;
        this.validIDUrl = validIDUrl;
        this.validated = validated;
        this.fcmToken = fcmToken;
    }

    public void clearUserData() {
        userLastName = null;
        userMiddleName = null;
        userFirstName = null;
        userPhoneNumber = null;
        userEmail = null;
        userPassword = null;
        civilStatus = null;
        birthday = null;
        birthPlace = null;
        emergencyContactPerson = null;
        emergencyContactPersonNo = null;
        validIDUrl = null;
        userProfileImage = null;
        validated = false;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getFullName() {
        return userFirstName + " " + userMiddleName + " " + userLastName;
    }

    public UserCensus(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword) {
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
