package uk.ac.tees.w9543466.pathlight.worker.dto;

import java.util.List;

public class WorkerApplicationResponse {
    private List<WorkerApplicationItem> applications;

    public WorkerApplicationResponse(List<WorkerApplicationItem> applications) {
        this.applications = applications;
    }

    public List<WorkerApplicationItem> getApplications() {
        return applications;
    }

    public void setApplications(List<WorkerApplicationItem> applications) {
        this.applications = applications;
    }
}
