package uk.ac.tees.w9543466.pathlight.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VerifyRequest {
    @NotBlank(message = "Email id is required")
    private String email;
    @NotBlank(message = "Role is required")
    private String role;
    @NotNull(message = "Verify flag is required")
    private boolean verify;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
