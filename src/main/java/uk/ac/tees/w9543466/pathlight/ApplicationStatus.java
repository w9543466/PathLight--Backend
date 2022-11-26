package uk.ac.tees.w9543466.pathlight;

public enum ApplicationStatus {
    APPLIED("applied"), REJECTED("rejected"), ACCEPTED("accepted");
    private final String status;

    ApplicationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
