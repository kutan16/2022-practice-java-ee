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
    private String validFrom;
    private String validTo;
    private String subject;
    private String domainName;
    private String organization;
    private String algorithm;
    private Location location;

    @Data
    public static class Location {
        private String region;
        private String province;
        private String locality;

        public Location(String region, String province, String locality) {
            this.region = region;
            this.province = province;
            this.locality = locality;
        }
    }


}
