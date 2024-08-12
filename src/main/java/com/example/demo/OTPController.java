//package com.example.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/otp")
//@CrossOrigin(origins = "http://localhost:3000") 
//public class OTPController {
//
//    @Autowired
//    private EmailService otpService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendOtp(@RequestParam String email) {
//        String otp = otpService.generateOTP();
//        otpService.sendOtpEmail(email, otp);
//        otpService.saveOtpForUser(email, otp);
//        return ResponseEntity.ok("OTP sent successfully");
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
//        if (otpService.verifyOtp(email, otp)) {
//            return ResponseEntity.ok("OTP verified successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
//        }
//    }
//}