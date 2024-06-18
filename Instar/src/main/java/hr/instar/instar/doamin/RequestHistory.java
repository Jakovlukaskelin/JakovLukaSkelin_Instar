package hr.instar.instar.doamin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHistory {

     private Integer idRequestHistory;
        private String username;
        private String vrijemeRequesta;
        private String request;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequestHistory that = (RequestHistory) o;
            return idRequestHistory.equals(that.idRequestHistory) &&
                    username.equals(that.username) &&
                    vrijemeRequesta.equals(that.vrijemeRequesta) &&
                    request.equals(that.request);
        }

@Override
public int hashCode() {
    return Objects.hash(idRequestHistory, username, vrijemeRequesta, request);

}
}
