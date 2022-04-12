package nilotpal.entity;

import lombok.Data;

@Data
public class User {

    private String userId;
    private String password;
    private String roles;
    private String scopes;

    public User() {
    }

    public User(String userId, String password, String roles, String scopes) {
        this.userId = userId;
        this.password = password;
        this.roles = roles;
        this.scopes = scopes;
    }

    public User(User user) {
        this.userId = user.userId;
        this.password = user.password;
        this.roles = user.roles;
        this.scopes = user.scopes;
    }

}
