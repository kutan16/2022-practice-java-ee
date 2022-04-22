package nilotpal.jaxrs.init;

import nilotpal.config.DatasourceConfig;
import nilotpal.config.PropertyConfig;
import nilotpal.service.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 *Custom Binder to bind classes as well as implemented interfaces to be available for injection
 */
public class MyApplicationBinder extends AbstractBinder {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationBinder.class);

    /**
     * Custom overridden method to bind the classes and interface implementation
     */
    @Override
    protected void configure() {
        log.info("Creating my application binder");
        bindAsContract(DatasourceConfig.class).in(Singleton.class);
        bindAsContract(CommonDataService.class);
        bind(StudentService.class).named("studentService").to(ServiceInterface.class).in(Singleton.class);
        bind(EmployeeService.class).named("employeeService").to(ServiceInterface.class).in(Singleton.class);
        bind(UserServiceImpl.class).named("userService").to(UserService.class).in(Singleton.class);
        bindAsContract(ServiceInterface.class);
        bindAsContract(UserService.class);
        bind(PropertyConfig.class).to(DatasourceConfig.class).in(Singleton.class);
    }
}