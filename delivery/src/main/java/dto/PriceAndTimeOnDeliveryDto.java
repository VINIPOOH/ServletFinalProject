package dto;

public class PriceAndTimeOnDeliveryDto {
    private long costInCents;
    private int timeOnWayInHours;

    PriceAndTimeOnDeliveryDto(long costInCents, int timeOnWayInHours) {
        this.costInCents = costInCents;
        this.timeOnWayInHours = timeOnWayInHours;
    }

    public static PriceAndTimeOnDeliveryDtoBuilder builder() {
        return new PriceAndTimeOnDeliveryDtoBuilder();
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }

    public int getTimeOnWayInHours() {
        return this.timeOnWayInHours;
    }

    public static class PriceAndTimeOnDeliveryDtoBuilder {
        private long costInCents;
        private int timeOnWayInHours;

        PriceAndTimeOnDeliveryDtoBuilder() {
        }

        public PriceAndTimeOnDeliveryDto.PriceAndTimeOnDeliveryDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public PriceAndTimeOnDeliveryDto.PriceAndTimeOnDeliveryDtoBuilder timeOnWayInHours(int timeOnWayInHours) {
            this.timeOnWayInHours = timeOnWayInHours;
            return this;
        }

        public PriceAndTimeOnDeliveryDto build() {
            return new PriceAndTimeOnDeliveryDto(costInCents, timeOnWayInHours);
        }
    }
}
