package com.example.app_e_ligas;

public class ReportModel {
    String emergencyType;
    String userId;
    String proofUrl;
    String neededHelp;
    String status;

    String reportedDate;
    String reportLocation;
    String reportingType;
    String fcmToken;

    String rejectReason;


    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getReportingType() {
        return reportingType;
    }

    public void setReportingType(String reportingType) {
        this.reportingType = reportingType;
    }

    public String getReportLocation() {
        return reportLocation;
    }


    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }

    public ReportModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public ReportModel(String emergencyType, String userId, String proofUrl, String neededHelp, String status,String reportedDate, String reportLocation, String reportingType, String fcmToken) {
        this.emergencyType = emergencyType;
        this.userId = userId;
        this.proofUrl = proofUrl;
        this.neededHelp = neededHelp;
        this.status = status;
        this.reportedDate = reportedDate;
        this.reportLocation = reportLocation;
        this.reportingType = reportingType;
        this.fcmToken = fcmToken;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProofUrl() {
        return proofUrl;
    }

    public void setProofUrl(String proofUrl) {
        this.proofUrl = proofUrl;
    }

    public String getNeededHelp() {
        return neededHelp;
    }

    public void setNeededHelp(String neededHelp) {
        this.neededHelp = neededHelp;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
