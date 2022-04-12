package nilotpal.service;

import nilotpal.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EmployeeService implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Override
    public List<Client> fetchClients() {
        log.info("Empty Employee Service impl class ");
        return null;
    }
}
