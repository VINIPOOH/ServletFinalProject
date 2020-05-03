package bll.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BillDto {
    long deliveryId;
    private long id;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;
}
