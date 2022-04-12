package nilotpal.entity;

import lombok.Data;

@Data
public class Client {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
    private String authorizedGrantTypes;

    public Client() {
    }

    public Client(String clientId, String clientSecret, String redirectUri, String scope, String authorizedGrantTypes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
    }
    public Client(Client client) {
        this.clientId=client.clientId;
        this.clientSecret=client.clientSecret;
        this.redirectUri=client.redirectUri;
        this.scope=client.scope;
        this.authorizedGrantTypes=client.authorizedGrantTypes;
    }
}