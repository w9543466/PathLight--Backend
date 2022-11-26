package uk.ac.tees.w9543466.pathlight.worker;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.tees.w9543466.pathlight.ApplicationStatus;
import uk.ac.tees.w9543466.pathlight.BaseController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.ErrorCode;
import uk.ac.tees.w9543466.pathlight.employer.entity.Work;
import uk.ac.tees.w9543466.pathlight.employer.repo.WorkRepo;
import uk.ac.tees.w9543466.pathlight.worker.dto.PreferenceDto;
import uk.ac.tees.w9543466.pathlight.worker.dto.ProfileResponse;
import uk.ac.tees.w9543466.pathlight.worker.entity.Application;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.entity.WorkerPref;
import uk.ac.tees.w9543466.pathlight.worker.repo.ApplicationRepo;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerPrefRepo;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/worker")
@RestController
public class WorkerController extends BaseController {
    @Autowired
    private WorkerRepo workerRepo;
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private WorkerPrefRepo workerPrefRepo;
    @Autowired
    private WorkRepo workRepo;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile() {
        var worker = getLoggedInWorker();
        var response = mapper.map(worker, ProfileResponse.class);
        return BaseResponse.ok(response);
    }

    @PostMapping("/preference")
    public ResponseEntity<BaseResponse<Void>> savePreference(@RequestBody PreferenceDto request) {
        String email = getUserEmail();
        request.setEmail(email);
        var pref = mapper.map(request, WorkerPref.class);
        workerPrefRepo.save(pref);
        return BaseResponse.ok("Preference saved");
    }

    @GetMapping("/preference")
    public ResponseEntity<BaseResponse<PreferenceDto>> getPreference() {
        var preference = workerPrefRepo.findByEmail(getUserEmail()).orElseThrow(() -> new EntityNotFoundException("No preference found with provided email"));
        var pref = mapper.map(preference, PreferenceDto.class);
        return BaseResponse.ok(pref);
    }

    @GetMapping("/works")
    public ResponseEntity<BaseResponse<List<Work>>> getWorks() {
        var result = new ArrayList<Work>();
        var email = getUserEmail();
        var preference = workerPrefRepo.findByEmail(email);
        if (preference.isEmpty()) {
            return BaseResponse.fail("Worker onboarding is incomplete", ErrorCode.SETUP_INCOMPLETE, HttpStatus.FORBIDDEN);
        } else {
            var pref = preference.get();
            var works = workRepo.findAll();
            for (var work : works) {
                boolean matches = doesLocationMatch(work.getLat(), work.getLng(), pref.getLocationLat(), pref.getLocationLng(), pref.getRadius());
                if (!matches) continue;
                result.add(work);
            }
            return BaseResponse.ok(result);
        }
    }

    @PostMapping("works/{workId}/apply")
    public ResponseEntity<BaseResponse<Long>> applyForWork(@PathVariable(name = "workId") long workId) {
        var worker = getLoggedInWorker();
        Long workerId = worker.getId();
        var alreadyApplied = applicationRepo.existsByWorkerId(workerId);
        if (alreadyApplied) {
            return BaseResponse.fail("You have already applied for this work", ErrorCode.ALREADY_EXISTS, HttpStatus.FORBIDDEN);
        }
        if (!worker.isVerified()) {
            return BaseResponse.fail("Worker not verified", ErrorCode.NOT_VERIFIED, HttpStatus.FORBIDDEN);
        }
        var work = workRepo.findById(workId).orElseThrow(() -> new EntityNotFoundException("No work found with provided id"));
        var preference = workerPrefRepo.findByEmail(worker.getEmail());
        if (preference.isEmpty()) {
            return BaseResponse.fail("Worker preference is incomplete", ErrorCode.SETUP_INCOMPLETE, HttpStatus.FORBIDDEN);
        } else {
            var pref = preference.get();
            boolean matches = doesLocationMatch(work.getLat(), work.getLng(), pref.getLocationLat(), pref.getLocationLng(), pref.getRadius());
            if (!matches) {
                String msg = "Your location does not match with the work's location, please try with another work";
                return BaseResponse.fail(msg, ErrorCode.WORK_MISMATCH, HttpStatus.FORBIDDEN);
            }
            var application = new Application();
            application.setWorkerId(workerId);
            application.setWorkId(workId);
            //application.setRate(rate);
            application.setApplicationStatus(ApplicationStatus.APPLIED);
            applicationRepo.save(application);
            return BaseResponse.ok(workId);
        }
    }

    private Worker getLoggedInWorker() {
        var email = getUserEmail();
        return workerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No worker found with provided email"));
    }

    private boolean doesLocationMatch(double lat1, double lng1, double lat2, double lng2, double radius) {
        var distance = distanceBetween(lat1, lng1, lat2, lng2);
        System.out.println("distance between worker and the work = " + distance);
        System.out.println("radius of the worker preferred work location = " + radius);
        return distance <= radius;
    }

    /**
     * @return the distance in meters
     */
    private double distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371;// earth's radius

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }
}