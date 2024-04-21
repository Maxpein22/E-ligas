package com.example.app_e_ligas;

public class Request {
    User user;
    String purpose;
    String type;
    String status;
    String createdAt;
    String description;
    String requestID;

    String kindOfBusiness;
    String addressOfBusiness;
    String controlNo;
    String photo1x1URL;
    String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPhoto1x1URL() {
        return photo1x1URL;
    }

    public void setPhoto1x1URL(String photo1x1URL) {
        this.photo1x1URL = photo1x1URL;
    }

    public String getControlNo() {
        return controlNo;
    }

    public void setControlNo(String controlNo) {
        this.controlNo = controlNo;
    }

    public String getKindOfBusiness() {
        return kindOfBusiness;
    }

    public void setKindOfBusiness(String kindOfBusiness) {
        this.kindOfBusiness = kindOfBusiness;
    }

    public String getAddressOfBusiness() {
        return addressOfBusiness;
    }

    public void setAddressOfBusiness(String addressOfBusiness) {
        this.addressOfBusiness = addressOfBusiness;
    }

    public Request(){

    }


    public Request(User user, String purpose, String type, String status, String createdAt, String description, String kindOfBusiness, String addressOfBusiness, String controlNo, String photo1x1URL, String fcmToken) {
        this.user = user;
        this.purpose = purpose;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.description = description;
        this.kindOfBusiness = kindOfBusiness;
        this.addressOfBusiness = addressOfBusiness;
        this.controlNo = controlNo;
        this.photo1x1URL = photo1x1URL;
        this.fcmToken = fcmToken;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
