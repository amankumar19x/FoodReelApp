package com.example.foodreelappassignment.model;

public class FoodData {
    private String title;
    private String location;
    private String url;
    private boolean isBookmarked;  // New field for bookmarking

    public FoodData(String title, String location, String url) {
        this.title = title;
        this.location = location;
        this.url = url;
        this.isBookmarked = false;  // Default: not bookmarked
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
