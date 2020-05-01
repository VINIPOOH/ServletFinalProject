package dto;

import entity.Delivery;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BillDto {
    private long id;
    long deliveryId;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;
}
