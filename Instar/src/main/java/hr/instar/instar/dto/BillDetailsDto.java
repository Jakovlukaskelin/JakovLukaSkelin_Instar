package hr.instar.instar.dto;


import hr.instar.instar.doamin.Racun;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class BillDetailsDto {

    private Racun racun;
    private List<ItemDetailsDto> itemDetails;


}
