package uk.ac.tees.w9543466.pathlight.employer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;

import java.util.Optional;

public interface EmployerRepo extends JpaRepository<Employer, Long> {
    Optional<Worker> findByEmail(String email);
}
