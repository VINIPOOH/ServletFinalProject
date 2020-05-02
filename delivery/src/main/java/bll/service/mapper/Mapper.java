package bll.service.mapper;

import dal.entity.Entity;
@FunctionalInterface
public interface Mapper<E, Dto> {
    Dto map(E entity);
}
