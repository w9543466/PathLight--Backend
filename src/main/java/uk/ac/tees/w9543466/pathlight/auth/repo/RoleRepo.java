package uk.ac.tees.w9543466.pathlight.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.tees.w9543466.pathlight.auth.entities.Roles;

import java.util.List;

public interface RoleRepo extends JpaRepository<Roles, Long> {

    List<Roles> findByUserId(String userId);
}
