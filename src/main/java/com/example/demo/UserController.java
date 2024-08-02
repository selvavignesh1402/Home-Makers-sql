package com.example.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/userregister")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserRegister registerUser(@RequestBody UserRegister user) {
        try {
            return userService.registerUser(user.getName(), user.getEmail(), user.getPassword());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/google")
    public UserRegister registerUserWithGoogle(@RequestBody GoogleTokenRequest tokenRequest) {
        try {
            String idTokenString = tokenRequest.getIdToken();
            GoogleIdToken idToken = GoogleVerifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                return userService.registerOrFindUserWithGoogle(name, email);
            } else {
                throw new IllegalArgumentException("Invalid Google ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
    }
}
