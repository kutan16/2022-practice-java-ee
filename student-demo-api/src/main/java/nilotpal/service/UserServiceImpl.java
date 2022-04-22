package nilotpal.service;

import nilotpal.entity.User;
import nilotpal.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class of UserService
 */
public class UserServiceImpl implements UserService {
    private Connection connection = null;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final DBUtil dbUtil;

    @Inject
    public UserServiceImpl(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Initializing connection object for mysql database if not already present
     */
    @PostConstruct
    public void initiate() {
        try {
            log.info("Inside post construct of StudentServiceImpl");
            connection = dbUtil.getConnection("auth-server");
        } catch (Exception se) {
            log.error("Db connection error");
            log.error(se.toString());
        }
    }

    /**
     * @param user A User entity
     * @return true or false depending on the query executed or not
     */
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

    /**
     * @param userId The user_id of the User
     * @return true or false depending on the query executed or not
     */
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

    /**
     * @return A list of Users
     */
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

    /**
     * @param userId The userId of the User
     * @return A User
     */
    @Override
    public User getUser(String userId) {
        log.info("Inside getUser of UserServiceImpl");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users WHERE (`user_id` = ?)");
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            User user = new User();
            while (rs.next()) {
                user.setUser_id(rs.getString("user_id"));
                user.setPassword(rs.getString("password"));
                user.setRoles(rs.getString("roles"));
                user.setScopes(rs.getString("scopes"));
            }
            if(!user.getUser_id().isEmpty()) {
                return user;
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
