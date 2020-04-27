package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery extends Entity {
    private LocalDate arrivalDate;
    private Way way;
    private User addressee;
    private User addresser;
    private Boolean isPackageReceived;
    private Boolean isDeliveryPaid;
    private int weight;
    private long costInCents;
}
