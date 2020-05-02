package bll.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryInfoToGetDto {
    private String addresserEmail;
    private Long deliveryId;
    private String localitySandName;
    private String localityGetName;
}
