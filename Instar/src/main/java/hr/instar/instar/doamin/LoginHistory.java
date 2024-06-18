package hr.instar.instar.doamin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory {

    private Integer idLogin;
    private String username;
    private String vrijemeLogina;
    private String ipAdresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginHistory that = (LoginHistory) o;
        return idLogin.equals(that.idLogin) &&
                username.equals(that.username) &&
                vrijemeLogina.equals(that.vrijemeLogina) &&
                ipAdresa.equals(that.ipAdresa);
    }
    @Override
    public int hashCode() {
        return Objects.hash(idLogin, username, vrijemeLogina, ipAdresa);
    }
}
