package dto.mapper;

import javax.servlet.http.HttpServletRequest;


@FunctionalInterface
public interface RequestDtoMapper<T> {
    T mapToDto(HttpServletRequest request);
}
