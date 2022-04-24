package nilotpal.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Class for storing certificate information
 */
@Data
public class CertificateInformation {

    private String version;
    private String issuer;
    private Date validFrom;
    private Date validTo;
    private String subject;
    private List<String> domainNames;
    private String organization;
    private Location location;

    @Data
    static class Location {
        private String region;
        private String province;
        private String locality;
    }


}
