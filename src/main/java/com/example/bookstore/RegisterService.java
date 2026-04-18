package com.example.bookstore;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RegisterService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AppUser> getAppUser(String username) {
        return  appUserRepository.findByUsername(username);
    }

    public void register(String username, String password) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setEnabled(1);
        appUserRepository.save(user);
    }
}
