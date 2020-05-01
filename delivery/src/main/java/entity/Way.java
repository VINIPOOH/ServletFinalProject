package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class Way extends Entity {
    private Locality localitySand;
    private Locality localityGet;
    private List<Delivery> deliveries;
    private int distanceInKilometres;
    private int timeOnWayInDays;
    private int priceForKilometerInCents;
    private List<TariffWeightFactor> wayTariffs;

    public Way(Long id, Locality localitySand, Locality localityGet, List<Delivery> deliveries, int distanceInKilometres, int timeOnWayInDays, int priceForKilometerInCents, List<TariffWeightFactor> wayTariffs) {
        super(id);
        this.localitySand = localitySand;
        this.localityGet = localityGet;
        this.deliveries = deliveries;
        this.distanceInKilometres = distanceInKilometres;
        this.timeOnWayInDays = timeOnWayInDays;
        this.priceForKilometerInCents = priceForKilometerInCents;
        this.wayTariffs = wayTariffs;
    }

    public static WayBuilder builder() {
        return new WayBuilder();
    }

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

    public static class WayBuilder {
        private long id;
        private Locality localitySand;
        private Locality localityGet;
        private List<Delivery> deliveries;
        private int distanceInKilometres;
        private int timeOnWayInDays;
        private int priceForKilometerInCents;
        private List<TariffWeightFactor> wayTariffs;

        WayBuilder() {
        }

        public WayBuilder id(long id) {
            this.id = id;
            return this;
        }

        public WayBuilder localitySand(Locality localitySand) {
            this.localitySand = localitySand;
            return this;
        }

        public WayBuilder localityGet(Locality localityGet) {
            this.localityGet = localityGet;
            return this;
        }

        public WayBuilder deliveries(List<Delivery> deliveries) {
            this.deliveries = deliveries;
            return this;
        }

        public WayBuilder distanceInKilometres(int distanceInKilometres) {
            this.distanceInKilometres = distanceInKilometres;
            return this;
        }

        public WayBuilder timeOnWayInDays(int timeOnWayInDays) {
            this.timeOnWayInDays = timeOnWayInDays;
            return this;
        }

        public WayBuilder priceForKilometerInCents(int priceForKilometerInCents) {
            this.priceForKilometerInCents = priceForKilometerInCents;
            return this;
        }

        public WayBuilder wayTariffs(List<TariffWeightFactor> wayTariffs) {
            this.wayTariffs = wayTariffs;
            return this;
        }

        public Way build() {
            return new Way(id, localitySand, localityGet, deliveries, distanceInKilometres, timeOnWayInDays, priceForKilometerInCents, wayTariffs);
        }

        public String toString() {
            return "Way.WayBuilder(id=" + this.id + ", localitySand=" + this.localitySand + ", localityGet=" + this.localityGet + ", deliveries=" + this.deliveries + ", distanceInKilometres=" + this.distanceInKilometres + ", timeOnWayInDays=" + this.timeOnWayInDays + ", priceForKilometerInCents=" + this.priceForKilometerInCents + ", wayTariffs=" + this.wayTariffs + ")";
        }
    }
}
