package uk.ac.tees.w9543466.pathlight.worker.entity;

import org.hibernate.annotations.CreationTimestamp;
import uk.ac.tees.w9543466.pathlight.ApplicationStatus;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "worker_id")
    private Long workerId;
    @Column(name = "work_id")
    private Long workId;
    @Column(name = "proposed_rate")
    private Double rate;
    @Column(name = "status")
    private ApplicationStatus applicationStatus;
    @Column(name = "created_date")
    @CreationTimestamp
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
