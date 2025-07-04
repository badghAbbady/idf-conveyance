package esiag.back.controllers.sample;

import esiag.back.models.sample.User;
import esiag.back.services.sample.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {
        Optional<User> user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (user.isPresent()) {
            return ResponseEntity.ok().build(); // Authenticated successfully
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Authentication failed
        }
    }
    @PostMapping(value="/signup",consumes = "application/json")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.addUser(user);
        if (savedUser != null) {
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
