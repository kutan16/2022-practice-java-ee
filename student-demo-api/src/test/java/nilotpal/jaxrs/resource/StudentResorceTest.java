//package nilotpal.service.resorce;
//
//import org.glassfish.jersey.internal.util.Base64;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class StudentResorceTest {
//
//    @Mock
//    private static HttpHeaders httpHeaders;
//    private static StudentResource studentResource;
//    private static final String GET_ROLES_ALLOWED = "ADMIN,USER";
//    private static final String POST_ROLES_ALLOWED = "ADMIN";
//
//    @Before
//    public void setUp() {
//        studentResource = new StudentResource();
//        studentResource.setHttpHeaders(httpHeaders);
//    }
//
//
//    @Test
//    public void testGetStudent() {
//        when(httpHeaders.getHeaderString("Authorization")).thenReturn(Base64.encodeAsString("nilotpal:nilotpal1"));
//        Response response = studentResource.getStudent(1);
//    }
//}
