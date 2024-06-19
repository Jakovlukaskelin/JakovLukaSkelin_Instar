package hr.instar.instar.controller.admin;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/purchases")
@AllArgsConstructor
public class AdminPurchaseHistoryController {

        @GetMapping("")
        public String getCompletePurchaseHistory() {
            return "admin/purchases";
        }
}
