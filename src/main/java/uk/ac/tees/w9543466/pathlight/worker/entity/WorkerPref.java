package uk.ac.tees.w9543466.pathlight.worker.entity;

import javax.persistence.*;

@Entity(name = "worker_pref")
public class WorkerPref {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    @Column(name = "min_wage")
    private double minWage;
    @Column(name = "work_days")
    private String workDays;
    @Column(name = "work_timings")
    private String workTimings;
    @Column(name = "location_lat")
    private double locationLat;
    @Column(name = "location_lng")
    private double locationLng;
    @Column(name = "radius")
    private double radius;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
