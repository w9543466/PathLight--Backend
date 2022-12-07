package uk.ac.tees.w9543466.pathlight.employer.dto;

import java.util.List;

public class ApplicationResponse {
    private List<ApplicationItemDto> applications;

    public ApplicationResponse(List<ApplicationItemDto> applications) {
        this.applications = applications;
    }

    public List<ApplicationItemDto> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationItemDto> applications) {
        this.applications = applications;
    }
}
