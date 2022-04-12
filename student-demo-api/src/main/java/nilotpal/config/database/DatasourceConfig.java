package nilotpal.config.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jvnet.hk2.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Startup;
import javax.inject.Singleton;
import java.sql.Connection;

/**
 * This class is responsible to instantiating MySqlDataSource object as well as DriverManager and Connection of Mysql
 */
@Singleton
@Startup
@Contract
public class DatasourceConfig {
    private static final String DB_USER = "nilotpal";
    private static final String DB_PASSWORD = "nilotpal";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/auth-server";
    private static final int DB_PORT = 3306;

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfig.class);

    private static MysqlDataSource mysqlDataSource = null;
    private static Connection connection = null;

    /**
     * Instantiates the Connection object
     * @param dbName name of the database(schema)
     * @return instance of Connection
     */
    public static Connection getMysqlDataSource(String dbName) {
        log.info("Inside getMysqlDataSource()");
        if(null == mysqlDataSource) {
            connect(DB_URL, DB_USER, DB_PASSWORD);
        }
        mysqlDataSource.setDatabaseName(dbName);
        log.info("Datasource created : " + dbName);
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            connection = mysqlDataSource.getConnection();
        } catch (Exception se) {
            log.error("Db connection error");
            log.error(se.toString());
        }
        return connection;
    }

    /**
     *Instantiates the MysqlDataSource instance
     * @param DB_URL the full url of the mysql jdbc db
     * @param DB_USER database username
     * @param DB_PASSWORD database password
     */
    private static void connect(String DB_URL, String DB_USER, String DB_PASSWORD) {
        log.info("Inside datasource connect()");
        try {
            mysqlDataSource = new MysqlDataSource();
//            mysqlDataSource.setServerName(DB_URL);
            mysqlDataSource.setUser(DB_USER);
            mysqlDataSource.setPassword(DB_PASSWORD);
            mysqlDataSource.setUrl(DB_URL);
//            mysqlDataSource.setPort(DB_PORT);
        } catch (Exception e) {
            log.info("MysqlDataSource err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
