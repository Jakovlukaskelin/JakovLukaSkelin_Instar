package hr.instar.instar.repository;


import hr.instar.instar.doamin.users;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryJbdc implements UserRepository{

    private final JdbcTemplate jdbcTemplate;
    @Override
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, username, password, true);
        String roleSql = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
        jdbcTemplate.update(roleSql, username, "ROLE_USER");
    }

    @Override
    public users findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, userRowMapper());
    }

    private RowMapper<users> userRowMapper() {
        return (rs, rowNum) -> {
            users user = new users();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        };
    }
}

