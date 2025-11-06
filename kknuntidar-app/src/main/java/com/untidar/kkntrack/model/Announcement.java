package com.untidar.kkntrack.model;

public class Announcement {
    private String title;
    private String content;
    private String date;

    public Announcement(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
