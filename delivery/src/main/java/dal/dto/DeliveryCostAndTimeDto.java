package dal.dto;

public class DeliveryCostAndTimeDto {
    private long costInCents;
    private int timeOnWayInHours;

    DeliveryCostAndTimeDto(long costInCents, int timeOnWayInHours) {
        this.costInCents = costInCents;
        this.timeOnWayInHours = timeOnWayInHours;
    }

    public static DeliveryCostAndTimeDtoBuilder builder() {
        return new DeliveryCostAndTimeDtoBuilder();
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public int getTimeOnWayInHours() {
        return this.timeOnWayInHours;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }



    public static class DeliveryCostAndTimeDtoBuilder {
        private long costInCents;
        private int timeOnWayInHours;

        DeliveryCostAndTimeDtoBuilder() {
        }

        public DeliveryCostAndTimeDto.DeliveryCostAndTimeDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public DeliveryCostAndTimeDto.DeliveryCostAndTimeDtoBuilder timeOnWayInHours(int timeOnWayInHours) {
            this.timeOnWayInHours = timeOnWayInHours;
            return this;
        }

        public DeliveryCostAndTimeDto build() {
            return new DeliveryCostAndTimeDto(costInCents, timeOnWayInHours);
        }

    }
}
