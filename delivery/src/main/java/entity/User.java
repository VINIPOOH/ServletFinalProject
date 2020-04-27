package entity;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


}
