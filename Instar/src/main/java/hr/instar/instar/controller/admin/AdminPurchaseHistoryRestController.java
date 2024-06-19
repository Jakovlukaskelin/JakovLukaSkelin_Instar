package hr.instar.instar.controller.admin;



import hr.instar.instar.dto.PurchaseHistoryDto;

import hr.instar.instar.repository.StoreRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin/purchases")
@AllArgsConstructor
public class AdminPurchaseHistoryRestController {

    private final StoreRepository storeRepository;

    @GetMapping("")
    public List<PurchaseHistoryDto> getCompletePurchaseHistory(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "endDate", required = false) String endDateStr) {
        List<PurchaseHistoryDto> completePurchaseHistory = storeRepository.getCompletePurchaseHistory();
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        DateTimeFormatter parameterFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        List<PurchaseHistoryDto> filteredPurchaseHistory = completePurchaseHistory.stream()
                .filter(dto -> dto.getPurchaseDetailsList().stream().anyMatch(racunDetails -> {
                    if (username != null && !username.isBlank()) {
                        return username.equals(racunDetails.getRacun().getUsername());
                    }
                    return true;
                }))
                .map(dto -> new PurchaseHistoryDto(
                        dto.getPurchaseDetailsList().stream()
                                .filter(racunDetails -> {
                                    if (startDateStr != null && !startDateStr.isBlank()) {
                                        LocalDateTime startDate = LocalDateTime.parse(startDateStr, parameterFormatter);
                                        LocalDateTime invoiceDate = LocalDateTime.parse(racunDetails.getRacun().getVrijemeKupovine(), dataFormatter);
                                        return !invoiceDate.isBefore(startDate);
                                    }
                                    if (endDateStr != null && !endDateStr.isBlank()) {
                                        LocalDateTime endDate = LocalDateTime.parse(endDateStr, parameterFormatter);
                                        LocalDateTime invoiceDate = LocalDateTime.parse(racunDetails.getRacun().getVrijemeKupovine(), dataFormatter);
                                        return !invoiceDate.isAfter(endDate);
                                    }
                                    return true;
                                })
                                .collect(Collectors.toList())
                ))
                .filter(dto -> !dto.getPurchaseDetailsList().isEmpty())
                .collect(Collectors.toList());



        return filteredPurchaseHistory;
    }
}
