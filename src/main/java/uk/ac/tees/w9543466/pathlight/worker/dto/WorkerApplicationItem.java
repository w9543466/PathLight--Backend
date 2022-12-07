package uk.ac.tees.w9543466.pathlight.worker.dto;

import uk.ac.tees.w9543466.pathlight.ApplicationStatus;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.entity.Work;

import java.time.Instant;

public class WorkerApplicationItem {
    private Long id;
    private Long workerId;
    private Long workId;
    private Double rate;
    private ApplicationStatus applicationStatus;
    private Instant createdDate;
    private Employer employer;
    private Work work;

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

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

    public long getCreatedDate() {
        return createdDate.toEpochMilli();
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
}
