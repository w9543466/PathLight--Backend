package uk.ac.tees.w9543466.pathlight.worker;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.worker.dto.ProfileResponse;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;

import javax.persistence.EntityNotFoundException;

@RequestMapping("/worker")
@RestController
public class WorkerController {

    @Autowired
    private WorkerRepo workerRepo;

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getPrincipal().toString();
        Worker worker = workerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No worker found with provided email"));
        ModelMapper mapper = new ModelMapper();
        var response = mapper.map(worker, ProfileResponse.class);
        return BaseResponse.ok(response);
    }
}
