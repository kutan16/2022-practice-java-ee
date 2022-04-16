package nilotpal.jaxrs.resorce.init;

import nilotpal.config.database.DatasourceConfig;
import nilotpal.service.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class MyApplicationBinder extends AbstractBinder {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationBinder.class);

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
    }
}