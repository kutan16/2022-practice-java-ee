package nilotpal.service;

import nilotpal.entity.Client;

import java.util.List;

/**
 * Interface having methods for manipulating data from database
 */
public interface StudentService {
    /**
     * Fetches a list of Client
     * @return A list of Client
     */
    public List<Client> fetchClients();
}
