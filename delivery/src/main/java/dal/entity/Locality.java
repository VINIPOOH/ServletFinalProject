package dal.entity;

import java.util.List;

public class Locality extends Entity {
    private final List<Way> waysWhereThisLocalityIsSend;
    private final List<Way> waysWhereThisLocalityIsGet;
    private String nameRu;
    private String nameEn;

    public Locality(Long id, String nameRu, String nameEn, List<Way> waysWhereThisLocalityIsSend, List<Way> waysWhereThisLocalityIsGet) {
        super(id);
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.waysWhereThisLocalityIsSend = waysWhereThisLocalityIsSend;
        this.waysWhereThisLocalityIsGet = waysWhereThisLocalityIsGet;
    }

    public static LocalityBuilder builder() {
        return new LocalityBuilder();
    }

    public String getNameRu() {
        return this.nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public List<Way> getWaysWhereThisLocalityIsSend() {
        return this.waysWhereThisLocalityIsSend;
    }

    public List<Way> getWaysWhereThisLocalityIsGet() {
        return this.waysWhereThisLocalityIsGet;
    }


    public static class LocalityBuilder {
        private long id;
        private String nameRu;
        private String nameEn;
        private List<Way> waysWhereThisLocalityIsSend;
        private List<Way> waysWhereThisLocalityIsGet;

        LocalityBuilder() {
        }

        public LocalityBuilder id(long id) {
            this.id = id;
            return this;
        }

        public LocalityBuilder nameRu(String nameRu) {
            this.nameRu = nameRu;
            return this;
        }

        public LocalityBuilder nameEn(String nameEn) {
            this.nameEn = nameEn;
            return this;
        }

        public LocalityBuilder waysWhereThisLocalityIsSend(List<Way> waysWhereThisLocalityIsSend) {
            this.waysWhereThisLocalityIsSend = waysWhereThisLocalityIsSend;
            return this;
        }

        public LocalityBuilder waysWhereThisLocalityIsGet(List<Way> waysWhereThisLocalityIsGet) {
            this.waysWhereThisLocalityIsGet = waysWhereThisLocalityIsGet;
            return this;
        }

        public Locality build() {
            return new Locality(id, nameRu, nameEn, waysWhereThisLocalityIsSend, waysWhereThisLocalityIsGet);
        }
    }
}
