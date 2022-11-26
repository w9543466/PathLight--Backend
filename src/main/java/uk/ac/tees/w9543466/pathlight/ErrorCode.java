package uk.ac.tees.w9543466.pathlight;

public enum ErrorCode {
    WORK_MISMATCH("ERR_605"),
    DATA_NOT_FOUND("ERR_604"),
    ALREADY_EXISTS("ERR_603"),
    VALIDATION_FAILED("ERR_602"),
    NOT_VERIFIED("ERR_601"),
    SETUP_INCOMPLETE("ERR_600");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
