package uk.ac.tees.w9543466.pathlight.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PreferenceDto {
    private double minWage;
    private String workDays;
    private String workTimings;
    private double locationLat;
    private double locationLng;
    private double radius;
    @JsonIgnore
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getMinWage() {
        return minWage;
    }

    public void setMinWage(double minWage) {
        this.minWage = minWage;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }

    public String getWorkTimings() {
        return workTimings;
    }

    public void setWorkTimings(String workTimings) {
        this.workTimings = workTimings;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
