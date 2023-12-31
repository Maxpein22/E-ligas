package com.example.app_e_ligas;

public class Request {
    User user;
    String purpose;
    String type;
    String status;
    String createdAt;
    String description;
    String requestID;

    public Request(){

    }

    public Request(User user, String purpose, String type, String status, String createdAt, String description) {
        this.user = user;
        this.purpose = purpose;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.description = description;
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
