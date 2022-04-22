package nilotpal.service;

import nilotpal.config.DatasourceConfig;
import nilotpal.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class of StudentService
 */
public class StudentService implements ServiceInterface {
    private Connection connection = null;
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    /**
     * Instantiates the connection instance
     */
    @PostConstruct
    public void initiate() {
        try {
            log.info("Inside post construct of StudentServiceImpl");
            connection = DatasourceConfig.getMysqlDataSource("auth-server");
        } catch (Exception se) {
            log.error("Db connection error");
            log.error(se.toString());
        }
    }

    /**
     * Fetches a list of Client
     *
     * @return A list of Client
     */
    @Override
    public Client fetchClients() {
        log.info("Inside fetchClients of StudentServiceImpl");
        if(null != connection) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from clients");
                log.info("Prepared statement created = " + preparedStatement.toString());
                ResultSet rs = preparedStatement.executeQuery();
                log.info("Result set fetched");
                Client rootClient = new Client();
                List<Client.Clients> listOfClient = new ArrayList<>();
                while (rs.next()) {
                    populateClient(rs, listOfClient);
                }
                if(!listOfClient.isEmpty()) {
                    rootClient.setClients(listOfClient);
                    return rootClient;
                }
                return null;
            } catch (Exception ce) {
                log.error("Error generating sql statement using the connection");
                return null;
            }
        }
        log.info("Connection is null in StudentServiceImpl");
        return null;
    }

    /**
     * Populates the Client object by using a ResultSet
     *
     * @param rs           ResultSet
     * @param listOfClient A list of Client
     * @throws SQLException Exception might be caused due to null result set or incorrect column names or any other scenarios
     */
    private void populateClient(ResultSet rs, List<Client.Clients> listOfClient) throws SQLException {
        Client.Clients client = new Client.Clients();
        client.setClient_id(rs.getString("client_id"));
        client.setClient_secret(rs.getString("client_secret"));
        client.setRedirect_uri(rs.getString("redirect_uri"));
        client.setRoles(rs.getString("scope"));
        client.setAuthorized_grant_types(rs.getString("authorized_grant_types"));
        listOfClient.add(client);
    }
}
