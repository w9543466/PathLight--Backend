package uk.ac.tees.w9543466.pathlight.employer.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity(name = "work")
public class Work {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String skills;
    private double lat;
    private double lng;
    @Column(name = "created_date")
    @CreationTimestamp
    private Instant createdDate;
    @Column(name = "expiry_date")
    private double expiryDate;
    @Column(name = "start_time")
    private double startTime;
    @Column(name = "work_duration")
    private double workDuration;
    @Column(name = "work_duration_unit")
    private double workDurationUnit;
    @Column(name = "rate_per_hour")
    private double ratePerHour;
    private String status;
    private String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
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

    public double getWorkDurationUnit() {
        return workDurationUnit;
    }

    public void setWorkDurationUnit(double workDurationUnit) {
        this.workDurationUnit = workDurationUnit;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
