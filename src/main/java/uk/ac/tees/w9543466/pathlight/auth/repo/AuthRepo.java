package uk.ac.tees.w9543466.pathlight.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.auth.entities.User;

import java.util.Optional;

public interface AuthRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}