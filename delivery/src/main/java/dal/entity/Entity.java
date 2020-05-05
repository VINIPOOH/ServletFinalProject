package dal.entity;

public abstract class Entity {
    private Long id;

    public Entity(Long id) {
        this.id = id;
    }

    public Entity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
