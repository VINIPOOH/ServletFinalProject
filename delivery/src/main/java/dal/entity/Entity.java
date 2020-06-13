package dal.entity;

public abstract class Entity {
    private Long id;

    Entity(Long id) {
        this.id = id;
    }

    Entity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
