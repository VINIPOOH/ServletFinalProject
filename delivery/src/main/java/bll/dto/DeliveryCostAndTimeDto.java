package bll.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCostAndTimeDto {
    private long costInCents;
    private int timeOnWayInHours;
}
