package dto.maper;

import dto.RegistrationInfoDto;

import javax.servlet.http.HttpServletRequest;

public class RegistrationRequestDtoMapper implements RequestDtoMapper<RegistrationInfoDto> {

    @Override
    public RegistrationInfoDto mapToDto(HttpServletRequest request) {
        return RegistrationInfoDto.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .passwordRepeat(request.getParameter("passwordRepeat"))
                .build();
    }
}
