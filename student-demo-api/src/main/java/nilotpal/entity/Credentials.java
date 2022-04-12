package nilotpal.entity;

import lombok.Data;

import java.util.Set;

@Data
public class Credentials {
    private String username;
    private String password;
    private Set<String> roles;

    public Credentials() {
    }

    public Credentials(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Credentials(Credentials credentials) {
        this.username = credentials.username;
        this.password = credentials.password;
        this.roles = credentials.roles;
    }
}
