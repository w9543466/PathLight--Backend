package uk.ac.tees.w9543466.pathlight.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uk.ac.tees.w9543466.pathlight.auth.repo.AuthRepo;
import uk.ac.tees.w9543466.pathlight.auth.repo.RoleRepo;
import uk.ac.tees.w9543466.pathlight.auth.entities.Roles;
import uk.ac.tees.w9543466.pathlight.auth.entities.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private AuthRepo repository;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = repository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Details not found"));
        if (encoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(email, password, getUserRoles(user.getEmail()));
        } else {
            throw new BadCredentialsException("Password mismatch");
        }
    }

    private List<GrantedAuthority> getUserRoles(String email) {
        var roles = roleRepo.findByUserId(email);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (Roles role : roles) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
