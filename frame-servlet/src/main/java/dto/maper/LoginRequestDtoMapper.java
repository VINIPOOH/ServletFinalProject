package dto.maper;

import dto.LoginInfoDto;

import javax.servlet.http.HttpServletRequest;

public class LoginRequestDtoMapper implements RequestDtoMapper<LoginInfoDto> {

    @Override
    public LoginInfoDto mapToDto(HttpServletRequest request) {
        return LoginInfoDto.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .build();
    }
}
