package dal.entity;

import java.time.LocalDate;

public class Bill extends Entity {

    Delivery delivery;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;

    public Bill(Long id, Delivery delivery, Boolean isDeliveryPaid, long costInCents, LocalDate dateOfPay) {
        super(id);
        this.delivery = delivery;
        this.isDeliveryPaid = isDeliveryPaid;
        this.costInCents = costInCents;
        this.dateOfPay = dateOfPay;
    }

    public Bill() {
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public Boolean getIsDeliveryPaid() {
        return this.isDeliveryPaid;
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public LocalDate getDateOfPay() {
        return this.dateOfPay;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }


    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }

    public static class BillBuilder {
        private long id;
        private Delivery delivery;
        private Boolean isDeliveryPaid;
        private long costInCents;
        private LocalDate dateOfPay;

        BillBuilder() {
        }

        public BillBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BillBuilder delivery(Delivery delivery) {
            this.delivery = delivery;
            return this;
        }

        public BillBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public BillBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public BillBuilder dateOfPay(LocalDate dateOfPay) {
            this.dateOfPay = dateOfPay;
            return this;
        }

        public Bill build() {
            return new Bill(id, delivery, isDeliveryPaid, costInCents, dateOfPay);
        }
    }
}
