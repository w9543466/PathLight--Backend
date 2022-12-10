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
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.entity.Work;
import uk.ac.tees.w9543466.pathlight.employer.repo.EmployerRepo;
import uk.ac.tees.w9543466.pathlight.employer.repo.WorkRepo;
import uk.ac.tees.w9543466.pathlight.worker.dto.*;
import uk.ac.tees.w9543466.pathlight.worker.entity.Application;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.entity.WorkerPref;
import uk.ac.tees.w9543466.pathlight.worker.repo.ApplicationRepo;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerPrefRepo;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/worker")
@RestController
public class WorkerController extends BaseController {
    @Autowired
    private WorkerRepo workerRepo;
    @Autowired
    private EmployerRepo employerRepo;
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
    public ResponseEntity<BaseResponse<WorkResponse>> getWorks() {
        var result = new ArrayList<WorkItemResponse>();
        Worker loggedInWorker = getLoggedInWorker();
        var email = loggedInWorker.getEmail();
        var workerId = loggedInWorker.getId();
//        var preference = workerPrefRepo.findByEmail(email);
//        if (preference.isEmpty()) {
//            return BaseResponse.fail("Worker profile is incomplete", ErrorCode.SETUP_INCOMPLETE, HttpStatus.FORBIDDEN);
//        }
        //var pref = preference.get();
        var works = workRepo.findAll();
        for (var work : works) {
//            boolean matches = LocationUtil.doesLocationMatch(work.getLat(), work.getLng(), pref.getLocationLat(), pref.getLocationLng(), pref.getRadius());
//            if (!matches) continue;
            Optional<Employer> employerOptional = employerRepo.findByEmail(work.getCreatedBy());
            boolean applied = applicationRepo.existsByWorkIdAndWorkerId(work.getId(), workerId);
            var mapped = mapper.map(work, WorkItemResponse.class);
            employerOptional.ifPresent(employer -> {
                mapped.setEmployerName(employer.getFirstName() + " " + employer.getLastName());
                mapped.setApplied(applied);
            });
            result.add(mapped);
        }
        var sorted = result.stream().sorted(Comparator.comparingLong(WorkItemResponse::getStartTime)).collect(Collectors.toList());
        return BaseResponse.ok(new WorkResponse(sorted));

    }

    @GetMapping("/works/application")
    public ResponseEntity<BaseResponse<WorkerApplicationResponse>> getApplications() {
        var worker = getLoggedInWorker();
        var workerId = worker.getId();
        var list = applicationRepo.findAllByWorkerId(workerId);
        var result = mapper.map(list, WorkerApplicationItem[].class);
        for (WorkerApplicationItem item : result) {
            var work = workRepo.findById(item.getWorkId());
            if (work.isPresent()) {
                Work work1 = work.get();
                var empEmail = work1.getCreatedBy();
                var employer = employerRepo.findByEmail(empEmail);
                item.setWork(work1);
                employer.ifPresent(item::setEmployer);
            }
        }
        return BaseResponse.ok(new WorkerApplicationResponse(Arrays.asList(result)));
    }

    @PostMapping("/works/application")
    public ResponseEntity<BaseResponse<Void>> applyForWork(@Valid @RequestBody WorkApplicationRequest request) {
        var workId = request.getWorkId();
        var rate = request.getProposedRate();
        var worker = getLoggedInWorker();
        Long workerId = worker.getId();
        var alreadyApplied = applicationRepo.existsByWorkIdAndWorkerId(workId, workerId);
        if (alreadyApplied) {
            return BaseResponse.fail("You have already applied for this work", ErrorCode.ALREADY_EXISTS, HttpStatus.FORBIDDEN);
        }
        if (!worker.isVerified()) {
            return BaseResponse.fail("Worker not verified", ErrorCode.NOT_VERIFIED, HttpStatus.FORBIDDEN);
        }
        var work = workRepo.findById(workId).orElseThrow(() -> new EntityNotFoundException("No work found with provided id"));
//        var preference = workerPrefRepo.findByEmail(worker.getEmail());
//        if (preference.isEmpty()) {
//            return BaseResponse.fail("Worker preference is incomplete", ErrorCode.SETUP_INCOMPLETE, HttpStatus.FORBIDDEN);
//        }
//        var pref = preference.get();
//        boolean matches = LocationUtil.doesLocationMatch(work.getLat(), work.getLng(), pref.getLocationLat(), pref.getLocationLng(), pref.getRadius());
//        if (!matches) {
//            String msg = "The work's location is out of bounds from your preferred location and radius, please try with another work";
//            return BaseResponse.fail(msg, ErrorCode.WORK_MISMATCH, HttpStatus.FORBIDDEN);
//        }
        rate = rate == 0 ? work.getTotalRate() : rate;
        if (rate == 0) {
            return BaseResponse.fail("Please provide a total rate for the entire service", ErrorCode.DATA_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
        var application = new Application();
        application.setWorkerId(workerId);
        application.setWorkId(workId);
        application.setRate(rate);
        application.setApplicationStatus(ApplicationStatus.APPLIED);
        applicationRepo.save(application);
        return BaseResponse.ok("Application submitted");

    }

    private Worker getLoggedInWorker() {
        var email = getUserEmail();
        return workerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No worker found with provided email"));
    }
}