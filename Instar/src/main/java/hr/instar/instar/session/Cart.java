package hr.instar.instar.session;


import hr.instar.instar.doamin.Racun;
import hr.instar.instar.doamin.Stavka;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private List<CartItem> cartItemList = new ArrayList<>();

    public void addItem(CartItem item) {
        Optional<CartItem> existingCartItem = cartItemList.stream()
                .filter(cartItem -> cartItem.getProduct().getIdProizvod().equals(item.getProduct().getIdProizvod()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + item.getQuantity());
        } else {
            cartItemList.add(item);
        }
    }


    public void removeItem(int productId) {
        cartItemList.removeIf(cartItem -> cartItem.getProduct().getIdProizvod().equals(productId));
    }

    public void removeAllItems() {
        cartItemList.clear();
    }


    public void updateItemQuantity(int productId, int quantity) {
        Optional<CartItem> existingCartItem = cartItemList.stream()
                .filter(cartItem -> cartItem.getProduct().getIdProizvod().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            if (quantity <= 0) {
                cartItemList.remove(existingCartItem.get());
            } else {
                existingCartItem.get().setQuantity(quantity);
            }
        }
    }

    public float getTotalPrice() {
        return cartItemList.stream()
                .map(cartItem -> cartItem.getProduct().getCijena() * cartItem.getQuantity())
                .reduce(0f, Float::sum);
    }

    public int getCurrentQuantityForProduct(int productId) {
        Optional<CartItem> existingCartItem = cartItemList.stream()
                .filter(cartItem -> cartItem.getProduct().getIdProizvod().equals(productId))
                .findFirst();

        return existingCartItem.map(CartItem::getQuantity).orElse(0);
    }


    public boolean IsEmpty() {
        return cartItemList.isEmpty();
    }

    public Racun generateRacun(String username, String nacinKupovine) {
        return new Racun(null, username, LocalDateTime.now().toString(), nacinKupovine, getTotalPrice());
    }

    public List<Stavka> generateStavka(int racunId) {
        List<Stavka> stavke = new ArrayList<>();
        for (CartItem item : cartItemList) {
            stavke.add(new Stavka(null, racunId, item.getProduct().getIdProizvod(), item.getQuantity()));
        }
        return stavke;
    }
}


