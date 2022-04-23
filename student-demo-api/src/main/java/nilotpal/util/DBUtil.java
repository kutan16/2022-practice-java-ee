package nilotpal.util;

import nilotpal.config.DatasourceConfig;

import javax.inject.Inject;
import java.sql.Connection;

/**
 * Utility class for getting the db connection
 */
public class DBUtil {

    public final DatasourceConfig datasourceConfig;

    @Inject
    public DBUtil(DatasourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    /**
     * Invoking this method return the connection object for the db
     *
     * @param dbName Name of Database schema
     * @return connection instance of the database
     */
    public Connection getConnection(String dbName) {
        return datasourceConfig.getMysqlDataSource(dbName);
    }
}
