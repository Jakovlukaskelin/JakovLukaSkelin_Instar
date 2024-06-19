package hr.instar.instar.controller;


import hr.instar.instar.doamin.Kategorija;
import hr.instar.instar.doamin.Proizvod;
import hr.instar.instar.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("store")
@AllArgsConstructor
public class StoreFrontController {


    private final StoreRepository storeRepository;


    @GetMapping("")
    public String home() {
        return "home";
    }
    @GetMapping("/products")
    public String getAllProducts(@RequestParam(required = false) Integer categoryID, Model model) {
        List<Proizvod> products = storeRepository.getAllProducts();
        List<Kategorija> categories = storeRepository.getAllCategories();

        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Kategorija::getIdKategorija, Kategorija::getNaziv));

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryMap", categoryMap);

        if (categoryID != null) {
            String selectedCategoryName = categoryMap.get(categoryID);
            if (selectedCategoryName != null) {
                model.addAttribute("selectedCategoryName", selectedCategoryName);
            }
        }
        return "products";
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Kategorija> categories = storeRepository.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }
}
