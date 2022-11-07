//package uk.ac.tees.w9543466.pathlight.security;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import uk.ac.tees.w9543466.pathlight.login.AuthRepo;
//import uk.ac.tees.w9543466.pathlight.login.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final AuthRepo userRepository;
//
//    public CustomUserDetailsService(AuthRepo userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with username or email:" + email));
//        return new org.springframework.security.core.userdetails.User(user.getEmail(),
//                user.getPassword(), getUserRoles(user.getRole()));
//    }
//
//    private List<GrantedAuthority> getUserRoles(String userRoles) {
//        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
//        String[] roles = userRoles.split(",");
//        for (String role : roles) {
//            grantedAuthorityList.add(new SimpleGrantedAuthority(role.replaceAll("\\s+", "")));
//        }
//        return grantedAuthorityList;
//    }
//}