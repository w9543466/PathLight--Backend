package uk.ac.tees.w9543466.pathlight.auth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.tees.w9543466.pathlight.BaseController;
import uk.ac.tees.w9543466.pathlight.BaseResponse;
import uk.ac.tees.w9543466.pathlight.ErrorCode;
import uk.ac.tees.w9543466.pathlight.UserRole;
import uk.ac.tees.w9543466.pathlight.auth.dto.EmployerRegisterRequest;
import uk.ac.tees.w9543466.pathlight.auth.dto.LoginRequest;
import uk.ac.tees.w9543466.pathlight.auth.dto.WorkerRegisterRequest;
import uk.ac.tees.w9543466.pathlight.auth.entities.Roles;
import uk.ac.tees.w9543466.pathlight.auth.entities.User;
import uk.ac.tees.w9543466.pathlight.auth.repo.AuthRepo;
import uk.ac.tees.w9543466.pathlight.auth.repo.RoleRepo;
import uk.ac.tees.w9543466.pathlight.employer.entity.Employer;
import uk.ac.tees.w9543466.pathlight.employer.repo.EmployerRepo;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Void>> check() {
        return BaseResponse.ok("Hit");
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(request.getRole());
        ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
        arrayList.add(authority);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword(), arrayList)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (authentication.isAuthenticated()) {
            return BaseResponse.ok("User signed-in successfully!.");
        } else {
            return BaseResponse.fail("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup/worker")
    public ResponseEntity<BaseResponse<Void>> registerWorker(@Valid @RequestBody WorkerRegisterRequest request) {
        var role = UserRole.WORKER.getRole();
        String email = request.getEmail();
        Optional<User> userWithEmail = authRepo.findByEmail(email);
        if (userWithEmail.isPresent()) {
            var currentRoles = roleRepo.findByUserId(userWithEmail.get().getEmail());
            var roles = currentRoles.stream().map(Roles::getRole).collect(Collectors.joining());
            if (roles.contains(role)) {
                return BaseResponse.fail("User already exists!", ErrorCode.ALREADY_EXISTS, HttpStatus.CONFLICT);
            }
            upsertRole(userWithEmail.get().getEmail(), role);
            return BaseResponse.success("New role " + role + " assigned to user", HttpStatus.CREATED);
        }
        var worker = new ModelMapper().map(request, Worker.class);
        workerRepo.save(worker);

        User user = new User();
        user.setEmail(email);
        user.setPassword(pwdEncoder.encode(request.getPassword()));
        var createdUser = authRepo.save(user);
        upsertRole(createdUser.getEmail(), role);
        return BaseResponse.success("User registered successfully as worker", HttpStatus.CREATED);
    }

    @PostMapping("/signup/employer")
    public ResponseEntity<BaseResponse<Void>> registerEmployer(@Valid @RequestBody EmployerRegisterRequest request) {
        var role = UserRole.EMPLOYER.getRole();
        String email = request.getEmail();
        Optional<User> userWithEmail = authRepo.findByEmail(email);
        if (userWithEmail.isPresent()) {
            var existingUser = userWithEmail.get();
            String existingUserEmail = existingUser.getEmail();
            var currentRoles = roleRepo.findByUserId(existingUserEmail);
            var roles = currentRoles.stream().map(Roles::getRole).collect(Collectors.joining());
            if (roles.contains(role)) {
                return BaseResponse.fail("User already exists!", ErrorCode.ALREADY_EXISTS, HttpStatus.CONFLICT);
            }
            upsertRole(existingUserEmail, role);
            return BaseResponse.success("New role " + role + " assigned to user", HttpStatus.CREATED);
        }
        var worker = new ModelMapper().map(request, Employer.class);
        employerRepo.save(worker);

        User user = new User();
        user.setEmail(email);
        user.setPassword(pwdEncoder.encode(request.getPassword()));
        var createdUser = authRepo.save(user);
        upsertRole(createdUser.getEmail(), role);
        return BaseResponse.success("User registered successfully as employer", HttpStatus.CREATED);
    }

    private void upsertRole(String userId, String role) {
        var newRole = new Roles();
        newRole.setUserId(userId);
        newRole.setRole("ROLE_" + role);
        roleRepo.save(newRole);
    }
}
