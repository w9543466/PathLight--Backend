package uk.ac.tees.w9543466.pathlight.employer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class WorkDto {
    private long workId;
    @NotBlank(message = "Title is required")
    private String title;
    private String skills;
    @NotNull(message = "Missing latitude. Location is required.")
    private double lat;
    @NotNull(message = "Missing longitude. Location is required.")
    private double lng;
    @NotNull(message = "Work expiry date is required")
    private double expiryDate;
    @NotNull(message = "Work start time is required")
    private double startTime;
    private double workDuration;
    private double workDurationUnit;
    private double totalRate;
    private String status;

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public double getWorkDurationUnit() {
        return workDurationUnit;
    }

    public void setWorkDurationUnit(double workDurationUnit) {
        this.workDurationUnit = workDurationUnit;
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

    public double getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(double expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(double workDuration) {
        this.workDuration = workDuration;
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
