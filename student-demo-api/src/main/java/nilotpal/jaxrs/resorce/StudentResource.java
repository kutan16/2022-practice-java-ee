package nilotpal.jaxrs.resorce;

import nilotpal.data.CommonData;
import nilotpal.entity.Credentials;
import nilotpal.entity.Student;
import nilotpal.service.CommonDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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
    //    private HttpHeaders httpHeaders;
    private static final String GET_ROLES_ALLOWED = "ADMIN,USER";
    private static final String POST_ROLES_ALLOWED = "ADMIN";
    private static final Logger log = LoggerFactory.getLogger(StudentResource.class);

    //    private final DbConfig dbConfig;
    private final CommonDataService commonDataService;

    @Inject
    public StudentResource(CommonDataService commonDataService) {
        this.commonDataService = commonDataService;
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
    public Response getStudent(@PathParam("id") Integer studentId, @Context HttpHeaders httpHeaders) {
        log.info("Inside : getStudent start");
        try {
            log.info(commonDataService.getClientList().toString());
            if(!checkForAuthorization("get", httpHeaders)) {
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

    private boolean checkForAuthorization(String method, HttpHeaders httpHeaders) throws NullPointerException {
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
        List<Credentials> creds = CommonData.getCredentials();
        if("GET".equalsIgnoreCase(method)) {
            return creds.stream().anyMatch(credentials -> credentials.getUsername().equalsIgnoreCase(user_credentials[0])
                    && credentials.getPassword().equalsIgnoreCase(user_credentials[1])
                    && (credentials.getRoles().contains(GET_ROLES_ALLOWED.split(",")[0].toLowerCase())
                    || credentials.getRoles().contains(GET_ROLES_ALLOWED.split(",")[1].toLowerCase())));
        } else if("POST".equalsIgnoreCase(method)) {
            return creds.stream().anyMatch(credentials -> credentials.getUsername().equalsIgnoreCase(user_credentials[0])
                    && credentials.getPassword().equalsIgnoreCase(user_credentials[1])
                    && credentials.getRoles().contains(POST_ROLES_ALLOWED));
        }
        return false;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postStudent(Student student) {
        log.info("Student Data received is : " + student.toString());
        return Response.status(Response.Status.OK)
                .entity("Student Created")
                .build();
    }

    private Student fetchStudent(Integer studentId, List<Student> students) {
        return students.stream()
                .filter(student -> studentId.equals(student.getStudent_id()))
                .findFirst().orElse(null);
    }

    /**
     * Injecting mock object of HttpHeaders for the sole purpose of Junit test
     *
     * @param httpHeaders - injects mock instance of HttpHeaders
     */
//    public void setHttpHeaders(HttpHeaders httpHeaders) {
//        this.httpHeaders = httpHeaders;
//    }
}
