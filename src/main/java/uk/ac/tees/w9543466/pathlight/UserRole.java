package uk.ac.tees.w9543466.pathlight;

public enum UserRole {

    WORKER("WORKER"), EMPLOYER("EMPLOYER"), ADMIN("ADMIN");
    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
