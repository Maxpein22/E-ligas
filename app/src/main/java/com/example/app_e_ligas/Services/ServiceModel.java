package com.example.app_e_ligas.Services;

public class ServiceModel {
    String serviceName;
    String description;
    String iconUrl;
    String price;
    String id;
    boolean isArchived;



    public ServiceModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public ServiceModel(String serviceName, String description, String iconUrl, String price, boolean isArchived) {
        this.serviceName = serviceName;
        this.description = description;
        this.iconUrl = iconUrl;
        this.price = price;
        this.isArchived = isArchived;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean archived) {
        isArchived = archived;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
