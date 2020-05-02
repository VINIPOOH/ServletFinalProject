package bll.service.mapper;

import dal.entity.Entity;
@FunctionalInterface
public interface EntityToDtoMapper<E extends Entity, Dto> {
    Dto map(E entity);
}
