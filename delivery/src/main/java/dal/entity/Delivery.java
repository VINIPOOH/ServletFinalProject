package dal.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Delivery extends Entity {
    public Delivery(Long id, LocalDate arrivalDate, Way way, User addressee, User addresser, Boolean isPackageReceived, Boolean isDeliveryPaid, int weight, long costInCents) {
        super(id);

        this.arrivalDate = arrivalDate;
        this.way = way;
        this.addressee = addressee;
        this.addresser = addresser;
        this.isPackageReceived = isPackageReceived;
        this.isDeliveryPaid = isDeliveryPaid;
        this.weight = weight;
        this.costInCents = costInCents;
    }

    private LocalDate arrivalDate;
    private Way way;
    private User addressee;
    private User addresser;
    private Boolean isPackageReceived;
    private Boolean isDeliveryPaid;
    private int weight;
    private long costInCents;

    public static DeliveryBuilder builder() {
        return new DeliveryBuilder();
    }

    public static class DeliveryBuilder {
        private long id;
        private LocalDate arrivalDate;
        private Way way;
        private User addressee;
        private User addresser;
        private Boolean isPackageReceived;
        private Boolean isDeliveryPaid;
        private int weight;
        private long costInCents;

        DeliveryBuilder() {
        }

        public DeliveryBuilder id(long id) {
            this.id = id;
            return this;
        }

        public DeliveryBuilder arrivalDate(LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
            return this;
        }

        public DeliveryBuilder way(Way way) {
            this.way = way;
            return this;
        }

        public DeliveryBuilder addressee(User addressee) {
            this.addressee = addressee;
            return this;
        }

        public DeliveryBuilder addresser(User addresser) {
            this.addresser = addresser;
            return this;
        }

        public DeliveryBuilder isPackageReceived(Boolean isPackageReceived) {
            this.isPackageReceived = isPackageReceived;
            return this;
        }

        public DeliveryBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public DeliveryBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public DeliveryBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public Delivery build() {
            return new Delivery(id, arrivalDate, way, addressee, addresser, isPackageReceived, isDeliveryPaid, weight, costInCents);
        }

        public String toString() {
            return "Delivery.DeliveryBuilder(id=" + this.id + ", arrivalDate=" + this.arrivalDate + ", way=" + this.way + ", addressee=" + this.addressee + ", addresser=" + this.addresser + ", isPackageReceived=" + this.isPackageReceived + ", isDeliveryPaid=" + this.isDeliveryPaid + ", weight=" + this.weight + ", costInCents=" + this.costInCents + ")";
        }
    }
}
