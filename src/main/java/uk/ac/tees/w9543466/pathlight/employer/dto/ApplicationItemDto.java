package uk.ac.tees.w9543466.pathlight.employer.dto;

import uk.ac.tees.w9543466.pathlight.ApplicationStatus;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;

import java.time.Instant;

public class ApplicationItemDto {
    private Long id;
    private Long workerId;
    private Long workId;
    private Double rate;
    private ApplicationStatus applicationStatus;
    private Instant createdDate;
    private Worker worker;

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

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public long getCreatedDate() {
        return createdDate.toEpochMilli();
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
