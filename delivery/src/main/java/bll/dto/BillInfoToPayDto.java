package bll.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillInfoToPayDto {
    private long billId;
    private long price;
    private long deliveryId;
    private int weight;
    private String addreeserEmail;
    private String localitySandName;
    private String localityGetName;

}
