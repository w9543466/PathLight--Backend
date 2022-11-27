package uk.ac.tees.w9543466.pathlight;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class PathLightApplicationTests {

    @Test
    void pwdTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        var result = encoder.encode("notebook");
        System.out.println(result);
        boolean matches = encoder.matches("notebook", result);
        assert matches;
    }

}
