package nilotpal.jaxrs.resorce;

import nilotpal.data.CommonData;
import nilotpal.entity.Client;
import nilotpal.entity.Credentials;
import nilotpal.entity.Student;
import nilotpal.entity.User;
import nilotpal.service.ServiceInterface;
import nilotpal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.List;

/**
 * Resource Class  which exposes /student api
 */
@Path("student")
public class StudentResource {
    private static final String GET_ROLES_ALLOWED = "ADMIN,USER";
    private static final String POST_ROLES_ALLOWED = "ADMIN";
    private static final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private final HttpHeaders httpHeaders;
    private final ServiceInterface studentService;
    private final ServiceInterface employeeService;
    private final UserService userService;

    /**
     * Injecting dependencies via the Constructor
     *
     * @param httpHeaders     HttpHeaders from request context
     * @param studentService  The Student Service implementation class with connection to Db
     * @param employeeService An Employee Service implementation class
     * @param userService     A User Service implementation class
     */
    @Inject
    public StudentResource(HttpHeaders httpHeaders,
                           @Named("studentService") ServiceInterface studentService,
                           @Named("employeeService") ServiceInterface employeeService,
                           @Named("userService") UserService userService) {
        this.httpHeaders = httpHeaders;
        this.studentService = studentService;
        this.employeeService = employeeService;
        this.userService = userService;
    }

    /**
     * Simply checks for the provided studentId in the Student Data.
     *
     * @param studentId - provided by the endUser
     * @return A Student entity in the form of Json object
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") Integer studentId) {
        log.info("Inside : getStudent start");
        try {
            if(!checkForAuthorization()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization received is incorrect or does not have proper role")
                        .build();
            }
        } catch (Exception e) {
            log.info("Inside : getStudent exception block");
            log.error(e.toString());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("error occurred")
                    .build();
        }
        log.info("Student id received : " + studentId);
        List<Student> students = CommonData.getStudents();
        Student result = fetchStudent(studentId, students);
        log.info("Fetched student data : " + result);
        Response response;
        if(result == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Student not found with StudentId : " + studentId)
                    .build();
        }
        return Response.status(Response.Status.OK)
                .entity(result)
                .build();
    }

    private boolean checkForAuthorization() throws NullPointerException {
        String auth = httpHeaders.getHeaderString("Authorization");
        String authToken;
        String decodedToken;
        String[] user_credentials;
        if(null != auth && auth.contains("Basic")) {
            String[] token = auth.split(" ");
            authToken = token[1];
            decodedToken = new String(Base64.getDecoder().decode(authToken));
            user_credentials = decodedToken.split(":");
        } else {
            return false;
        }
        List<Credentials> credentials = CommonData.getCredentials();
        return credentials.stream().anyMatch(credentialsMatch -> credentialsMatch.getUsername().equalsIgnoreCase(user_credentials[0])
                && credentialsMatch.getPassword().equalsIgnoreCase(user_credentials[1])
                && (credentialsMatch.getRoles().contains(GET_ROLES_ALLOWED.split(",")[0].toLowerCase())
                || credentialsMatch.getRoles().contains(GET_ROLES_ALLOWED.split(",")[1].toLowerCase())));
    }

    private Student fetchStudent(Integer studentId, List<Student> students) {
        return students.stream()
                .filter(student -> studentId.equals(student.getStudent_id()))
                .findFirst().orElse(null);
    }

    /**
     * Create resource of single user
     *
     * @param user accept a user object
     * @return response if user created or not
     */
    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUsers(User user) {
        log.info("User Data received is : " + user.toString());
        if(!checkForAuthorization()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization received is incorrect or does not have proper role")
                    .build();
        }
        if(userService.addUser(user)) {
            return Response.status(Response.Status.OK)
                    .entity("User Created")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Problems occurred while adding the user")
                    .build();
        }
    }

    /**
     * Returns the whole list of Users
     *
     * @return A list of users from the DB
     */
    @GET
    @Path("users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        if(!checkForAuthorization()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization received is incorrect or does not have proper role")
                    .build();
        }
        List<User> listOfUsers = userService.listAllUsers();
        if(null != listOfUsers) {
            return Response.status(Response.Status.OK)
                    .entity(listOfUsers)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No Users found in db")
                    .build();
        }
    }

    /**
     * Delete the user from the db
     *
     * @param user_id the user_id of the user based on which it will delete the user
     * @return Response indicating the use is deleted or not
     */
    @DELETE
    @Path("user/{user_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user_id") String user_id) {
        if(!checkForAuthorization()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization received is incorrect or does not have proper role")
                    .build();
        }
        if(userService.deleteUser(user_id)) {
            return Response.status(Response.Status.OK)
                    .entity("User with userId : " + user_id + " has been deleted from db")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No Users found in db")
                    .build();
        }
    }

    /**
     * Get list of Clients from database
     *
     * @return list of clients
     */
    @GET
    @Path("clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        log.info("Inside getClients Resource");
        if(!checkForAuthorization()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization received is incorrect or does not have proper role")
                    .build();
        }
        Client clients = studentService.fetchClients();
        if(null != clients) {
            return Response.status(Response.Status.OK)
                    .entity(clients.getClients())
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Clients not found in Db or DB connection failed ")
                    .build();
        }
    }

    /**
     * Get list of Clients from database
     *
     * @return list of clients
     */
    @GET
    @Path("clients/asd")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient_asd() {
        log.info("Inside getClient_asd Resource");
        if(!checkForAuthorization()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization received is incorrect or does not have proper role")
                    .build();
        }
        Client clients = employeeService.fetchClients();
        if(null != clients) {
            return Response.status(Response.Status.OK)
                    .entity(clients)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Clients not found in Db or DB connection failed ")
                    .build();
        }
    }
}
