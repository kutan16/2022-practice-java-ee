package nilotpal.jaxrs.init;

import nilotpal.jaxrs.authentication.AuthenticationFilter;
import nilotpal.jaxrs.resource.StudentResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.ws.rs.ApplicationPath;

/**
 * Custom Resource configuration class to register the required resource classes
 */
@ApplicationPath("/api")
public class MyApplicationConfig extends ResourceConfig {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationConfig.class);

    public MyApplicationConfig() {
        log.info("Creating my application config");
        register(new MyApplicationBinder());
        register(StudentResource.class);
        register(AuthenticationFilter.class);
    }
}