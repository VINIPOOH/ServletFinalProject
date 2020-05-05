package web.dto;

public class RegistrationInfoDto {
    private String username;
    private String password;
    private String passwordRepeat;

    public RegistrationInfoDto(String username, String password, String passwordRepeat) {
        this.username = username;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
    }

    public static RegistrationInfoDtoBuilder builder() {
        return new RegistrationInfoDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordRepeat() {
        return this.passwordRepeat;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public static class RegistrationInfoDtoBuilder {
        private String username;
        private String password;
        private String passwordRepeat;

        RegistrationInfoDtoBuilder() {
        }

        public RegistrationInfoDto.RegistrationInfoDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public RegistrationInfoDto.RegistrationInfoDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegistrationInfoDto.RegistrationInfoDtoBuilder passwordRepeat(String passwordRepeat) {
            this.passwordRepeat = passwordRepeat;
            return this;
        }

        public RegistrationInfoDto build() {
            return new RegistrationInfoDto(username, password, passwordRepeat);
        }

    }
}