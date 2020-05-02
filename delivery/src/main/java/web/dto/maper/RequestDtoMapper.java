package web.dto.maper;

import javax.servlet.http.HttpServletRequest;


@FunctionalInterface
public interface RequestDtoMapper<T> {
    T mapToDto(HttpServletRequest request);
}
