package hr.instar.instar.doamin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class users {

    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        users user = (users) o;
        return username.equals(user.username) &&
                password.equals(user.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
