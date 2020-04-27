package dto;

import entity.Entity;
import entity.Way;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocaliseLocalityDto {
        private Long id;
        private String name;

}
