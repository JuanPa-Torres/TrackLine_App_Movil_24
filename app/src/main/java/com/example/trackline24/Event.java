package com.example.trackline24;
public class Event {
    private String award;
    private String dateStartRegistry;
    private String eventDate;
    private String facebookLink;
    private String idActivity;
    private String idEvent;
    private String imagePath;
    private String imageUrl;
    private String latitudeFinish;
    private String latitudeStart;
    private String longitudeFinish;
    private String longitudeStart;
    private String name;
    private String nameOrganizer;
    private String objective;
    private String type;
    private String whatsappLink;

    public Event(String award, String dateStartRegistry, String eventDate, String facebookLink,
                 String idActivity, String idEvent, String imageUrl,
                 String latitudeFinish, String latitudeStart, String longitudeFinish,
                 String longitudeStart, String name, String nameOrganizer, String objective,
                 String type, String whatsappLink) {
        this.award = award;
        this.dateStartRegistry = dateStartRegistry;
        this.eventDate = eventDate;
        this.facebookLink = facebookLink;
        this.idActivity = idActivity;
        this.idEvent = idEvent;
        this.imageUrl = imageUrl;
        this.latitudeFinish = latitudeFinish;
        this.latitudeStart = latitudeStart;
        this.longitudeFinish = longitudeFinish;
        this.longitudeStart = longitudeStart;
        this.name = name;
        this.nameOrganizer = nameOrganizer;
        this.objective = objective;
        this.type = type;
        this.whatsappLink = whatsappLink;
    }

    // Getters y Setters
    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getDateStartRegistry() {
        return dateStartRegistry;
    }

    public void setDateStartRegistry(String dateStartRegistry) {
        this.dateStartRegistry = dateStartRegistry;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        this.idActivity = idActivity;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLatitudeFinish() {
        return latitudeFinish;
    }

    public void setLatitudeFinish(String latitudeFinish) {
        this.latitudeFinish = latitudeFinish;
    }

    public String getLatitudeStart() {
        return latitudeStart;
    }

    public void setLatitudeStart(String latitudeStart) {
        this.latitudeStart = latitudeStart;
    }

    public String getLongitudeFinish() {
        return longitudeFinish;
    }

    public void setLongitudeFinish(String longitudeFinish) {
        this.longitudeFinish = longitudeFinish;
    }

    public String getLongitudeStart() {
        return longitudeStart;
    }

    public void setLongitudeStart(String longitudeStart) {
        this.longitudeStart = longitudeStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOrganizer() {
        return nameOrganizer;
    }

    public void setNameOrganizer(String nameOrganizer) {
        this.nameOrganizer = nameOrganizer;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWhatsappLink() {
        return whatsappLink;
    }

    public void setWhatsappLink(String whatsappLink) {
        this.whatsappLink = whatsappLink;
    }
}