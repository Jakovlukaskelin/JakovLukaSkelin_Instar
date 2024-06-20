package hr.instar.instar.controller;

import hr.instar.instar.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private  final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/auth/login";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login/process")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        logger.info("Attempting login for user: {}", username);
        if (userService.loginUser(username, password) != null) {
            logger.info("Login successful for user: {}", username);
            return "redirect:/store";
        }
        logger.info("Login failed for user: {}", username);
        return "redirect:/auth/login?error=true";
    }
}