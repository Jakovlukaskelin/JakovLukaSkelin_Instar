package hr.instar.instar.controller;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import hr.instar.instar.doamin.Proizvod;
import hr.instar.instar.paypal.PaypalService;
import hr.instar.instar.repository.StoreRepository;
import hr.instar.instar.session.Cart;
import hr.instar.instar.session.CartItem;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("store/cart")
@AllArgsConstructor
@SessionAttributes("cart")
public class CartController {


    private final StoreRepository storeRepository;
    private final PaypalService paypalService;



    @ModelAttribute("cart")
    public Cart getCart() {
        return new Cart();
    }


    @GetMapping("")
    public String showCart(@ModelAttribute("cart") Cart cart,
                           @RequestParam(required = false)String purchase,
                           @RequestParam(required = false)String paymentMethod,
                           @RequestParam(required = false)String paymentId,
                           @RequestParam(required = false)String PayerID,
                           Authentication authentication,
                            Model model) {

        String purchaseStatus = purchase;
        boolean hasUserRole = false;
        if (authentication != null && authentication.isAuthenticated()) {
            hasUserRole = false;
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if ("ROLE_USER".equals(authority.getAuthority())) {
                    hasUserRole = true;
                    break;
                }
            }
        }
        if (hasUserRole && "success".equals(purchase)) {
            String username = authentication.getName();
            if ("Paypal".equals(paymentMethod)) {
                if (paymentId != null && PayerID != null) {
                    if (!paymentId.isBlank() && !PayerID.isBlank()) {
                        try {
                            Payment payment = paypalService.executePayment(paymentId, PayerID);
                            if ("approved".equals(payment.getState())) {
                                storeRepository.savePurchase(cart, username, paymentMethod);
                                cart.removeAllItems();
                            } else {
                                purchaseStatus = "error";
                            }
                        } catch (PayPalRESTException e) {
                            e.printStackTrace();
                            purchaseStatus = "error";
                        }
                    }
                } else if ("Cash".equals(paymentMethod)) {
                    storeRepository.savePurchase(cart, username, paymentMethod);
                    cart.removeAllItems();

                }
            }
        }
     model.addAttribute("cartItems", cart.getCartItemList());
        model.addAttribute("purchaseStatus", purchaseStatus);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute("cart") Cart cart,
                            @RequestParam int productId,
                            @RequestParam int quantity) {
        Proizvod product = storeRepository.getProductById(productId);
        int currentQuantity = cart.getCurrentQuantityForProduct(productId);
        int newTotalQuantity = currentQuantity + quantity;

        if (newTotalQuantity <= product.getDostupnaKolicina()){
            cart.addItem(new CartItem(product, quantity));
        }
        return "redirect:/store/products";
    }
@PostMapping("/update")
    public String updateCart(@ModelAttribute("cart") Cart cart,
                             @RequestParam int productId,
                             @RequestParam int quantity) {
        cart.updateItemQuantity(productId, quantity);
        return "redirect:/store/cart";
    }

    @GetMapping("/remove")
    public String removeFromCart(@ModelAttribute("cart") Cart cart,
                                 @RequestParam int productId) {
        cart.removeItem(productId);
        return "redirect:/store/cart";
    }

    @GetMapping("/clear")
    public String clearCart(@ModelAttribute("cart") Cart cart) {
        cart.removeAllItems();
        return "redirect:/store/cart";
    }
}
