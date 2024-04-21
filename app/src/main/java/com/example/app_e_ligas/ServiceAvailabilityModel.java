package com.example.app_e_ligas;

public class ServiceAvailabilityModel {
    String lastRequestedDate;
    String nextAvailableRequestDate;
    String serviceType;

    public ServiceAvailabilityModel(){
        // for firebase
    }

    public ServiceAvailabilityModel(String lastRequestedDate, String nextAvailableRequestDate, String serviceType) {
        this.lastRequestedDate = lastRequestedDate;
        this.nextAvailableRequestDate = nextAvailableRequestDate;
        this.serviceType = serviceType;
    }

    public String getLastRequestedDate() {
        return lastRequestedDate;
    }

    public void setLastRequestedDate(String lastRequestedDate) {
        this.lastRequestedDate = lastRequestedDate;
    }

    public String getNextAvailableRequestDate() {
        return nextAvailableRequestDate;
    }

    public void setNextAvailableRequestDate(String nextAvailableRequestDate) {
        this.nextAvailableRequestDate = nextAvailableRequestDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
