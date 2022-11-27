package uk.ac.tees.w9543466.pathlight.worker.dto;

import javax.validation.constraints.NotNull;

public class WorkApplicationRequest {

    @NotNull(message = "workId is required")
    private long workId;
    private double proposedRate;

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public double getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(double proposedRate) {
        this.proposedRate = proposedRate;
    }
}
