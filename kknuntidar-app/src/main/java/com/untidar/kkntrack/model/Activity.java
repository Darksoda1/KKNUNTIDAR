package com.untidar.kkntrack.model;

public class Activity {
    private int id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String location;
    private String userEmail;
    private String photoPath;

    public Activity() {}

    public Activity(String title, String description, String date, String time, String location, String userEmail) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.userEmail = userEmail;
    }

    public Activity(String title, String description, String date, String time, String location, String userEmail, String photoPath) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.userEmail = userEmail;
        this.photoPath = photoPath;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
