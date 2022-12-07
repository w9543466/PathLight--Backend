package uk.ac.tees.w9543466.pathlight.worker.dto;

import java.util.List;

public class WorkResponse {
    private List<WorkItemResponse> works;

    public WorkResponse(List<WorkItemResponse> works) {
        this.works = works;
    }

    public List<WorkItemResponse> getWorks() {
        return works;
    }

    public void setWorks(List<WorkItemResponse> works) {
        this.works = works;
    }
}
