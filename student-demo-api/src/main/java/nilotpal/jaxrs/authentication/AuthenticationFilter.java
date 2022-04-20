package nilotpal.jaxrs.authentication;

import nilotpal.data.CommonData;
import nilotpal.entity.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String GET_ROLES_ALLOWED = "ADMIN,USER";
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        log.info("Initializing Aythentication Filter");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if(!checkForAuthorization(requestContext)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Not Authorized")
                            .build()
            );
        }
    }

    private boolean checkForAuthorization(ContainerRequestContext requestContext) throws NullPointerException {
        String auth = requestContext.getHeaderString("Authorization");
        String authToken;
        String decodedToken;
        String[] user_credentials;
        if(null != auth && auth.contains("Basic")) {
            String[] token = auth.split(" ");
            authToken = token[1];
            decodedToken = new String(Base64.getDecoder().decode(authToken));
            user_credentials = decodedToken.split(":");
        } else {
            return false;
        }
        List<Credentials> credentials = CommonData.getCredentials();
        return credentials.stream().anyMatch(credentialsMatch -> credentialsMatch.getUsername().equalsIgnoreCase(user_credentials[0])
                && credentialsMatch.getPassword().equalsIgnoreCase(user_credentials[1])
                && (credentialsMatch.getRoles().contains(GET_ROLES_ALLOWED.split(",")[0].toLowerCase())
                || credentialsMatch.getRoles().contains(GET_ROLES_ALLOWED.split(",")[1].toLowerCase())));
    }
}
