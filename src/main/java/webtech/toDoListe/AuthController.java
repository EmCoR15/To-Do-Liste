package webtech.toDoListe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.UUID;


// AuthController.java
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // oder deine Frontend-URL
public class AuthController {
    @Value("${app.auth.user}")
    private String validUser;

    @Value("${app.auth.pass}")
    private String validPass;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {


        if (validUser.equals(req.getUsername()) && validPass.equals(req.getPassword())) {
            // Dummy-Token (kann einfach eine UUID sein)
            String token = UUID.randomUUID().toString();
            return ResponseEntity.ok(new LoginResponse(token, req.getUsername()));
        }

        // sonst 401 = Unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
