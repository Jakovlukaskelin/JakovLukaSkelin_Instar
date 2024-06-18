package hr.instar.instar.doamin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proizvod {

    private Integer idProizvod;
    private Integer kategorijaID;
    private String naziv;
    private Float cijena;
    private Integer dostupnaKolicina;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proizvod proizvod = (Proizvod) o;
        return idProizvod.equals(proizvod.idProizvod) &&
                kategorijaID.equals(proizvod.kategorijaID) &&
                naziv.equals(proizvod.naziv) &&
                cijena.equals(proizvod.cijena) &&
                dostupnaKolicina.equals(proizvod.dostupnaKolicina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProizvod, kategorijaID, naziv, cijena, dostupnaKolicina);
    }
}
