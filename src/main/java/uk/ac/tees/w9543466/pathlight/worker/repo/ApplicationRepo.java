package uk.ac.tees.w9543466.pathlight.worker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.worker.entity.Application;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application, Long> {
    boolean existsByWorkerId(Long workerId);

    List<Application> findAllByWorkerId(long workerId);
}
