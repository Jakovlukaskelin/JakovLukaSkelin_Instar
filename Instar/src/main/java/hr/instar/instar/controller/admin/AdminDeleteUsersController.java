package hr.instar.instar.controller.admin;

import hr.instar.instar.doamin.users;
import hr.instar.instar.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor

public class AdminDeleteUsersController {

    private final UserService userService;

    @PostMapping("/deleteUser/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "redirect:/admin/users?deletion=success";
    }

    @GetMapping("/users")
    public String getAllUsers(@RequestParam(required = false) String deletion, Model model) {
        List<users> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("deletionStatus", deletion);
        return "admin/users";
    }
}
