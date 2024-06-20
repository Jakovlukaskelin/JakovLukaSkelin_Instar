package hr.instar.instar.controller.admin;


import hr.instar.instar.doamin.LoginHistory;
import hr.instar.instar.doamin.users;
import hr.instar.instar.repository.StoreRepository;
import hr.instar.instar.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("admin/logins")
@AllArgsConstructor
public class AdminLoginHistoryController {
private final StoreRepository storeRepository;
private final UserService userService;

@GetMapping("")
public String getCompleteLoginHistory(Model model) {
    List<LoginHistory> completeLoginHistory = storeRepository.getCompleteLoginHistory();
    model.addAttribute("completeLoginHistory", completeLoginHistory);
    return "admin/logins";
}


}
