package hr.instar.instar.session;


import hr.instar.instar.doamin.Proizvod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Proizvod product;
    private int quantity;
}
