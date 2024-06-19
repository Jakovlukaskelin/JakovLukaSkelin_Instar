package hr.instar.instar.controller;


import hr.instar.instar.dto.PurchaseHistoryDto;
import hr.instar.instar.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("store/purchases")
@AllArgsConstructor
public class PurchaseHistoryController {

    private final StoreRepository storeRepository;

    @GetMapping("")
    public String showCompletePurchaseHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        PurchaseHistoryDto purchaseHistory = storeRepository.getPurchaseHistoryForUser(username);
        model.addAttribute("purchaseHistory", purchaseHistory);
        return "purchases";
    }
}
