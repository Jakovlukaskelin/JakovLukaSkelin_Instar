package hr.instar.instar.repository;

import hr.instar.instar.doamin.Kategorija;
import hr.instar.instar.doamin.LoginHistory;
import hr.instar.instar.doamin.Proizvod;
import hr.instar.instar.doamin.RequestHistory;
import hr.instar.instar.dto.PurchaseHistoryDto;
import hr.instar.instar.session.Cart;

import java.util.List;

public interface StoreRepository {

    List<Proizvod> getAllProducts();
    List<Kategorija> getAllCategories();
    Proizvod getProductById(int productId);
    void savePurchase(Cart cart, String username, String nacinKupovine);
    PurchaseHistoryDto getPurchaseHistoryForUser(String username);

    void addProduct(Proizvod product);
    void updateProduct(Proizvod product);
    void deleteProductById(int productId);
    boolean productHasDependentItems(int productId);

    void addCategory(Kategorija category);
    void updateCategory(Kategorija category);
    void deleteCategoryById(int categoryId);
    Kategorija getCategoryById(int categoryId);
    boolean categoryHasDependentProducts (int categoryId);

    void addRequestHistory(RequestHistory requestHistory);
    void addLoginHistory(LoginHistory loginHistory);

    List<LoginHistory> getCompleteLoginHistory();

    List<PurchaseHistoryDto> getCompletePurchaseHistory();
}
