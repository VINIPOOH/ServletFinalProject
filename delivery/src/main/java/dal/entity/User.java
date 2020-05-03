package dal.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString

public class User extends Entity {
    private String email;
    private RoleType roleType;
    private String password;
    private Long userMoneyInCents;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private List<Delivery> waysWhereThisUserIsSend;
    private List<Delivery> waysWhereThisUserIsGet;

    public User(Long id, String email, RoleType roleType, String password, Long userMoneyInCents, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, List<Delivery> waysWhereThisUserIsSend, List<Delivery> waysWhereThisUserIsGet) {
        super(id);
        this.email = email;
        this.roleType = roleType;
        this.password = password;
        this.userMoneyInCents = userMoneyInCents;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.waysWhereThisUserIsSend = waysWhereThisUserIsSend;
        this.waysWhereThisUserIsGet = waysWhereThisUserIsGet;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    public static class UserBuilder {
        private long id;
        private String email;
        private RoleType roleType;
        private String password;
        private Long userMoneyInCents;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;
        private List<Delivery> waysWhereThisUserIsSend;
        private List<Delivery> waysWhereThisUserIsGet;

        UserBuilder() {
        }

        public UserBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder roleType(RoleType roleType) {
            this.roleType = roleType;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder userMoneyInCents(Long userMoneyInCents) {
            this.userMoneyInCents = userMoneyInCents;
            return this;
        }

        public UserBuilder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public UserBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public UserBuilder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder waysWhereThisUserIsSend(List<Delivery> waysWhereThisUserIsSend) {
            this.waysWhereThisUserIsSend = waysWhereThisUserIsSend;
            return this;
        }

        public UserBuilder waysWhereThisUserIsGet(List<Delivery> waysWhereThisUserIsGet) {
            this.waysWhereThisUserIsGet = waysWhereThisUserIsGet;
            return this;
        }

        public User build() {
            User user = new User(id, email, roleType, password, userMoneyInCents, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, waysWhereThisUserIsSend, waysWhereThisUserIsGet);

            return user;
        }

        public String toString() {
            return "User.UserBuilder(email=" + this.email + ", roleType=" + this.roleType + ", password=" + this.password + ", userMoneyInCents=" + this.userMoneyInCents + ", accountNonExpired=" + this.accountNonExpired + ", accountNonLocked=" + this.accountNonLocked + ", credentialsNonExpired=" + this.credentialsNonExpired + ", enabled=" + this.enabled + ", waysWhereThisUserIsSend=" + this.waysWhereThisUserIsSend + ", waysWhereThisUserIsGet=" + this.waysWhereThisUserIsGet + ")";
        }
    }
}
