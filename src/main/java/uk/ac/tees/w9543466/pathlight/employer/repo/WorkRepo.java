package uk.ac.tees.w9543466.pathlight.employer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.employer.entity.Work;

import java.util.List;
import java.util.Optional;

public interface WorkRepo extends JpaRepository<Work, Long> {
    Optional<List<Work>> findByCreatedBy(String createdBy);

    Optional<Work> findByCreatedByAndId(String createdBy, long id);
}
