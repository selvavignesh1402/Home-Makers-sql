package com.example.demo;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/userregister")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject BCryptPasswordEncoder

    @PostMapping("/register")
    public UserRegister registerUser(@RequestBody UserRegister user) {
        try {
            // Hash the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Generate OTP and send it via email
            String generatedOtp = otpService.generateAndSendOTP(user.getEmail());
            user.setOtp(passwordEncoder.encode(generatedOtp));
            user.setOtpVerified(false); // Set OTP verification status to false initially

            return userService.saveUser(user); // Save user with the OTP and unverified status
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public UserRegister verifyOtp(@RequestBody UserRegister user) {
        UserRegister existingUser = userService.findByEmail(user.getEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (passwordEncoder.matches(user.getOtp(), existingUser.getOtp())) {
            existingUser.setOtpVerified(true); // Mark the OTP as verified
            return userService.saveUser(existingUser);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP. Please try again.");
        }
    }

    @GetMapping
    public List<UserRegister> getAllUsers() {
        return userService.getAllUsers();
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Google ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Return 204 No Content for successful deletion
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
