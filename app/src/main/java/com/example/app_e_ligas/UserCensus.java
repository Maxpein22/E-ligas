package com.example.app_e_ligas;

import java.util.HashMap;
import java.util.Map;

public class UserCensus {
    public String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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
    public String address;
    public String age;
    public String alias;
    public String barangay;
    public String cityMunicipality;
    public String collegeSchoolAddress;
    public String collegeSchoolName;
    public String companyAddress;
    public String companyName;
    public boolean dataSubmitted;
    public String durationOfEmployment;
    public String elemSchoolAddress;
    public String elemSchoolName;
    public String fourPs;
    public String gender;
    public String height;
    public String highSchoolAddress;
    public String highSchoolName;
    public String houseBlockLot;
    public String houseType;
    public String learning_system;
    public String nationality;
    public String occupation;
    public String province;
    public String pwdType;
    public String religion;
    public String resident_status;
    public String soloParent;
    public String stPurokSitioSubd;
    public String type_employment;
    public String vaccineStatus;
    public String vocSchoolAddress;
    public String vocSchoolName;
    public String voters;
    public String weight;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public UserCensus(String userLastName, String userMiddleName, String userFirstName, String userPhoneNumber, String userEmail, String userPassword, String civilStatus, String birthday, String birthPlace, String emergencyContactPerson, String emergencyContactPersonNo, String validIDUrl, String userProfileImage, String fcmToken, String email, String address, String age, String alias, String barangay, String cityMunicipality, String collegeSchoolAddress, String collegeSchoolName, String companyAddress, String companyName, boolean dataSubmitted, String durationOfEmployment, String elemSchoolAddress, String elemSchoolName, String fourPs, String gender, String height, String highSchoolAddress, String highSchoolName, String houseBlockLot, String houseType, String learning_system, String nationality, String occupation, String province, String pwdType, String religion, String resident_status, String soloParent, String stPurokSitioSubd, String type_employment, String vaccineStatus, String vocSchoolAddress, String vocSchoolName, String voters, String weight, boolean validated) {
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userFirstName = userFirstName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.civilStatus = civilStatus;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.emergencyContactPerson = emergencyContactPerson;
        this.emergencyContactPersonNo = emergencyContactPersonNo;
        this.validIDUrl = validIDUrl;
        this.userProfileImage = userProfileImage;
        this.fcmToken = fcmToken;
        this.email = email;
        this.address = address;
        this.age = age;
        this.alias = alias;
        this.barangay = barangay;
        this.cityMunicipality = cityMunicipality;
        this.collegeSchoolAddress = collegeSchoolAddress;
        this.collegeSchoolName = collegeSchoolName;
        this.companyAddress = companyAddress;
        this.companyName = companyName;
        this.dataSubmitted = dataSubmitted;
        this.durationOfEmployment = durationOfEmployment;
        this.elemSchoolAddress = elemSchoolAddress;
        this.elemSchoolName = elemSchoolName;
        this.fourPs = fourPs;
        this.gender = gender;
        this.height = height;
        this.highSchoolAddress = highSchoolAddress;
        this.highSchoolName = highSchoolName;
        this.houseBlockLot = houseBlockLot;
        this.houseType = houseType;
        this.learning_system = learning_system;
        this.nationality = nationality;
        this.occupation = occupation;
        this.province = province;
        this.pwdType = pwdType;
        this.religion = religion;
        this.resident_status = resident_status;
        this.soloParent = soloParent;
        this.stPurokSitioSubd = stPurokSitioSubd;
        this.type_employment = type_employment;
        this.vaccineStatus = vaccineStatus;
        this.vocSchoolAddress = vocSchoolAddress;
        this.vocSchoolName = vocSchoolName;
        this.voters = voters;
        this.weight = weight;
        this.validated = validated;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getCityMunicipality() {
        return cityMunicipality;
    }

    public void setCityMunicipality(String cityMunicipality) {
        this.cityMunicipality = cityMunicipality;
    }

    public String getCollegeSchoolAddress() {
        return collegeSchoolAddress;
    }

    public void setCollegeSchoolAddress(String collegeSchoolAddress) {
        this.collegeSchoolAddress = collegeSchoolAddress;
    }

    public String getCollegeSchoolName() {
        return collegeSchoolName;
    }

    public void setCollegeSchoolName(String collegeSchoolName) {
        this.collegeSchoolName = collegeSchoolName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isDataSubmitted() {
        return dataSubmitted;
    }

    public void setDataSubmitted(boolean dataSubmitted) {
        this.dataSubmitted = dataSubmitted;
    }

    public String getDurationOfEmployment() {
        return durationOfEmployment;
    }

    public void setDurationOfEmployment(String durationOfEmployment) {
        this.durationOfEmployment = durationOfEmployment;
    }

    public String getElemSchoolAddress() {
        return elemSchoolAddress;
    }

    public void setElemSchoolAddress(String elemSchoolAddress) {
        this.elemSchoolAddress = elemSchoolAddress;
    }

    public String getElemSchoolName() {
        return elemSchoolName;
    }

    public void setElemSchoolName(String elemSchoolName) {
        this.elemSchoolName = elemSchoolName;
    }

    public String getFourPs() {
        return fourPs;
    }

    public void setFourPs(String fourPs) {
        this.fourPs = fourPs;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHighSchoolAddress() {
        return highSchoolAddress;
    }

    public void setHighSchoolAddress(String highSchoolAddress) {
        this.highSchoolAddress = highSchoolAddress;
    }

    public String getHighSchoolName() {
        return highSchoolName;
    }

    public void setHighSchoolName(String highSchoolName) {
        this.highSchoolName = highSchoolName;
    }

    public String getHouseBlockLot() {
        return houseBlockLot;
    }

    public void setHouseBlockLot(String houseBlockLot) {
        this.houseBlockLot = houseBlockLot;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getLearning_system() {
        return learning_system;
    }

    public void setLearning_system(String learning_system) {
        this.learning_system = learning_system;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPwdType() {
        return pwdType;
    }

    public void setPwdType(String pwdType) {
        this.pwdType = pwdType;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getResident_status() {
        return resident_status;
    }

    public void setResident_status(String resident_status) {
        this.resident_status = resident_status;
    }

    public String getSoloParent() {
        return soloParent;
    }

    public void setSoloParent(String soloParent) {
        this.soloParent = soloParent;
    }

    public String getStPurokSitioSubd() {
        return stPurokSitioSubd;
    }

    public void setStPurokSitioSubd(String stPurokSitioSubd) {
        this.stPurokSitioSubd = stPurokSitioSubd;
    }

    public String getType_employment() {
        return type_employment;
    }

    public void setType_employment(String type_employment) {
        this.type_employment = type_employment;
    }

    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }

    public String getVocSchoolAddress() {
        return vocSchoolAddress;
    }

    public void setVocSchoolAddress(String vocSchoolAddress) {
        this.vocSchoolAddress = vocSchoolAddress;
    }

    public String getVocSchoolName() {
        return vocSchoolName;
    }

    public void setVocSchoolName(String vocSchoolName) {
        this.vocSchoolName = vocSchoolName;
    }

    public String getVoters() {
        return voters;
    }

    public void setVoters(String voters) {
        this.voters = voters;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

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
        map.put("email", email);
        map.put("address", address);
        map.put("age", age);
        map.put("alias", alias);
        map.put("barangay", barangay);
        map.put("cityMunicipality", cityMunicipality);
        map.put("collegeSchoolAddress", collegeSchoolAddress);
        map.put("collegeSchoolName", collegeSchoolName);
        map.put("companyAddress", companyAddress);
        map.put("companyName", companyName);
        map.put("dataSubmitted", dataSubmitted);
        map.put("durationOfEmployment", durationOfEmployment);
        map.put("elemSchoolAddress", elemSchoolAddress);
        map.put("elemSchoolName", elemSchoolName);
        map.put("fourPs", fourPs);
        map.put("gender", gender);
        map.put("height", height);
        map.put("highSchoolAddress", highSchoolAddress);
        map.put("highSchoolName", highSchoolName);
        map.put("houseBlockLot", houseBlockLot);
        map.put("houseType", houseType);
        map.put("learning_system", learning_system);
        map.put("nationality", nationality);
        map.put("occupation", occupation);
        map.put("province", province);
        map.put("pwdType", pwdType);
        map.put("religion", religion);
        map.put("resident_status", resident_status);
        map.put("soloParent", soloParent);
        map.put("stPurokSitioSubd", stPurokSitioSubd);
        map.put("type_employment", type_employment);
        map.put("vaccineStatus", vaccineStatus);
        map.put("vocSchoolAddress", vocSchoolAddress);
        map.put("vocSchoolName", vocSchoolName);
        map.put("voters", voters);
        map.put("weight", weight);
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
