package bll.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceAndTimeOnDeliveryDto {
    private long costInCents;
    private int timeOnWayInHours;
}
