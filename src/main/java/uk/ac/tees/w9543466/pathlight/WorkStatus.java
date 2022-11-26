package uk.ac.tees.w9543466.pathlight;

public enum WorkStatus {
    NOT_STARTED("not_started"),
    STARTED("started"),
    CANCELLED("cancelled"),
    NO_SHOW_W("no_show_w"),
    NO_SHOW_E("no_show_e"),
    NO_SHOW("no_show"),
    COMPLETED_W("completed_worker"),
    COMPLETED_E("completed_employer"),
    COMPLETED("completed");
    private final String status;

    WorkStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
