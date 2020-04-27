package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Locality extends Entity {
    private String nameRu;
    private String nameEn;
    private List<Way> waysWhereThisLocalityIsSend;
    private List<Way> waysWhereThisLocalityIsGet;

    public Locality(String nameRu, String nameEn) {
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }
}
