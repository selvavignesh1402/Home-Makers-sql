package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public UserRegister registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        UserRegister user = new UserRegister();
        user.setName(name);
        user.setEmail(email);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);  // Ensure this is encoded

        System.out.println("Encoded Password: " + encodedPassword);

        // Generate OTP, hash it, and set to the user
        String generatedOtp = otpService.generateAndSendOTP(email);
        String encodedOtp = passwordEncoder.encode(generatedOtp);
        user.setOtp(encodedOtp);
        user.setOtpVerified(false);

        return userRepository.save(user);
    }

    public UserRegister registerOrFindUserWithGoogle(String name, String email) {
        Optional<UserRegister> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        UserRegister user = new UserRegister();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("defaultGooglePassword"));

        // Google users might not need OTP initially, but it can be added later if required.
        user.setOtpVerified(true);

        return userRepository.save(user);
    }

    public List<UserRegister> getAllUsers() {
        return userRepository.findAll();
    }

    public UserRegister deleteUser(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    public UserRegister findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserRegister saveUser(UserRegister user) {
        return userRepository.save(user);
    }

    public UserRegister verifyOtp(String email, String otp) {
        UserRegister user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Verify the OTP by checking if the hashed OTP matches
        if (passwordEncoder.matches(otp, user.getOtp())) {
            user.setOtpVerified(true);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    public UserRegister findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
    }

}
