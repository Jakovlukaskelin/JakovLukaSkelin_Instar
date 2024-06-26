package hr.instar.instar.controller;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import hr.instar.instar.paypal.PaypalService;
import hr.instar.instar.repository.StoreRepository;
import hr.instar.instar.session.Cart;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("store/checkout")
@AllArgsConstructor
@SessionAttributes("cart")
public class CheckoutController {

    private final StoreRepository storeRepository;
    private final PaypalService paypalService;



@ModelAttribute("cart")
    public Cart getCart() {
    return new Cart();

}




@PostMapping("/process")
    public String checkout(@ModelAttribute("cart") Cart cart,
                           @RequestParam String paymentMethod ,
                            Authentication authentication) {

    if (cart.IsEmpty()) {
        return "redirect:/store/cart?purchase=empty_cart";
    }
    String username = authentication.getName();
    if ("Cash".equals(paymentMethod)) {
        storeRepository.savePurchase(cart, username, paymentMethod);
        cart.removeAllItems();
        return "redirect:/store/cart?purchase=success&paymentMethod=Cash";

    } else if ("Paypal".equals(paymentMethod)) {
        try {
            double totalAmount = cart.getTotalPrice();
            Payment payment = paypalService.createPayment(totalAmount, "EUR", "paypal", "sale",
                    "Test payment",
                    "https://thebestweb.azurewebsites.net/store/cart?purchase=error&paymentMethod=Paypal",
                    "https://thebestweb.azurewebsites.net/store/cart?purchase=success&paymentMethod=Paypal");
            for(Links link : payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
            return "redirect:/store/cart?purchase=error&paymentMethod=Paypal";
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return "redirect:/store/cart?purchase=error&paymentMethod=Paypal";
        }
    } else {
        return "redirect:/store/cart?purchase=error";
    }
}



}
