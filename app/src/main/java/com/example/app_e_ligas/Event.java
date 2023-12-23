package com.example.app_e_ligas;

public class Event {
    private String eventName;
    private int year;
    private int month;
    private int dayOfMonth;
    private String eventDescription; // Add event description field

    public Event(String eventName, int year, int month, int dayOfMonth) {
        this.eventName = eventName;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        // Initialize event description with an empty string
        this.eventDescription = "";
    }

    public String getEventName() {
        return eventName;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
