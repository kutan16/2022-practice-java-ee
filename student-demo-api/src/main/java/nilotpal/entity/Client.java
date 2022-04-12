package nilotpal.entity;

import lombok.Data;

@Data
public class Client {
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String roles;
    private String authorized_grant_types;

    public Client() {
    }

    public Client(String client_id, String client_secret, String redirect_uri, String roles, String authorized_grant_types) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.roles = roles;
        this.authorized_grant_types = authorized_grant_types;
    }
}
