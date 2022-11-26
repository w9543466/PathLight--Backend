package uk.ac.tees.w9543466.pathlight.worker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.worker.entity.WorkerPref;

import java.util.Optional;

public interface WorkerPrefRepo extends JpaRepository<WorkerPref, Long> {
    Optional<WorkerPref> findByEmail(String email);
}
