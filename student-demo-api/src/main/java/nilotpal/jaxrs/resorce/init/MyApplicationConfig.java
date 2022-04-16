package nilotpal.jaxrs.resorce.init;

import nilotpal.jaxrs.resorce.StudentResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class MyApplicationConfig extends ResourceConfig {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationConfig.class);

    public MyApplicationConfig() {
        log.info("Creating my application config");
        register(new MyApplicationBinder());
        register(StudentResource.class);
    }
}