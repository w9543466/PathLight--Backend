package uk.ac.tees.w9543466.pathlight.auth.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is mandatory")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
