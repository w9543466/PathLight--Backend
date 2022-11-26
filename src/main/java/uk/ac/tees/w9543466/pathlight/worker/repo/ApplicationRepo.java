package uk.ac.tees.w9543466.pathlight.worker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.worker.entity.Application;

public interface ApplicationRepo extends JpaRepository<Application, Long> {
    boolean existsByWorkerId(Long workerId);
}
