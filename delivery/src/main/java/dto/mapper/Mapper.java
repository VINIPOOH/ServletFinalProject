package dto.mapper;

@FunctionalInterface
public interface Mapper<E, Dto> {
    Dto map(E entity);
}
