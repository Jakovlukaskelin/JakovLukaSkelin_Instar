package hr.instar.instar.controller.admin;


import hr.instar.instar.doamin.LoginHistory;
import hr.instar.instar.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin/logins")
@AllArgsConstructor
public class AdminLoginHistoryController {
private final StoreRepository storeRepository;

@GetMapping("")
public String getCompleteLoginHistory(Model model) {
    List<LoginHistory> completeLoginHistory = storeRepository.getCompleteLoginHistory();
    model.addAttribute("completeLoginHistory", completeLoginHistory);
    return "admin/logins";
}

}
