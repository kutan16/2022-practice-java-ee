package nilotpal.config.injection;

import nilotpal.config.DbConfig;
import nilotpal.jaxrs.resorce.StudentResource;
import nilotpal.service.CommonDataService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApplicationBinder extends AbstractBinder {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationBinder.class);

    @Override
    protected void configure() {
        log.info("Creating my application binder");
        bindAsContract(DbConfig.class);
        bindAsContract(CommonDataService.class);
    }
}