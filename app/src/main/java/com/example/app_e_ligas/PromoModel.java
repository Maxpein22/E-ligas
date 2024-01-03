package com.example.app_e_ligas;

public class PromoModel {

    String ID;
    String datePosted;
    String description;
    String eventBanner;
    String eventTitle;

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    public String getEventBanner() {
        return eventBanner;
    }

    public void setEventBanner(String eventBanner) {
        this.eventBanner = eventBanner;
    }

    public PromoModel(String datePosted, String description, String eventTitle, String eventBanner) {
        this.datePosted = datePosted;
        this.description = description;
        this.eventTitle = eventTitle;
        this.eventBanner = eventBanner;
    }

    public PromoModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    


}
