package nilotpal.config.injection;

import nilotpal.config.database.DatasourceConfig;
import nilotpal.service.CommonDataService;
import nilotpal.service.EmployeeService;
import nilotpal.service.StudentService;
import nilotpal.service.StudentServiceImpl;
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
        bind(StudentServiceImpl.class).named("a").to(StudentService.class).in(Singleton.class);
        bind(EmployeeService.class).named("b").to(StudentService.class).in(Singleton.class);
        bindAsContract(StudentService.class);
    }
}