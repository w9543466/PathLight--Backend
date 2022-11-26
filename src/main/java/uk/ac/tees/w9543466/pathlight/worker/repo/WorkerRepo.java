package uk.ac.tees.w9543466.pathlight.worker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;

import java.util.Optional;

public interface WorkerRepo extends JpaRepository<Worker, Long> {
    Optional<Worker> findByEmail(String email);
}
