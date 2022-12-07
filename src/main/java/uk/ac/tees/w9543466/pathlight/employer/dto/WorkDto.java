package uk.ac.tees.w9543466.pathlight.employer.dto;

import java.util.List;

public class WorkDto {

    private List<WorkItem> works;

    public WorkDto(List<WorkItem> works) {
        this.works = works;
    }

    public List<WorkItem> getWorks() {
        return works;
    }

    public void setWorks(List<WorkItem> works) {
        this.works = works;
    }
}
