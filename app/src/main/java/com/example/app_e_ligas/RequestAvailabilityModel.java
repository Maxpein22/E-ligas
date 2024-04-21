package com.example.app_e_ligas;

import java.util.HashMap;
import java.util.List;

public class RequestAvailabilityModel {
    HashMap<String, ServiceAvailabilityModel> serviceAvailabilityModelHashMap;

    public RequestAvailabilityModel(){
        // for firebase
    }

    public RequestAvailabilityModel(HashMap<String, ServiceAvailabilityModel> serviceAvailabilityModelHashMap) {
        this.serviceAvailabilityModelHashMap = serviceAvailabilityModelHashMap;
    }

    public HashMap<String, ServiceAvailabilityModel> getServiceAvailabilityModelHashMap() {
        return serviceAvailabilityModelHashMap;
    }

    public void setServiceAvailabilityModelHashMap(HashMap<String, ServiceAvailabilityModel> serviceAvailabilityModelHashMap) {
        this.serviceAvailabilityModelHashMap = serviceAvailabilityModelHashMap;
    }
}
