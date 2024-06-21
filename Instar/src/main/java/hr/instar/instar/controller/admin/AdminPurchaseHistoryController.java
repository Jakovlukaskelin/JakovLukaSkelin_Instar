package hr.instar.instar.controller.admin;


import hr.instar.instar.dto.PurchaseHistoryDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("admin/purchases")
@AllArgsConstructor
public class AdminPurchaseHistoryController {


    @GetMapping("")
    public String getCompletePurchaseHistory() {
        return "admin/purchases";
    }

}
