package service.mapper;

import entity.Entity;
@FunctionalInterface
public interface EntityToDtoMapper<E extends Entity, Dto> {
    Dto map(E entity);
}
