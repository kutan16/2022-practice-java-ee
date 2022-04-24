package nilotpal.jaxrs.init;

import nilotpal.certificate.CertificateService;
import nilotpal.certificate.SSLSocketProvider;
import nilotpal.certificate.StoreTrustedCertificates;
import nilotpal.config.DatasourceConfig;
import nilotpal.config.PropertyConfig;
import nilotpal.service.*;
import nilotpal.util.DBUtil;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.net.ssl.SSLSocket;

/**
 * Custom Binder to bind classes as well as implemented interfaces to be available for injection
 */
public class MyApplicationBinder extends AbstractBinder {
    private final static Logger log = LoggerFactory.getLogger(MyApplicationBinder.class);

    /**
     * Custom overridden method to bind the classes and interface implementation
     */
    @Override
    protected void configure() {
        log.info("Creating my application binder");
        bindAsContract(CommonDataService.class);
        bind(StudentService.class).named("studentService").to(ServiceInterface.class).in(Singleton.class);
        bind(EmployeeService.class).named("employeeService").to(ServiceInterface.class).in(Singleton.class);
        bind(UserServiceImpl.class).named("userService").to(UserService.class).in(Singleton.class);
        bindAsContract(ServiceInterface.class);
        bindAsContract(UserService.class);
        bindAsContract(PropertyConfig.class).in(Singleton.class);
        bindAsContract(DatasourceConfig.class).in(Singleton.class);
        bindAsContract(DBUtil.class).in(Singleton.class);
        bindAsContract(UserServiceImpl.class).in(Singleton.class);
        bindAsContract(StudentService.class).in(Singleton.class);
        bindAsContract(EmployeeService.class).in(Singleton.class);
        bindAsContract(CertificateService.class);
        bindFactory(SSLSocketProvider.class, Singleton.class).to(SSLSocket.class).in(PerLookup.class);
        bindAsContract(StoreTrustedCertificates.class).in(Singleton.class);
    }
}