package nilotpal.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;

/**
 * This class is responsible to instantiating MySqlDataSource object as well as DriverManager and Connection of Mysql
 */
public class DatasourceConfig {
    private String DB_USER;
    private String DB_PASSWORD;
    private String DB_URL;

    private static final Logger log = LoggerFactory.getLogger(DatasourceConfig.class);

    private static MysqlDataSource mysqlDataSource = null;
    private static Connection connection = null;

    @PostConstruct
    public void loadPropertiesForDb() {
        PropertyConfig properties = PropertyConfig.getInstance();
        this.DB_USER = properties.get("db.username");
        this.DB_PASSWORD = properties.get("db.password");
        this.DB_URL = properties.get("db.url");
        log.info("DatasourceConfig loaded with properties 'url = {} ', 'username = {} ','password = {} '", DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Instantiates the Connection object
     *
     * @param dbName name of the database(schema)
     * @return instance of Connection
     */
    public Connection getMysqlDataSource(String dbName) {
        log.info("Inside getMysqlDataSource()");
        if(null == mysqlDataSource) {
            connect(DB_URL, DB_USER, DB_PASSWORD);
        }
        mysqlDataSource.setDatabaseName(dbName);
        log.info("Datasource created : " + dbName);
        try {
            if(null == connection) {
                log.info("doing mysqlDataSource.getConnection()");
                connection = mysqlDataSource.getConnection();
            } else {
                log.info("returning already build connection");
                return connection;
            }
        } catch (Exception se) {
            log.error("Db connection error");
            log.error(se.toString());
        }
        return connection;
    }

    /**
     * Instantiates the MysqlDataSource instance
     *
     * @param DB_URL      the full url of the mysql jdbc db
     * @param DB_USER     database username
     * @param DB_PASSWORD database password
     */
    private void connect(String DB_URL, String DB_USER, String DB_PASSWORD) {
        log.info("Inside datasource connect()");
        try {
            mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser(DB_USER);
            mysqlDataSource.setPassword(DB_PASSWORD);
            mysqlDataSource.setUrl(DB_URL);
        } catch (Exception e) {
            log.info("MysqlDataSource err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
