package uk.ac.tees.w9543466.pathlight.employer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WorkItem {
    private long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String skills;
    @NotNull(message = "Missing latitude. Location is required.")
    private double lat;
    @NotNull(message = "Missing longitude. Location is required.")
    private double lng;
    @NotNull(message = "Work start time is required")
    private long startTime;
    private double totalRate;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(double totalRate) {
        this.totalRate = totalRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
