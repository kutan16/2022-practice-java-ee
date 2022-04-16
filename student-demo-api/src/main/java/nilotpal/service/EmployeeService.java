package nilotpal.service;

import nilotpal.config.database.DatasourceConfig;
import nilotpal.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class EmployeeService implements ServiceInterface {
    private Connection connection = null;
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Instantiates the connection instance
     */
    @PostConstruct
    public void initiate() {
        try {
            log.info("Inside post construct of EmployeeService");
            connection = DatasourceConfig.getMysqlDataSource("auth-server");
        } catch (Exception se) {
            log.error("Db connection error");
            log.error(se.toString());
        }
    }

    @Override
    public Client fetchClients() {
        if(null != connection) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from clients where client_secret = 'asd'");
                log.info("Prepared statement created = " + preparedStatement.toString());
                ResultSet rs = preparedStatement.executeQuery();
                log.info("Result set fetched");
                Client client = new Client();
                client.setClients(Collections.singletonList(populateClient(rs)));
                return client;
            } catch (Exception ce) {
                log.error("Error generating sql statement using the connection");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param rs ResultSet
     * @return A client
     * @throws SQLException Exception might be caused due to null result set or incorrect column names or any other scenarios
     */
    private Client.Clients populateClient(ResultSet rs) throws SQLException {
        Client.Clients client = new Client.Clients();
        while (rs.next()) {
            client.setClient_id(rs.getString("client_id"));
            client.setClient_secret(rs.getString("client_secret"));
            client.setRedirect_uri(rs.getString("redirect_uri"));
            client.setRoles(rs.getString("scope"));
            client.setAuthorized_grant_types(rs.getString("authorized_grant_types"));
        }
        return client;
    }
}
