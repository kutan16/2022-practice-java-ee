package nilotpal.service;

import nilotpal.entity.User;

import java.util.List;

public interface UserService {

    /**
     * Adds an User
     *
     * @param user A User entity
     * @return true/false
     */
    boolean addUser(User user);

    /**
     * Delete an User
     *
     * @param userId The user_id of the User
     * @return true/false
     */
    boolean deleteUser(String userId);

    /**
     * Returns the list of all the users present in the database
     *
     * @return list of all users
     */
    List<User> listAllUsers();

    /**
     * Returns a user
     *
     * @param userId The userId of the User
     * @return The User
     */
    User getUser(String userId);
}
