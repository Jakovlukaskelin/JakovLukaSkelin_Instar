package hr.instar.instar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryDto {

    private List<BillDetailsDto> purchaseDetailsList;
}
