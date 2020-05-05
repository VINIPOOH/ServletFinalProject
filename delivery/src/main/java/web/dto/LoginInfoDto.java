package web.dto;

public class LoginInfoDto {
    private String username;
    private String password;

    public LoginInfoDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static LoginInfoDtoBuilder builder() {
        return new LoginInfoDtoBuilder();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static class LoginInfoDtoBuilder {
        private String username;
        private String password;

        LoginInfoDtoBuilder() {
        }

        public LoginInfoDto.LoginInfoDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginInfoDto.LoginInfoDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public LoginInfoDto build() {
            return new LoginInfoDto(username, password);
        }

    }
}