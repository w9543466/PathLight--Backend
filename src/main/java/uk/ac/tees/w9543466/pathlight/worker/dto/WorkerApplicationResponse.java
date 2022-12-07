package uk.ac.tees.w9543466.pathlight.worker.dto;

import java.util.List;

public class WorkerApplicationResponse {
    public WorkerApplicationResponse(List<WorkerApplicationItem> works) {
        this.works = works;
    }

    private List<WorkerApplicationItem> works;

    public List<WorkerApplicationItem> getWorks() {
        return works;
    }

    public void setWorks(List<WorkerApplicationItem> works) {
        this.works = works;
    }
}
