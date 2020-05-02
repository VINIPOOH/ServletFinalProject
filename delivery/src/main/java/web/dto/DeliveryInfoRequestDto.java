package web.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DeliveryInfoRequestDto {


    private int deliveryWeight;

    private long localitySandID;

    private long localityGetID;
}
