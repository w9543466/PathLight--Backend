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
import org.springframework.web.bind.annotation.*;
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
import uk.ac.tees.w9543466.pathlight.mail.EmailDetails;
import uk.ac.tees.w9543466.pathlight.mail.EmailService;
import uk.ac.tees.w9543466.pathlight.worker.entity.Worker;
import uk.ac.tees.w9543466.pathlight.worker.repo.WorkerRepo;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
public class AuthController extends BaseController {

    @Autowired
    private EmailService emailService;
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

    public static String generateRandomPassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Void>> check() {
        return BaseResponse.ok("Hit");
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@Valid @RequestBody LoginRequest request) {
        String requestedRole = request.getRole();
        String email = request.getEmail();
        String password = request.getPassword();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(requestedRole);
        ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
        arrayList.add(authority);

        var authenticationToken = new UsernamePasswordAuthenticationToken(email, password, arrayList);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userRoles = roleRepo.findByUserId(email);
        boolean noneMatch = userRoles.stream().noneMatch(role -> role.getRole().equalsIgnoreCase("ROLE_" + requestedRole));
        if (noneMatch) {
            return BaseResponse.fail("Invalid user role", HttpStatus.UNAUTHORIZED);
        }
        if (!authentication.isAuthenticated()) {
            return BaseResponse.fail("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        return BaseResponse.ok("User signed-in successfully!.");
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

    @GetMapping("/forgotPassword/{emailId}")
    public ResponseEntity<BaseResponse<Void>> forgotPassword(@PathVariable(name = "emailId") String emailId) {
        var user = authRepo.findByEmail(emailId).orElseThrow(() -> new EntityNotFoundException("Invalid email id"));
        String newPwd = generateRandomPassword(8);
        String body = "Your new password is " + newPwd;
        String subject = "Password reset!";
        boolean sent = emailService.send(new EmailDetails(emailId, body, subject));
        if (!sent) {
            return BaseResponse.fail("Unable to reset password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(pwdEncoder.encode(newPwd));
        authRepo.save(user);
        return BaseResponse.ok("Password has been reset and sent to your registered email id");
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
