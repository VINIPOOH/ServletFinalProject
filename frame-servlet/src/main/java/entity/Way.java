package entity;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Way extends Entity {
    private Locality localitySand;
    private Locality localityGet;
    private List<Delivery> deliveries;
    private int distanceInKilometres;
    private int timeOnWayInDays;
    private int priceForKilometerInCents;
    private List<TariffWeightFactor> wayTariffs;

    @Override
    public String toString() {
        return "Way{"; //+
//                "id=" + id +
//                ", localitySand=" + localitySand.getNameRu() +
//                ", localityGet=" + localityGet.getNameRu() +
//                ", distanceInKilometres=" + distanceInKilometres +
//                ", timeOnWayInHours=" + timeOnWayInHours +
//                ", priceForKilometerInCents=" + priceForKilometerInCents +
//                ", wayTariffs=" + wayTariffs +
//                '}';
    }
}
