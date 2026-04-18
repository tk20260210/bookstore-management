package com.example.bookstore;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String passwordConfirm,
                           Model model) {

        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Not correspond password");
            model.addAttribute("username", username);
            return "register";
        }

        if (registerService.getAppUser(username).isPresent()) {
            model.addAttribute("error", "Already exists");
            model.addAttribute("username", username);
            return "register";
        }

        model.addAttribute("username", username);
        model.addAttribute("password", password);
        return "register-confirm";
    }

    @PostMapping("/register/complete")
    public String complete(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        registerService.register(username, password);
        model.addAttribute("username", username);
        return "redirect:/login";
    }
}
