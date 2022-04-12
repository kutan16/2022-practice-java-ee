package nilotpal.service;

import nilotpal.entity.Client;
import nilotpal.entity.Credentials;
import nilotpal.entity.Student;
import nilotpal.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * A Common Class which contains Student, Client & User Data
 */
public class CommonDataService {
    /**
     * Student Data
     *
     * @return A list of Students
     */
    public List<Student> getStudents() {
        return Arrays.asList(new Student(1, "jack", "computer science"),
                new Student(2, "ash", "electrical"),
                new Student(3, "oliver", "mechanical"),
                new Student(4, "sonia", "civil"),
                new Student(5, "shahid", "it"),
                new Student(6, "kate", "philosophy"));

    }

    /**
     * Client Data
     *
     * @return A Singleton list of Client
     */
    public List<Client> getClientList() {
        return Collections.singletonList(new Client("webappclient", "webappclientsecret", "http://localhost:9180/callback",
                "resource.read resource.write", "authorization_code refresh_token"));
    }

    /**
     * User Data
     *
     * @return A Singleton List of User
     */
    public List<User> getUserList() {
        return Collections.singletonList(new User("appuser", "appusersecret", "USER", "resource.read resource.write"));
    }

    /**
     * Credentials storage
     *
     * @return List of Credentials
     */
    public List<Credentials> getCredentials() {
        return Arrays.asList(new Credentials("nilotpal", "nilotpal1", new HashSet<>(Arrays.asList("admin", "user"))),
                new Credentials("johnny", "hello", new HashSet<>(Collections.singletonList("user"))));
    }
}
