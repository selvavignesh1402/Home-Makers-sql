package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserRegister registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }

//        if (!PASSWORD_PATTERN.matcher(password).matches()) {
//            throw new IllegalArgumentException("Password must contain at least one digit, one lowercase, " +
//                    "one uppercase letter, one special character, and must be at least 8 characters long");
//        }

        UserRegister user = new UserRegister();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

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
        // You can set a default password for users registered through Google
        user.setPassword(passwordEncoder.encode("defaultGooglePassword"));

        return userRepository.save(user);
    }
    
    public UserRegister registerUserWithGoogle(String name, String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserRegister newUser = new UserRegister();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setPassword(null);
                    return userRepository.save(newUser);
                });
    }
}
