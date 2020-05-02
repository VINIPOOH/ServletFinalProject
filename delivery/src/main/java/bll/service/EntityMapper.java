package bll.service;

public interface EntityMapper<T, E> {
    T mapToEntity(E e);
}

