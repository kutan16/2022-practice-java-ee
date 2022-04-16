package nilotpal.jaxrs.resorce.init;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class MyApplicationFeature implements Feature {

    @Inject
    public MyApplicationFeature(ServiceLocator serviceLocator) {
        ServiceLocatorUtilities.enableImmediateScope(serviceLocator);
    }

    @Override
    public boolean configure(FeatureContext context) {
        return true;
    }
}
