package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegistrationInfoDto {
    private String username;
    private String password;
    private String passwordRepeat;
}