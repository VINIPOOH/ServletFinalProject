package entity;

import lombok.Data;

@Data
public abstract class Entity {
    private Long id;

    public Entity(Long id) {
        this.id = id;
    }
}
