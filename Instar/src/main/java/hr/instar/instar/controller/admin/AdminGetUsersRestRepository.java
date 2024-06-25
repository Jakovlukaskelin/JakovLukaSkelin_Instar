package hr.instar.instar.controller.admin;


import hr.instar.instar.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@AllArgsConstructor
public class AdminGetUsersRestRepository {
    private final JdbcTemplate jdbcTemplate;

    private final StoreRepository storeRepository;


    @GetMapping("/usersWithPurchases")
    public List<String> getUsersWithPurchases() {
        String sql = "SELECT DISTINCT username FROM Racun";
        return jdbcTemplate.queryForList(sql, String.class);



    }
}
