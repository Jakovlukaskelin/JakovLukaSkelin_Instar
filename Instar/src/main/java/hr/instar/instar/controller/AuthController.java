package hr.instar.instar.controller;

import hr.instar.instar.service.UserService;
import lombok.AllArgsConstructor;
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
        if (userService.loginUser(username, password) != null) {
            return "redirect:/store";
        }
        return "redirect:/auth/login?error=true";
    }
}