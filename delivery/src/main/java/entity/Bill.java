package entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDate;

@Data
public class Bill {

    private Long id;
    Delivery delivery;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;


}
