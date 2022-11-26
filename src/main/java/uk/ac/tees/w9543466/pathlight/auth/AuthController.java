package uk.ac.tees.w9543466.pathlight.auth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.tees.w9543466.pathlight.BaseController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.ErrorCode;
import uk.ac.tees.w9543466.pathlight.UserRole;
import uk.ac.tees.w9543466.pathlight.auth.dto.LoginRequest;
import uk.ac.tees.w9543466.pathlight.auth.dto.RegisterRequest;
import uk.ac.tees.w9543466.pathlight.auth.entities.Roles;
import uk.ac.tees.w9543466.pathlight.auth.entities.User;
import uk.ac.tees.w9543466.pathlight.auth.repo.AuthRepo;
import uk.ac.tees.w9543466.pathlight.auth.repo.RoleRepo;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.repo.EmployerRepo;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthController extends BaseController {

    @Autowired
    private AuthRepo authRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private WorkerRepo workerRepo;
    @Autowired
    private EmployerRepo employerRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder pwdEncoder;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (authentication.isAuthenticated()) {
            return BaseResponse.ok("User signed-in successfully!.");
        } else {
            return BaseResponse.fail("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> registerUser(@Valid @RequestBody RegisterRequest request) {
        String role = request.getRole();
        if (!role.equalsIgnoreCase(UserRole.EMPLOYER.getRole()) && !role.equalsIgnoreCase(UserRole.WORKER.getRole())) {
            return BaseResponse.fail("Only EMPLOYER and WORKER user type is supported!", HttpStatus.BAD_REQUEST);
        }

        String email = request.getEmail();
        Optional<User> userWithEmail = authRepo.findByEmail(email);
        if (userWithEmail.isPresent()) {
            var currentRoles = roleRepo.findByUserId(userWithEmail.get().getEmail());
            var roles = currentRoles.stream().map(Roles::getRole).collect(Collectors.joining());
            if (roles.contains(role)) {
                return BaseResponse.fail("User already exists!", ErrorCode.ALREADY_EXISTS, HttpStatus.CONFLICT);
            }
        }

        if (role.equalsIgnoreCase(UserRole.WORKER.getRole())) {
            var worker = new ModelMapper().map(request, Worker.class);
            workerRepo.save(worker);
        } else if (role.equalsIgnoreCase(UserRole.EMPLOYER.getRole())) {
            var worker = new ModelMapper().map(request, Employer.class);
            employerRepo.save(worker);
        } else {
            return BaseResponse.fail("Unknown user role", HttpStatus.BAD_REQUEST);
        }

        if (userWithEmail.isPresent()) {
            var existingUser = userWithEmail.get();
            if (!upsertRole(existingUser.getEmail(), role)) {
                return BaseResponse.fail("Unknown user role", HttpStatus.BAD_REQUEST);
            }
            return BaseResponse.success("New role assigned to user", HttpStatus.CREATED);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(pwdEncoder.encode(request.getPassword()));
        var createdUser = authRepo.save(user);
        if (!upsertRole(createdUser.getEmail(), role)) {
            return BaseResponse.fail("Unknown user role", HttpStatus.BAD_REQUEST);
        }
        return BaseResponse.success("User registered successfully", HttpStatus.CREATED);
    }

    private boolean upsertRole(String userId, String role) {
        var newRole = new Roles();
        newRole.setUserId(userId);
        if (role.equalsIgnoreCase(UserRole.EMPLOYER.getRole())) {
            newRole.setRole(UserRole.EMPLOYER.getRole());
        } else if (role.equalsIgnoreCase(UserRole.WORKER.getRole())) {
            newRole.setRole(UserRole.WORKER.getRole());
        } else return false;
        roleRepo.save(newRole);
        return true;
    }
}
