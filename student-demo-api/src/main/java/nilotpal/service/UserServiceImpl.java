package nilotpal.service;

import nilotpal.config.database.DatasourceConfig;
import nilotpal.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private Connection connection = null;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Override
    public boolean addUser(User user) {
        log.info("Inside addUser of UserServiceImpl");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into users values (?,?,?,?)");
            preparedStatement.setString(1, user.getUser_id());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRoles());
            preparedStatement.setString(4, user.getScopes());
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            log.error("Error executing sql statement using the connection");
            return false;
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        log.info("Inside deleteUser of UserServiceImpl");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE (`user_id` = ?)");
            preparedStatement.setString(1, userId);
            preparedStatement.execute();
            return true;
        } catch (Exception e) {
            log.error("Error executing sql statement using the connection");
            return false;
        }
    }

    @Override
    public List<User> listAllUsers() {
        log.info("Inside listAllUsers of UserServiceImpl");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users");
            ResultSet rs = preparedStatement.executeQuery();
            List<User> listOfUsers = new ArrayList<>();
            while (rs.next()) {
                poplateUsers(rs, listOfUsers);
            }
            if(!listOfUsers.isEmpty()) {
                return listOfUsers;
            }
            return null;
        } catch (Exception e) {
            log.error("Error executing sql statement using the connection");
            return null;
        }
    }

    private void poplateUsers(ResultSet rs, List<User> listOfUsers) throws SQLException {
        User user = new User();
        user.setUser_id(rs.getString("user_id"));
        user.setPassword(rs.getString("password"));
        user.setRoles(rs.getString("roles"));
        user.setScopes(rs.getString("scopes"));
        listOfUsers.add(user);
    }
}
