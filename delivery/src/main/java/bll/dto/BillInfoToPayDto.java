package bll.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillInfoToPayDto {
    private long bill_id;
    private long price;
    private long delivery_id;
    private int weight;
    private String addreeserEmail;
    private String localitySandName;
    private String localityGetName;

}
