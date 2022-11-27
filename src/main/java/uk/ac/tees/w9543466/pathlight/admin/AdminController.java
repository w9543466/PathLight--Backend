package uk.ac.tees.w9543466.pathlight.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.tees.w9543466.pathlight.BaseController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.UserRole;
import uk.ac.tees.w9543466.pathlight.admin.dto.VerifyRequest;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.repo.EmployerRepo;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController extends BaseController {

    @Autowired
    private WorkerRepo workerRepo;

    @Autowired
    private EmployerRepo employerRepo;

    @PutMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verify(@Valid @RequestBody VerifyRequest request) {
        var email = request.getEmail();
        var role = UserRole.valueOf(request.getRole().toUpperCase());
        var msg = "";
        switch (role) {
            case WORKER:
                Worker worker = workerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No worker found with provided email"));
                worker.setVerified(request.isVerify());
                workerRepo.save(worker);
                msg = "Employer with email " + email + " " + (worker.isVerified() ? "verified" : "unverified");
                break;
            case EMPLOYER:
                Employer employer = employerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No employer found with provided email"));
                employer.setVerified(request.isVerify());
                employerRepo.save(employer);
                msg = "Employer with email " + email + " " + (employer.isVerified() ? "verified" : "unverified");
                break;
            default:
                msg = "Unknown user role " + request.getRole();
                break;
        }

        return BaseResponse.ok(msg);
    }

    @GetMapping("/all/workers")
    public ResponseEntity<BaseResponse<List<Worker>>> getWorkers() {
        List<Worker> all = workerRepo.findAll();
        return BaseResponse.ok(all);
    }

    @GetMapping("/all/employers")
    public ResponseEntity<BaseResponse<List<Employer>>> getEmployers() {
        List<Employer> all = employerRepo.findAll();
        return BaseResponse.ok(all);
    }
}
