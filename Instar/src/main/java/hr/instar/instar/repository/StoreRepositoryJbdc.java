package hr.instar.instar.repository;

import hr.instar.instar.doamin.*;
import hr.instar.instar.dto.BillDetailsDto;
import hr.instar.instar.dto.ItemDetailsDto;
import hr.instar.instar.dto.PurchaseHistoryDto;
import hr.instar.instar.session.Cart;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Primary
@Repository
public class StoreRepositoryJbdc implements StoreRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    public StoreRepositoryJbdc(JdbcTemplate jdbcTemplate, StringHttpMessageConverter stringHttpMessageConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.stringHttpMessageConverter = stringHttpMessageConverter;
    }
    @Override
    public List<Proizvod> getAllProducts() {
       String sql = "SELECT * FROM Proizvod";
       return jdbcTemplate.query(sql,(ResultSet rs, int rowNum) -> new Proizvod(
               rs.getInt("IDProizvod"),
               rs.getInt("KategorijaID"),
               rs.getString("Naziv"),
               rs.getFloat("Cijena"),
               rs.getInt("DostupnaKolicina")));
    }

    @Override
    public List<Kategorija> getAllCategories() {
        String sql = "SELECT * FROM Kategorija";
        return jdbcTemplate.query(sql,(ResultSet rs, int rowNum) -> new Kategorija(
                rs.getInt("IDKategorija"),
                rs.getString("Naziv")));
    }

    @Override
    public Proizvod getProductById(int productId) {
        String sql = "SELECT * FROM Proizvod WHERE IDProizvod = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (ResultSet rs, int rowNum) -> new Proizvod(
                rs.getInt("IDProizvod"),
                rs.getInt("KategorijaID"),
                rs.getString("Naziv"),
                rs.getFloat("Cijena"),
                rs.getInt("DostupnaKolicina")));
    }

    @Override
    public void savePurchase(Cart cart, String username, String nacinKupovine) {
        Racun racun = cart.generateRacun(username, nacinKupovine);

        final String insertRacunSql = "INSERT INTO Racun (Username, VrijemeKupovine, NacinKupovine, UkupnaCijena) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertRacunSql, new String[]{"IDRacun"});
            ps.setString(1, racun.getUsername());
            ps.setString(2, racun.getVrijemeKupovine());
            ps.setString(3, racun.getNacinKupovine());
            ps.setFloat(4, racun.getUkupnaCijena());
            return ps;
        }, keyHolder);

        int racunId = keyHolder.getKey().intValue();
        List<Stavka> stavke = cart.generateStavka(racunId);

        final String insertStavkaSql = "INSERT INTO Stavka (RacunID, ProizvodID, Kolicina) VALUES (?, ?, ?)";
        final String updateProizvodSql = "UPDATE Proizvod SET DostupnaKolicina = DostupnaKolicina - ? WHERE IDProizvod = ?";
        for (Stavka stavka : stavke) {
            jdbcTemplate.update(insertStavkaSql, stavka.getRacunID(), stavka.getProizvodID(), stavka.getKolicina());
            jdbcTemplate.update(updateProizvodSql, stavka.getKolicina(), stavka.getProizvodID());
        }

    }

    @Override
    public PurchaseHistoryDto getPurchaseHistoryForUser(String username) {
        List<BillDetailsDto> billDetailsDtoList = new ArrayList<>();
        List<Racun> racunList = getRacunListForUser(username);
        for(Racun racun : racunList){
            List<Stavka> stavkaList = getStavkaListForRacun(racun.getIdRacun());

            List<ItemDetailsDto> itemDetailsDtoList = new ArrayList<>();
            for(Stavka stavka : stavkaList){
                Proizvod proizvod = getProductById(stavka.getProizvodID());

                ItemDetailsDto itemDetailsDto = new ItemDetailsDto(
                        stavka.getIdStavka(),
                        stavka.getRacunID(),
                        stavka.getProizvodID(),
                        stavka.getKolicina(),
                        proizvod.getNaziv(),
                        proizvod.getCijena());

             itemDetailsDtoList.add(itemDetailsDto);

            }
            BillDetailsDto billDetailsDto = new BillDetailsDto(racun, itemDetailsDtoList);
            billDetailsDtoList.add(billDetailsDto);

        }

        return new PurchaseHistoryDto(billDetailsDtoList);
    }

    private List<Stavka> getStavkaListForRacun(Integer idRacun) {
        String sql = "SELECT * FROM Stavka WHERE RacunID = ?";
        return jdbcTemplate.query(sql, new Object[]{idRacun}, (ResultSet rs, int rowNum) -> new Stavka(
                rs.getInt("IDStavka"),
                rs.getInt("RacunID"),
                rs.getInt("ProizvodID"),
                rs.getInt("Kolicina")));
    }

    private List<Racun> getRacunListForUser(String username) {
        String sql = "SELECT * FROM Racun WHERE Username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, (ResultSet rs, int rowNum) -> new Racun(
                rs.getInt("IDRacun"),
                rs.getString("Username"),
                rs.getString("VrijemeKupovine"),
                rs.getString("NacinKupovine"),
                rs.getFloat("UkupnaCijena")));

    }

    @Override
    public void addProduct(Proizvod product) {
 String sql = "INSERT INTO Proizvod (KategorijaID, Naziv, Cijena, DostupnaKolicina) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getKategorijaID(), product.getNaziv(), product.getCijena(), product.getDostupnaKolicina());
    }

    @Override
    public void updateProduct(Proizvod product) {
          String sql = "UPDATE Proizvod SET KategorijaID = ?, Naziv = ?, Cijena = ?, DostupnaKolicina = ? WHERE IDProizvod = ?";
          jdbcTemplate.update(sql, product.getKategorijaID(), product.getNaziv(), product.getCijena(), product.getDostupnaKolicina(), product.getIdProizvod());
    }

    @Override
    public void deleteProductById(int productId) {
          String sql = "DELETE FROM Proizvod WHERE IDProizvod = ?";
          jdbcTemplate.update(sql, productId);
    }

    @Override
    public boolean productHasDependentItems(int productId) {
       String sql = "SELECT COUNT(*) FROM Stavka WHERE ProizvodID = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class) > 0;
    }

    @Override
    public void addCategory(Kategorija category) {
String sql = "INSERT INTO Kategorija (Naziv) VALUES (?)";
        jdbcTemplate.update(sql, category.getNaziv());
    }

    @Override
    public void updateCategory(Kategorija category) {
String sql = "UPDATE Kategorija SET Naziv = ? WHERE IDKategorija = ?";
        jdbcTemplate.update(sql, category.getNaziv(), category.getIdKategorija());
    }

    @Override
    public void deleteCategoryById(int categoryId) {

        String sql = "DELETE FROM Kategorija WHERE IDKategorija = ?";
        jdbcTemplate.update(sql, categoryId);
    }

    @Override
    public Kategorija getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Kategorija WHERE IDKategorija = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, (ResultSet rs, int rowNum) -> new Kategorija(
                rs.getInt("IDKategorija"),
                rs.getString("Naziv")));
    }

    @Override
    public boolean categoryHasDependentProducts(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Proizvod WHERE KategorijaID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, Integer.class) > 0;
    }

    @Override
    public void addRequestHistory(RequestHistory requestHistory) {

        String sql = "INSERT INTO RequestHistory (Username, RequestTime, RequestType) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, requestHistory.getUsername(), requestHistory.getVrijemeRequesta(), requestHistory.getRequestType());
    }

    @Override
    public void addLoginHistory(LoginHistory loginHistory) {
String sql = "INSERT INTO LoginHistory (Username, vrijemeLogina, ipAdresa) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, loginHistory.getUsername(), loginHistory.getVrijemeLogina(), loginHistory.getIpAdresa());
    }

    @Override
    public List<LoginHistory> getCompleteLoginHistory() {
        String sql = "SELECT * FROM LoginHistory";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> new LoginHistory(
                rs.getInt("IDLoginHistory"),
                rs.getString("Username"),
                rs.getString("vrijemeLogina"),
                rs.getString("ipAdresa")));
    }

    @Override
    public List<PurchaseHistoryDto> getCompletePurchaseHistory() {
        List<PurchaseHistoryDto> purchaseHistoryDtoList = new ArrayList<>();
        List<String> allUserNames = getAllUserNamesWithInvoices();
        for(String username : allUserNames){
            PurchaseHistoryDto purchaseHistoryDto = getPurchaseHistoryForUser(username);
            purchaseHistoryDtoList.add(purchaseHistoryDto);
        }
        return purchaseHistoryDtoList;
    }

    @Override
    public void deleteUser(String username) {
        String deleteAuthoritiesSql = "DELETE FROM authorities WHERE username = ?";
        jdbcTemplate.update(deleteAuthoritiesSql, username);
        String sql = "DELETE FROM users WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }

    @Override
    public List<users> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> new users(
                rs.getString("username"),
                rs.getString("password")));
    }

    private List<String> getAllUserNamesWithInvoices() {
    String sql = "SELECT u.username"+
                "FROM users u " +
                    "JOIN authorities a ON u.username = a.username " +
                "LEFT JOIN Racun r ON u.username = r.username " +
                "WHERE a.authority = 'ROLE_USER' " +
                "GROUP BY u.username " +
                "HAVING COUNT(r.IDRacun) > 0";

        return jdbcTemplate.queryForList(sql, String.class);

    }



}
