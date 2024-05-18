package com.example.app_e_ligas;

public class PromoModel {

    String ID;
    String datePosted;
    String description;
    String eventBanner;
    String eventTitle;
    String organizerName;
    String startDate;
    String endDate;

    String eventLink;

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

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public PromoModel() {
        // Default constructor required for calls to DataSnapshot.getValue(PromoModel.class)
    }

    public PromoModel(String ID, String datePosted, String description, String eventBanner, String eventTitle, String organizerName, String startDate, String endDate, String eventLink) {
        this.ID = ID;
        this.datePosted = datePosted;
        this.description = description;
        this.eventBanner = eventBanner;
        this.eventTitle = eventTitle;
        this.organizerName = organizerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventLink = eventLink;
    }
}
