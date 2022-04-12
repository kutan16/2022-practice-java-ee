package nilotpal.config;

import org.glassfish.hk2.api.Immediate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Immediate
public class DbConfig {
    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private static final Logger log = LoggerFactory.getLogger(DbConfig.class);

    public DbConfig() {
    }

    public DbConfig(String username, String password, String url) {
        this.dbPassword = password;
        this.dbUsername = username;
        this.dbUrl = url;
    }

    @PostConstruct
    public void connect() {
        log.info("DB connection processing");
        this.dbUsername = "nilotpal";
        this.dbPassword = "nilotpal";
        this.dbUrl = "jdbc:mysql://localhost:3306/auth-server";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            log.info("DB connection successful");
        } catch (ClassNotFoundException | SQLException ce) {
            log.error(ce.getLocalizedMessage());
        }
    }
}
