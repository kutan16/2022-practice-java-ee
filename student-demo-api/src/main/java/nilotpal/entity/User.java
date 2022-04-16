package nilotpal.entity;

import lombok.Data;

@Data
public class User {

    private String user_id;
    private String password;
    private String roles;
    private String scopes;

    public User() {
    }

    public User(String user_id, String password, String roles, String scopes) {
        this.user_id = user_id;
        this.password = password;
        this.roles = roles;
        this.scopes = scopes;
    }

    public User(User user) {
        this.user_id = user.user_id;
        this.password = user.password;
        this.roles = user.roles;
        this.scopes = user.scopes;
    }

}
