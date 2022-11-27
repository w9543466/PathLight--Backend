package uk.ac.tees.w9543466.pathlight.employer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;

import java.util.Optional;

public interface EmployerRepo extends JpaRepository<Employer, Long> {
    Optional<Employer> findByEmail(String email);
}
