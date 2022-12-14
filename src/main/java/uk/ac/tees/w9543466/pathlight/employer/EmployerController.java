package uk.ac.tees.w9543466.pathlight.employer;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.tees.w9543466.pathlight.ApplicationStatus;
import uk.ac.tees.w9543466.pathlight.BaseController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.WorkStatus;
import uk.ac.tees.w9543466.pathlight.employer.dto.ApplicationItemDto;
import uk.ac.tees.w9543466.pathlight.employer.dto.ApplicationResponse;
import uk.ac.tees.w9543466.pathlight.employer.dto.WorkDto;
import uk.ac.tees.w9543466.pathlight.employer.dto.WorkItem;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.entity.Work;
import uk.ac.tees.w9543466.pathlight.employer.repo.EmployerRepo;
import uk.ac.tees.w9543466.pathlight.employer.repo.WorkRepo;
import uk.ac.tees.w9543466.pathlight.worker.dto.ProfileResponse;
import uk.ac.tees.w9543466.pathlight.worker.entity.Application;
import uk.ac.tees.w9543466.pathlight.worker.repo.ApplicationRepo;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequestMapping("/employer")
@RestController
public class EmployerController extends BaseController {

    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private EmployerRepo employerRepo;
    @Autowired
    private WorkRepo workRepo;

    @Autowired
    private WorkerRepo workerRepo;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<ProfileResponse>> getProfile() {
        String email = getUserEmail();
        Employer employer = employerRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No employer found with provided email"));
        var response = mapper.map(employer, ProfileResponse.class);
        return BaseResponse.ok(response);
    }

    @PostMapping("/work")
    public ResponseEntity<BaseResponse<Void>> createWork(@Valid @RequestBody WorkItem request) {
        mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        PropertyMap<WorkItem, Work> mapping = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        mapper.addMappings(mapping);
        var work = mapper.map(request, Work.class);
        work.setCreatedBy(getUserEmail());
        work.setStatus(WorkStatus.NOT_STARTED);
        workRepo.save(work);
        return BaseResponse.success("Work created", HttpStatus.CREATED);
    }

    @DeleteMapping("/work/{workId}")
    public ResponseEntity<BaseResponse<WorkDto>> cancelWork(@PathVariable(name = "workId") long workId) {
        var email = getUserEmail();
        Work work = workRepo.findByCreatedByAndId(email, workId).orElseThrow(() -> new EntityNotFoundException("No work found with provided id"));
        workRepo.deleteById(work.getId());
        return getWorks();
    }

    @PutMapping("/work")
    public ResponseEntity<BaseResponse<Void>> updateWork(@RequestBody WorkItem request) {
        String userEmail = getUserEmail();
        var workEntity = workRepo.findByCreatedByAndId(userEmail, request.getId()).orElseThrow(() -> new EntityNotFoundException("No work found with provided id"));
        var status = workEntity.getStatus();
        mapper.map(request, workEntity);
        workEntity.setCreatedBy(userEmail);
        workEntity.setCreatedDate(Instant.now());
        workEntity.setStatus(status);
        workRepo.save(workEntity);
        return BaseResponse.ok("Work updated");
    }

    @GetMapping("/works")
    public ResponseEntity<BaseResponse<WorkDto>> getWorks() {
        var email = getUserEmail();
        var works = workRepo.findByCreatedBy(email);
        var workList = mapper.map(works, WorkItem[].class);
        var result = Arrays.stream(workList).sorted(Comparator.comparingLong(WorkItem::getStartTime)).collect(Collectors.toList());
        return BaseResponse.ok(new WorkDto(result));
    }

    @GetMapping("/work/{workId}/application")
    public ResponseEntity<BaseResponse<ApplicationResponse>> getApplications(@PathVariable(name = "workId") long workId) {
        var email = getUserEmail();
        var work = workRepo.findByCreatedByAndId(email, workId).orElseThrow(() -> new EntityNotFoundException("Work not found for the given workId"));
        var list = applicationRepo.findAllByWorkId(work.getId());
        var result = new ArrayList<ApplicationItemDto>();
        for (Application application : list) {
            ApplicationItemDto dto = mapper.map(application, ApplicationItemDto.class);
            var worker = workerRepo.findById(application.getWorkerId());
            if (worker.isPresent()) {
                dto.setWorker(worker.get());
                result.add(dto);
            }
        }
        return BaseResponse.ok(new ApplicationResponse(result));
    }

    @PostMapping("/work/application/{applicationId}/accept")
    public ResponseEntity<BaseResponse<Void>> acceptApplications(@PathVariable(name = "applicationId") long applicationId) {
        var application = applicationRepo.findById(applicationId).orElseThrow(() -> new EntityNotFoundException("No Application found with provided id"));
        var work = workRepo.findById(application.getWorkId()).orElseThrow(() -> new EntityNotFoundException("No work found for this application"));
        if (work.getStatus() != WorkStatus.NOT_STARTED) {
            return BaseResponse.fail("Cannot accept. Work status is " + work.getStatus().getStatus(), HttpStatus.CONFLICT);
        }
        if (!work.getCreatedBy().equalsIgnoreCase(getUserEmail())) {
            return BaseResponse.fail("Cannot accept. Not your work", HttpStatus.FORBIDDEN);
        }
        var list = applicationRepo.findAllByWorkId(application.getWorkId());
        var found = list.stream().anyMatch(item -> item.getApplicationStatus() == ApplicationStatus.ACCEPTED);
        if (found) {
            return BaseResponse.fail("Cannot accept. Another application is already accepted for this job", HttpStatus.CONFLICT);
        }
        application.setApplicationStatus(ApplicationStatus.ACCEPTED);
        applicationRepo.save(application);
        Double rate = application.getRate();
        if (rate != null && rate != 0) {
            work.setTotalRate(rate);
            workRepo.save(work);
        }
        return BaseResponse.ok("Application accepted");
    }

    @PostMapping("/work/application/{applicationId}/reject")
    public ResponseEntity<BaseResponse<Void>> rejectApplications(@PathVariable(name = "applicationId") long applicationId) {
        var application = applicationRepo.findById(applicationId).orElseThrow(() -> new EntityNotFoundException("No Application found with provided id"));
        var work = workRepo.findById(application.getWorkId()).orElseThrow(() -> new EntityNotFoundException("No work found for this application"));
        if (work.getCreatedBy().equalsIgnoreCase(getUserEmail())) {
            return BaseResponse.fail("Cannot accept. Not your work", HttpStatus.FORBIDDEN);
        }
        application.setApplicationStatus(ApplicationStatus.REJECTED);
        applicationRepo.save(application);
        return BaseResponse.ok("Application accepted");
    }
}
