package dal.entity;

import lombok.Data;

import java.util.List;

@Data
public class TariffWeightFactor extends Entity {
    private int minWeightRange;
    private int maxWeightRange;
    private int overPayOnKilometer;

    public TariffWeightFactor(Long id, int minWeightRange, int maxWeightRange, int overPayOnKilometer, List<Way> waysWhereUsed) {
        super(id);
        this.minWeightRange = minWeightRange;
        this.maxWeightRange = maxWeightRange;
        this.overPayOnKilometer = overPayOnKilometer;
        this.waysWhereUsed = waysWhereUsed;
    }

    private List<Way> waysWhereUsed;

    public static TariffWeightFactorBuilder builder() {
        return new TariffWeightFactorBuilder();
    }


    public static class TariffWeightFactorBuilder {
        private long id;
        private int minWeightRange;
        private int maxWeightRange;
        private int overPayOnKilometer;
        private List<Way> waysWhereUsed;

        TariffWeightFactorBuilder() {
        }

        public TariffWeightFactorBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TariffWeightFactorBuilder minWeightRange(int minWeightRange) {
            this.minWeightRange = minWeightRange;
            return this;
        }

        public TariffWeightFactorBuilder maxWeightRange(int maxWeightRange) {
            this.maxWeightRange = maxWeightRange;
            return this;
        }

        public TariffWeightFactorBuilder overPayOnKilometer(int overPayOnKilometer) {
            this.overPayOnKilometer = overPayOnKilometer;
            return this;
        }

        public TariffWeightFactorBuilder waysWhereUsed(List<Way> waysWhereUsed) {
            this.waysWhereUsed = waysWhereUsed;
            return this;
        }

        public TariffWeightFactor build() {
            return new TariffWeightFactor(id, minWeightRange, maxWeightRange, overPayOnKilometer, waysWhereUsed);
        }

        public String toString() {
            return "TariffWeightFactor.TariffWeightFactorBuilder(id=" + this.id + ", minWeightRange=" + this.minWeightRange + ", maxWeightRange=" + this.maxWeightRange + ", overPayOnKilometer=" + this.overPayOnKilometer + ", waysWhereUsed=" + this.waysWhereUsed + ")";
        }
    }
}
