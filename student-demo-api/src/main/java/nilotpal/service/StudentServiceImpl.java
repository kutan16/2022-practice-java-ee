package nilotpal.service;

import nilotpal.config.database.DatasourceConfig;
import nilotpal.entity.Client;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class of StudentService
 */
public class StudentServiceImpl implements StudentService {
    private Connection connection = null;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

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
    public List<Client> fetchClients() {
        log.info("Inside fetchClients of StudentServiceImpl");
        if(null != connection) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from clients");
                log.info("Prepared statement created = " + preparedStatement.toString());
                ResultSet rs = preparedStatement.executeQuery();
                log.info("Result set fetched");
                List<Client> listOfClient = new ArrayList<>();
                while (rs.next()) {
                    populateClient(rs, listOfClient);
                }
                if(!listOfClient.isEmpty()) {
                    return listOfClient;
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
    private void populateClient(ResultSet rs, List<Client> listOfClient) throws SQLException {
        Client client = new Client();
        client.setId(rs.getString("client_id"));
        client.setClientSecret(rs.getString("client_secret"));
        client.setRedirectUri(rs.getString("redirect_uri"));
        client.setScope(rs.getString("scope"));
        client.setAuthorizedGrantTypes(rs.getString("authorized_grant_types"));
        listOfClient.add(client);
    }

//    @PreDestroy
//    public void destroy() {
//        try {
//            connection.close();
//        } catch (Exception se) {
//            log.error("connection closing error");
//            log.error(se.toString());
//        }
//    }
}
