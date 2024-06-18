package hr.instar.instar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailsDto {

    private  Integer idStavka;
    private  Integer racunID;
    private  Integer proizvodID;
    private  Integer kolicina;
    private  String naziv;
    private  Float cijena;
}
